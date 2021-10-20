package org.zerock.sb.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(of = "uuid") //uuid가 같으면 같다라고 인정 (중복된 데이터가 들어가기 힘들어짐)
public class DiaryPictureDTO {

    private String uuid;
    private String fileName;
    private String savePath;
    private int idx;

    public String getLink(){
        return savePath+"/s_"+uuid+"_"+fileName;
    }

}
