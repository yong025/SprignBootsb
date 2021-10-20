package org.zerock.sb.entity;

import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.Set;

@Entity//(꼭 변수에 ID값을 정해줘야 함)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member {

    @Id
    private String mid; //여기에는 소셜로그인의 경우가 있기때문에 Auto Increment사용안함

    private String mpw;

    private String mname;

    //회원권한이 있음 멤버롤도 함께 설계
    @ElementCollection(fetch = FetchType.LAZY)
    private Set<MemberRole> roleSet;//entity는 Set으로 설정
}


