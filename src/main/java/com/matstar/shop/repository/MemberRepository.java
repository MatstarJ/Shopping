package com.matstar.shop.repository;

import com.matstar.shop.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {

    //이메일 검사
    Member findByEmail(String email);


}

