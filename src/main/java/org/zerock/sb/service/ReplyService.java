package org.zerock.sb.service;

import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;
import org.zerock.sb.dto.ReplyDTO;

import javax.transaction.Transactional;

@Transactional
public interface ReplyService {

    PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO);

    Long register(ReplyDTO replyDTO);//서비스계층은 항상 dto로 받음

    PageResponseDTO<ReplyDTO> remove(long bno, long rno, PageRequestDTO pageRequestDTO);

    PageResponseDTO<ReplyDTO> modify(ReplyDTO replyDTO, PageRequestDTO pageRequestDTO);

}