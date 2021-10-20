package org.zerock.sb.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity//(꼭 변수에 ID값을 정해줘야 함)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member","diary"})
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long fno;

    @ManyToOne (fetch = FetchType.LAZY) // 연관관계를 줘야 Diary 객체를 불러올 수 있다.
    private Diary diary;

    @ManyToOne (fetch = FetchType.LAZY)
    private Member member;

    private int score; //좋아요를 누르면 +1 싫어요는 -1을 하기 위함

    @CreationTimestamp
    private LocalDateTime regDate;

}
