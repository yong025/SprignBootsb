package org.zerock.sb.entity;

import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

import java.util.HashSet;
import java.util.Set;

@Entity//(꼭 변수에 ID값을 정해줘야 함)
@Table(name = "tbl_diary")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"tags", "pictures"})
public class Diary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long dno;

    private String title;

    private String content;

    private String writer;

    @CreationTimestamp //등록시간 자동관리(하이버네이트)
    private LocalDateTime regDate;

    @UpdateTimestamp//수정시간 자동관리
    private LocalDateTime modDate;

    @ElementCollection(fetch = FetchType.LAZY) // 해시태그는 종속적인 관계이기 때문에 ElementCollection을 줘야한다.
    @CollectionTable(name = "tbl_diary_tag")//tags 테이블명을 지정해서 생성
    @Fetch(value = FetchMode.JOIN)
    //FetchMode.SELECT(count 먼저 날리고 필요할때까지 기다리다가 tag에 대한 select날림)
    // FetchMode.SUBSELECT(select와 where조건이 다르지만 가져오는 결과는 같음)
    //FetchMode.JOIN(처음부터 tag에 대한 select날리고 count가져옴)
    // join fetch를 사용해서 n+1문제를 해결한다.
    @BatchSize(size = 50)// fetch와 batchsize를 사용하면 in조건을 사용해서 쿼리를 날려서 가져온다.
    @Builder.Default
    private Set<String> tags = new HashSet<>();//단독의 의미를 가지지않고, 단순히 서술만해준다. 다이어리에 종속된 관계
    //다이어리 게시글 수정시 tags의 내용은 전체삭제되고 다시 넣는다.

    //테이블 생성시 자동으로 tbl_diary_tags라는 테이블을 생성해주고 diary_dno라는 FK를 자동으로 잡아준다.


    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tbl_diary_picture")//다이어리 자체를 서술해준다.
    @Fetch(value = FetchMode.JOIN)//ElementCollection에만 쓰는게 아니라 OneToMany에도 사용가능
    @BatchSize(size = 50)
    private Set<DiaryPicture> pictures;
    // 화면에서 처리는 List - Entity에서는 Set로 정의

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public void setPictures(Set<DiaryPicture> pictures) {
        this.pictures = pictures;
    }
}
