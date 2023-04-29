package com.list.springsecurityjwt.repository;

import com.list.springsecurityjwt.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);
    Optional<Member> findMemberByName(String name);
    Boolean existsByName(String name);
}
