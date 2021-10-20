package org.zerock.sb.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardDTO {

//DTO는 어노테이션 필요없음
    private Long bno;
    private String title;
    private String  content;
    private String writer;
    private LocalDateTime regDate;
    private LocalDateTime modDate;
}
