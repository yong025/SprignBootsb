package org.zerock.sb.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.sb.entity.Member;
import org.zerock.sb.entity.MemberRole;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class MemberRepositoryTests {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertMembers(){

        IntStream.rangeClosed(1,100).forEach(i -> {

            Set<MemberRole> roleSet = new HashSet<>();
            roleSet.add(MemberRole.USER);//(0권한)

            if(i >= 50){// 50~79까지는 판매자(2번 들어감 0,1 권한)
              roleSet.add(MemberRole.STORE);
            }
            if(i >= 80){//80~100까지는 관리자(3번 들어감 0,1,2 권한)
                roleSet.add(MemberRole.ADMIN);
            }

            Member member = Member.builder()
                    .mid("user" + i)
                    .mpw("1111")
                    .mname("사용자" + i)
                    .roleSet(roleSet)
                    .build();

            memberRepository.save(member);

        });
    }

    @Test
    public void updateMembers(){

        List<Member> memberList = memberRepository.findAll(); //유저리스트를 모두 찾아온다.

        memberList.forEach(member -> {
            member.changePassword(passwordEncoder.encode("1111"));//1111을 암호화로 변경
            //같은 1111이지만 다 다름 으깨진 모양으로 확인      -     복호화가 안됨

            memberRepository.save(member);//패스워드 암호화로 변경해서 넣어준다.
        });

    }

}
