package org.zerock.sb.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.sb.entity.Board;
import org.zerock.sb.entity.Reply;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTests {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insert200(){

        IntStream.rangeClosed(1,200).forEach(i -> {

            Long bno = (long)200 - ( i % 5 );//200, 199, 198, 197, 196

            int replyCount = (i % 5); // 0, 1,2,3,4

            IntStream.rangeClosed(0,replyCount).forEach(j -> {

                Board board = Board.builder().bno(bno).build();//bno만 있으면 어떤 보드인지 확인 가능

                Reply reply = Reply.builder()
                        .replyText("Reply...........")
                        .replyer("replyer..............")
                        .board(board)
                        .build();
                    //리플라이에서 보드를 물고 있는 단방향 참조관계

                replyRepository.save(reply);
            });//inner loop

        });//out loop
    }

    @Transactional
    @Test
    public void testRead(){

        Long rn = 1L;

        Reply reply = replyRepository.findById(rn).get();
        log.info(reply);
        //test하면 안되는게 정상 EAGAR로 바꾸면 되는 듯 보이지만 다른 문제가 발생한다.
        //Lazy로 했을 때 no Session 오류 (커넥션이 안됨)
    }

    @Test
    public void testByBno(){
        Long bno = 200L;

        List<Reply> replyList
                = replyRepository.findReplyByBoard_BnoOrderByRno(bno);

        replyList.forEach(reply -> log.info(reply));//@ToString수정안하면 오류임 (리스트는 가져오나 보드도 함께 ToString하면서 오류가 남)
    }

    @Test
    public void testCountOfBoard(){

        Long bno = 198L;

        //120
        int count = replyRepository.getReplyCountOfBoard(bno);
        int lastpage = (int) (Math.ceil(count/10.0));

        if(lastpage == 0){//무플일때 마지막페이지가 0보다 작으면 문제발생  그래서 마지막페이지가 0이면 1로 정의한다.
            lastpage = 1;
        }

//        // 120/10.0 => 12 * 10 = 120 - 10  limit 110,120
//        int lastpageNum = (int)(Math.ceil (count / (double)10));
//
//        int lastEnd = lastpageNum * 10;
//        int lastStart = lastEnd - 10;
//
//        log.info(lastStart + " : " + lastEnd);

        //0부터 시작하는 페이지번호, 사이즈, 소트
        Pageable pageable = PageRequest.of(lastpage -1, 10); // 마지막페이지의 -1 , 10개씩 가져오기

        Page<Reply> result = replyRepository.getListByBno(bno,pageable);//해당 게시물의 댓글의 목록
        // 댓글 총갯수를 가져오려면 findAll(pageable)

        log.info("total:" + result.getTotalElements()); // 해당 번호의 댓글 개수
            log.info("..." + result.getTotalPages()); // 댓글의 마지막 페이지

        result.get().forEach(reply -> {
            log.info(reply);
        });
    }
}
