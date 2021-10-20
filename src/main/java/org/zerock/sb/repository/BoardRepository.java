package org.zerock.sb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.sb.entity.Board;
import org.zerock.sb.repository.search.BoardSearch;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardSearch {//Board Entity와 식별값 Id Long타입


    //아직 검색은 안되지만 페이징처리하면서 게시글마다 댓글 개수도 가져오기
    //JPQL
    @Query("select b , count(r) from Board b left join Reply r on r.board = b group by b")//reply entity안의 board 와 board를 매개로 묶어줌
    //board를 기준으로 압축해서 보여줘야하기 때문에 group by b 사용
    Page<Object[]> ex1(Pageable pageable);
    //무조건적인 규칙 2가지
    //파라미터가 Pageable면 리턴값은 무조건 Page<>다.
    //select (2개 이상) 가 나오면 무조건 Object의 배열로 받아야한다.

}
