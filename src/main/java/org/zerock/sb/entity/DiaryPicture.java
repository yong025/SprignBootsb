package org.zerock.sb.entity;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable // 얘가 걸려야지 elementCollection으로 처리가 가능해진다. //값객체라 pk필요없음
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
@EqualsAndHashCode(of = "uuid") //uuid가 같으면 같다라고 인정 (중복된 데이터가 들어가기 힘들어짐)
public class DiaryPicture implements Comparable<DiaryPicture>{

    private String uuid;
    private String fileName;
    private String savePath;
    private int idx;

    @Override
    public int compareTo(DiaryPicture o) {
        return o.idx - this.idx;
    }
}
