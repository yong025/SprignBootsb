package org.zerock.sb.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

public class DiaryListDTO { //리스트니까 내용 수정날짜 필요없음

    private Long dno;

    private String title;

    private String writer;

    private LocalDateTime regDate;

    private List<String> tags; //화면에 출력할때 함께 출력해야하는 변수들
    //파라미터 수집시 인덱스를 줄려면 List로 주는것이 편하다.
    // 화면에서 처리는 List - Entity에서는 Set로 정의
    private List<DiaryPictureDTO> pictures;

    private int totalScore;
}
