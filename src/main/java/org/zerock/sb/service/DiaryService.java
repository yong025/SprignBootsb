package org.zerock.sb.service;

import org.zerock.sb.dto.*;

import javax.transaction.Transactional;

@Transactional
public interface DiaryService {

    Long register(DiaryDTO dto);

    DiaryDTO read(Long dno);

    PageResponseDTO<DiaryDTO> getList(PageRequestDTO pageRequestDTO);

    PageResponseDTO<DiaryListDTO> getListWithFavorite(PageRequestDTO pageRequestDTO);
}
