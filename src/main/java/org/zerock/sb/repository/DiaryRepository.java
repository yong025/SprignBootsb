package org.zerock.sb.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.sb.entity.Diary;
import org.zerock.sb.repository.search.DiarySearch;

public interface DiaryRepository extends JpaRepository<Diary, Long> , DiarySearch {

    //from 뒤에는 무조건 entity d.tags는 컬럼이 문자열하나로되있기때문에 on조건 줄 수 없다.
    @Query("select d from Diary d left join d.tags dt where dt like concat('%',:tag,'%')")//인텔리제이 오류나오지만 사용가능
    Page<Diary> searchTags(String tag, Pageable pageable);

    //value를 주고 nativeQuery로 실제 쿼리를 사용할 수도 있다.
    //혹은 projection을 사용해서 뽑아올 수도 있다.
    @Query("select d, coalesce(sum(f.score), 0) from Diary d left join Favorite f on f.diary = d group by d")
    Page<Object[]> findWithFavoriteCount(Pageable pageable);
}
