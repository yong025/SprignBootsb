package org.zerock.sb.repository;


import com.google.common.collect.Sets;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.sb.dto.DiaryDTO;
import org.zerock.sb.entity.Diary;
import org.zerock.sb.entity.DiaryPicture;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class DiaryRepositoryTests {

    @Autowired
    DiaryRepository diaryRepository;

    @Autowired
    ModelMapper modelMapper;

    @Test
    public void testInsert() {

        IntStream.rangeClosed(1, 100).forEach(i -> {

            Set<String> tags = IntStream.rangeClosed(1, 3).mapToObj(j -> i + "_tag_" + j).collect(Collectors.toSet());
            //mapToObj 1번다이어리 글에 1_tag_2이렇게 만들어준다.

            Set<DiaryPicture> pictures = IntStream.rangeClosed(1,3).mapToObj(j -> {
                DiaryPicture picture = DiaryPicture.builder()
                        .uuid(UUID.randomUUID().toString())
                        .savePath("2021/10/18")
                        .fileName("img" + j + ".jpg")
                        .idx(j)
                        .build();
                return picture;
            }).collect(Collectors.toSet());

            Diary diary = Diary.builder()
                    .title("title.." + i)
                    .content("content.." + i)
                    .writer("user00")
                    .tags(tags)
                    .pictures(pictures)
                    .build();

            diaryRepository.save(diary);
        });
    }

    @Test
    public void testSelectOne() {//entity에서 fetch join을 사용하면 lazy상태에서도 left outer join을 사용해서 다이어리와 태그를 한번에 가져온다.
        Long dno = 1L;

        Optional<Diary> optionalDiary = diaryRepository.findById(dno);

        Diary diary = optionalDiary.orElseThrow();

        log.info(diary); //diary를 가져올때 ToString가 걸리고 tbl_diary_tag에 또 select를 날려야하는데 두개를 한번에 날릴 수 없음(no Session)
        log.info(diary.getTags());
        //1번 선택 시나리오 exclude 사용해서 tags를 안가져오도록한다. 1번 주석 - 무조건 해놓는게 좋음 하지만 원초적인 수정은 아님
        //2번 선택 트랜잭션으로 한번에 가져오도록 한다. 2번 주석 - 계속 연결해서 diary가져온 후 다시한번 select날려서 _tags를 또 가져온다.(o)
        //3번 선택 애초에 EAGAR로딩으로 가져온다.(테이블 두개를 조인해서 한번에 가져온다)(x)
        // 3번 주석 - 다이어리를 조회하는 화면이라면 다이어리를 가져오면서 태그를 함께 가져와서 좋아보임 - 그러나 조회할 때만 좋음 - 목록을 가져올 때는 우리가 원하는대로 동작이 필요함

        log.info(diary.getPictures());
    }

    @Transactional // noSession나오면 무조건 걸어야한다.
    @Test
    public void testPaging1() {//목록조회
        Pageable pageable = PageRequest.of(0, 10, Sort.by("dno").descending());

        Page<Diary> result = diaryRepository.findAll(pageable);
        //eagar로 로딩하면 셀렉트를 12번해서 가져옴 - 수가 많아지면 서버 다운 가능성 多
        //lazy로 하면 2번해서 카운트로 가져옴 (하지만 갯수만 가져오기때문에 해시태그를 가져오려면 결굴 다시 select날려야함)

        result.get().forEach(diary -> {
            log.info(diary);
            log.info(diary.getTags());
            //쿼리를 한두번만 날려서 가져오는게 가장 best이지만 현실적으로 어려움
            //in조건으로 쿼리를 세네번 날리긴 하지만 n+1문제 해결이 가능하다.
            //성능 상 손해를 보긴 하지만 화면목록에서 모든 이미지를 보여 줄 필요가 있었기 때문에 JOIN FETCH를 사용했다.
            log.info(diary.getPictures());
        });
    }

    @Test
    public void testSelectOne2() {
        Long dno = 1L;

        Optional<Diary> optionalDiary = diaryRepository.findById(dno);

        Diary diary = optionalDiary.orElseThrow();

        DiaryDTO diaryDTO = modelMapper.map(diary, DiaryDTO.class); //entity를 dto로 변경

        log.info(diaryDTO);
    }

    @Test
    public void testSearchTags(){
        String tag = "1";
        Pageable pageable = PageRequest.of(0,10, Sort.by("dno").descending());

        Page<Diary> result = diaryRepository.searchTags(tag,pageable);

        result.get().forEach(diary -> {
            log.info(diary);
            log.info(diary.getTags());
            log.info(diary.getPictures());
            log.info("--------------------");
        });
    }

    @Test
    public void testDelete(){

        Long dno = 203L;

        diaryRepository.deleteById(dno);
    }

    @Commit
    @Transactional
    @Test
    public void testUpdate(){

        Set<String> updateTags
                = Sets.newHashSet("aaa","bbb","ccc");

        Set<DiaryPicture> updatePictures
                = IntStream.rangeClosed(10,15).mapToObj(i-> {
            DiaryPicture picture =
                    DiaryPicture.builder()
                            .uuid(UUID.randomUUID().toString())
                            .savePath("2021/10/19")
                            .fileName("Test"+i+".jpg")
                            .idx(i)
                            .build();

            return picture;
        }).collect(Collectors.toSet());

        Optional<Diary> optionalDiary = diaryRepository.findById(202L);

        Diary diary = optionalDiary.orElseThrow();

        diary.setTitle("Updated title 103");
        diary.setContent("Update content 103");
        diary.setTags(updateTags);
        diary.setPictures(updatePictures);

        diaryRepository.save(diary);

    }

    @Test
    public void testWithFavorite(){

        Pageable pageable = PageRequest.of(0,10, Sort.by("dno").descending());

        Page<Object[]> result = diaryRepository.findWithFavoriteCount(pageable);

        for(Object[] objects : result.getContent()){
            log.info(Arrays.toString(objects));
        }

    }

    @Test
    public void testListHard(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("dno").descending());

        diaryRepository.getSearchList(pageable);

    }
}
