package org.zerock.sb.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardListDTO {

    private Long bno;
    private String title;
    private String writer;
    private LocalDateTime regDate;
    private long replyCount;

}
