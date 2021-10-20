package org.zerock.sb.service;

import org.springframework.transaction.annotation.Transactional;
import org.zerock.sb.dto.BoardDTO;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;

@Transactional
public interface BoardService {

    Long register(BoardDTO boardDTO); //글작성 후 번호 리턴
    
    PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO);

    BoardDTO read(Long bno);

    void modify(BoardDTO boardDTO);

    void remove(Long bno);
}
