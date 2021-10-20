package org.zerock.sb.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@ToString(exclude = "board")//board는 빼고 ToString해라 (연관관계에서 제외) 잘못되면 시스템이 다운될 수 있다.
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String replyText;
    private String replyer;

    @ManyToOne(fetch = FetchType.LAZY)//기본 LAZY로 걸어야함 , EAGAR는 Reply를 가져올 때 board정보도 같이 가져오는 설정.(현재는 필요없음)
    private Board board;
    //mybatis와 다른점은 이제는 객체:객체의 관계로 걸어야 한다.
    //관계를 서술해 줘야 오류가 안남.


    @CreationTimestamp
    private LocalDateTime replyDate;

    public void setText(String text){
        this.replyText = text;
    }

}
