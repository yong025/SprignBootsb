package org.zerock.sb.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.sb.dto.PageRequestDTO;
import org.zerock.sb.dto.PageResponseDTO;
import org.zerock.sb.dto.ReplyDTO;
import org.zerock.sb.entity.Reply;
import org.zerock.sb.repository.ReplyRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;
    private final ModelMapper modelMapper;

    @Override
    public PageResponseDTO<ReplyDTO> getListOfBoard(Long bno, PageRequestDTO pageRequestDTO) {//page -1이면 총 댓글 수를 가져오는 로직필요

        Pageable pageable = null;

        if(pageRequestDTO.getPage() == -1){
            int lastPage = calcLastPage(bno, (double) pageRequestDTO.getSize()); // -1 댓글이 없는 경우, 숫자 마지막 댓글 페이지 ,

            if(lastPage <= 0){//라스트페이지가 0보다 작거나 같다면
                lastPage = 1;
            }
            pageRequestDTO.setPage(lastPage);//음수가 되지않도록 세팅을 바꿔줌
        }

        pageable = PageRequest.of(pageRequestDTO.getPage() -1, pageRequestDTO.getSize());


        Page<Reply> result = replyRepository.getListByBno(bno, pageable);

        List<ReplyDTO> replyDTOList = result.get()
                .map(reply -> modelMapper.map(reply, ReplyDTO.class))
                .collect(Collectors.toList());

        //replyDTOList.forEach(replyDTO -> log.info(replyDTO));

        return new PageResponseDTO<>(pageRequestDTO, (int)result.getTotalElements(), replyDTOList);
    }

    @Override
    public Long register(ReplyDTO replyDTO) {

        Reply reply = modelMapper.map(replyDTO, Reply.class);//modelMapper로 dto변환

        log.info(reply);
        replyRepository.save(reply);

        return reply.getRno();
    }

    @Override
    public PageResponseDTO<ReplyDTO> remove(long bno, long rno, PageRequestDTO pageRequestDTO) {
        replyRepository.deleteById(rno);

        return getListOfBoard(bno, pageRequestDTO);
    }

    @Override
    public PageResponseDTO<ReplyDTO> modify(ReplyDTO replyDTO, PageRequestDTO pageRequestDTO) {

        //우선 원본가져오기, findById
        Reply reply = replyRepository.findById(replyDTO.getRno()).orElseThrow();

        reply.setText(replyDTO.getReplyText());

        replyRepository.save(reply);
        return getListOfBoard(replyDTO.getBno(), pageRequestDTO);// 목록 리턴하기
    }

    private int calcLastPage(Long bno, Double size) {//마지막 페이지만 계산해서 가져오는 로직

        int count = replyRepository.getReplyCountOfBoard(bno);
        int lastpage = (int) (Math.ceil(count/size));

        if(lastpage == 0){//무플일때 마지막페이지가 0보다 작으면 문제발생  그래서 마지막페이지가 0이면 1로 정의한다.
            lastpage = 1;

        }
        return lastpage;//마지막 페이지번호로 반환
    }
}