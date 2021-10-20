package org.zerock.sb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.sb.dto.BoardDTO;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;
import org.zerock.sb.entity.Board;
import org.zerock.sb.repository.BoardRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final ModelMapper modelMapper;
    private final BoardRepository boardRepository;

    @Override
    public Long register(BoardDTO boardDTO) {

        //DTO -> Entity
        Board board = modelMapper.map(boardDTO, Board.class);
        //repository save( ) (save는 등록 수정할때 사용) -> Long
        boardRepository.save(board);

        return board.getBno();
    }

    @Override
    public PageResponseDTO<BoardDTO> getList(PageRequestDTO pageRequestDTO) {
        char[] typeArr = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize(),
                Sort.by("bno").descending());
        //pageable은 공식처럼 0부터 시작하지만 실제 페이지는 1 부터시작하기 때문에 맞춰주기 위해 -1해줘야 함.

        Page<Board> result = boardRepository.search1(typeArr, keyword, pageable);

        List<BoardDTO> dtoList = result.get().map(
                        board -> modelMapper.map(board, BoardDTO.class))
                .collect(Collectors.toList());
        long totalCount = result.getTotalElements();

        return new PageResponseDTO<>(pageRequestDTO, (int) totalCount, dtoList);
    }

    @Override
    public BoardDTO read(Long bno) {

        Optional<Board> result = boardRepository.findById(bno); //id(프라이머리키)를 가져와서 result에 담는다
        //Optional이란 'null일 수도 있는 객체'를 감싸는 일종의 Wrapper 클래스입니다.
        //Optional<Baord> optional
        //즉, 이러한 optional 변수 내부에는 null이 아닌 T 객체가 있을 수도 있고 null이 있을 수도 있습니다.
        // //따라서, Optional 클래스는 여러 가지 API를 제공하여 null일 수도 있는 객체를 다룰 수 있도록 돕습니다.

        if (result.isEmpty()) {//게시글이 없다면 예외처리
            throw new RuntimeException("NOT FOUND");
        }

        return modelMapper.map(result.get(), BoardDTO.class);//result.get이 결과문(게시글)
    }

    @Override
    public void modify(BoardDTO boardDTO) {
        //추가정보가 있을 수 있기 때문에 findById를 사용하는 것이 좋다/
        Optional<Board> result = boardRepository.findById(boardDTO.getBno());//DTO안의 bno(프라이머리키)에서 찾아온다.

        if (result.isEmpty()) {
            throw new RuntimeException("NOT FOUND");
        }
        Board board = result.get();
        board.change(boardDTO.getTitle(), boardDTO.getContent());//수정을 위해 boardDTO의 title,content를 가져와 변경해 entity에 넣는다.
        boardRepository.save(board);//수정값을 넣어서 board에 저장해준다.

    }

    @Override
    public void remove(Long bno) {

        boardRepository.deleteById(bno);//삭제할 ID(프라이머리키)를 가져와 넣으면 삭제.

    }
}



