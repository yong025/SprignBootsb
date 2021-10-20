package org.zerock.sb.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.sb.dto.BoardDTO;
import org.zerock.sb.entity.Board;

import java.util.Arrays;

@SpringBootTest
@Log4j2
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void testSearch1(){

        char[] typeArr = null;
        String keyword = null;
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());


        Page<Board> result = boardRepository.search1(typeArr, keyword, pageable);

        result.get().forEach(board -> {
            log.info(board);

            BoardDTO boardDTO = modelMapper.map(board, BoardDTO.class); //board를 boardDTO로 변환해준다 .
        });
    }

    @Test
    public void testEx1(){

        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.ex1(pageable);

        log.info(result);

        result.get().forEach(element -> {

            Object[] arr = (Object[])element;

            log.info(Arrays.toString(arr));

                });
    }
}
