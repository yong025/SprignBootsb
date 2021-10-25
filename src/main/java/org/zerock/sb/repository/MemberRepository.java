package org.zerock.sb.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.sb.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    @Query("select m from Member m where m.mid = :mid") // 정적 쿼리메서드사용
    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> getMemberEager(String mid);

}
