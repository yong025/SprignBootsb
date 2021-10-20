package org.zerock.sb.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.sb.entity.Diary;
import org.zerock.sb.entity.Favorite;
import org.zerock.sb.entity.Member;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class FavoriteRepositoryTests {

    @Autowired
    private FavoriteRepository favoriteRepository;

    //이 코드를 돌리면 Member 테이블만 점검하고 Diary 테이블은 점검하지 않음 -> Diary의 dno는 A.I 이기 때문에
    @Test
    public void insertDummies() {

        IntStream.rangeClosed(1, 100).forEach(i -> {

            Long dno = (long)(96 + (i % 5));
            String mid = "user" + i;

            Diary diary = Diary.builder().dno(dno).build();
            Member member = Member.builder().mid(mid).build();

            Favorite favorite = Favorite.builder()
                    .member(member)
                    .diary(diary)
                    .score(1)
                    .build();

            favoriteRepository.save(favorite);

        });
    }



}
