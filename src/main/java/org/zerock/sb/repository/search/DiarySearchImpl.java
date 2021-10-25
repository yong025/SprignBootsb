package org.zerock.sb.repository.search;

import com.querydsl.jpa.JPQLQuery;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.zerock.sb.entity.Diary;
import org.zerock.sb.entity.QDiary;
import org.zerock.sb.entity.QDiaryPicture;
import org.zerock.sb.entity.QFavorite;

@Log4j2
public class DiarySearchImpl extends QuerydslRepositorySupport implements DiarySearch{


    public DiarySearchImpl() {
        super(Diary.class);
    }

    @Override
    public Page<Object[]> getSearchList(Pageable pageable) {

        log.info("getSearchList............................");

        QDiary qDiary = QDiary.diary; // 쿼리dsl
        QFavorite qFavorite = QFavorite.favorite;
        QDiaryPicture qDiaryPicture = new QDiaryPicture("pic"); // 엔티티가 아니기 때문에 new로 선언해줘야함

        JPQLQuery<Diary> query = from(qDiary);
        query.leftJoin(qDiary.tags);
        query.leftJoin(qDiary.pictures, qDiaryPicture);

        query.leftJoin(qFavorite).on(qFavorite.diary.eq(qDiary)); //favorite와 diary 조인
        query.groupBy(qDiary);

        getQuerydsl().applyPagination(pageable, query);//페이징처리

        query.select(qDiary.dno, qDiary.title, qFavorite.score.sum());

        log.info("query:" + query);

        query.fetch();


        return null;
    }
}
