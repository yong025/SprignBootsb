package org.zerock.sb.service;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.sb.dto.PageRequestDTO;

@SpringBootTest
@Log4j2
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;

    @Test
    public void getListOfBoardTest() {
        Long bno = 199L;

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(-1)//페이지 -1을 주고 총 댓글개수를 파악하도록 함.
                .build();


        log.info(replyService.getListOfBoard(bno, pageRequestDTO)); //log로 페이지 천제 댓글수 확인
    }

}

