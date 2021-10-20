package org.zerock.sb.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;
import org.zerock.sb.dto.ReplyDTO;
import org.zerock.sb.service.ReplyService;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/replies")
public class ReplyController {

    private final ReplyService replyService;

    @GetMapping("/list/{bno}")
    public PageResponseDTO<ReplyDTO> getListOfBoard(@PathVariable("bno") Long bno, PageRequestDTO pageRequestDTO) {

        return replyService.getListOfBoard(bno, pageRequestDTO);

    }

    @PostMapping("")
    public PageResponseDTO<ReplyDTO> register(@RequestBody ReplyDTO replyDTO){//json데이터 받으려면 RequestBody사용

        replyService.register(replyDTO);

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(-1).build();

        return replyService.getListOfBoard(replyDTO.getBno(), pageRequestDTO);
    }

    @DeleteMapping("/{bno}/{rno}")
    public PageResponseDTO<ReplyDTO> remove(@PathVariable("rno")Long rno,
                                            @PathVariable("bno")Long bno, PageRequestDTO pageRequestDTO){

        return replyService.remove(bno,rno,pageRequestDTO);
    }

    @PutMapping("/{bno}/{rno}")
    public PageResponseDTO<ReplyDTO>modify(@PathVariable("rno")Long rno,
                                           @PathVariable("bno")Long bno,
                                           @RequestBody ReplyDTO replyDTO,
                                           PageRequestDTO requestDTO){

        log.info("bno" + bno);
        log.info("rno" + rno);
        log.info("replyDTO" + replyDTO);

        return replyService.modify(replyDTO, requestDTO);
    }

}
