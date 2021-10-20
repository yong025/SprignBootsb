package org.zerock.sb.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.sb.entity.Board;
import org.zerock.sb.entity.QBoard;


import java.util.List;

@Log4j2
public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch{//구현할 클래스는 네이밍 룰이 있다. 인터페이스의 이름에 Impl만 붙여야함.

    public BoardSearchImpl(){
        super(Board.class);
    }

    @Override
    public Page<Board> search1(char[] typeArr, String keyword, Pageable pageable) {
        log.info("search1----------------------------------------");

        //이 방식이 표준 방식
        QBoard board = QBoard.board; // 쿼리를 보낼 수 있는 객체
        //QueryDSL을 사용하여 메소드 기반으로 쿼리를 작성할 때
        //우리가 만든 도메인 클래스의 구조를 설명해주는 메타데이터 역할을 하며
        //쿼리의 조건을 설정할 때 사용된다.
        JPQLQuery<Board> jpqlQuery = from(board);//board로부터

        //검색조건이 있다면 실행한다.
        if(typeArr != null && typeArr.length > 0){

            BooleanBuilder condition = new BooleanBuilder(); //괄호 열고 괄호 닫고의 개념 or조건을 주도록 변경하기 위해 사용

            for(char type: typeArr){

                if(type == 'T'){
                    condition.or(board.title.contains(keyword));//board의 title에서 keyword가 포함된 문자
                }else if(type == 'C'){
                    condition.or(board.content.contains(keyword));
                }else if(type == 'W'){
                    condition.or(board.content.contains(keyword));

                }
            }
            jpqlQuery.where(condition);//where조건에 condition을 준다.
        }


        jpqlQuery.where(board.bno.gt(0L)); // bno가 0보다 크다는 조건을 준다.(Great Then)
        jpqlQuery.fetch(); //fetch가 실제 실행

        JPQLQuery<Board> pagingQuery =
                this.getQuerydsl().applyPagination(pageable, jpqlQuery);

        List<Board> boardList = pagingQuery.fetch(); //fetch는 실제 실행을 시키는 기능
        long totalCount = pagingQuery.fetchCount();


        return new PageImpl<> (boardList, pageable, totalCount);
    }
}
