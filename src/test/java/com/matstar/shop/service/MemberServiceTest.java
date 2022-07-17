package com.matstar.shop.service;

import com.matstar.shop.dto.MemberFormDto;
import com.matstar.shop.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
@TestPropertySource(locations="classpath:application-test.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();

        memberFormDto.setEmail("test@email.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("서울시 마포구");
        memberFormDto.setPassword("1234");

        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() {

        Member member= createMember();
        Member savedMember = memberService.saveMember(member);

        //저장하려고 요청한 값과 실제 저장된 데이터 비교
        // 첫번째 파라미터로 기대값, 두번째로 실제저장된값
        assertEquals(member.getName(),savedMember.getName());
        assertEquals(member.getEmail(),savedMember.getEmail());
        assertEquals(member.getAddress(),savedMember.getAddress());
        assertEquals(member.getPassword(),savedMember.getPassword());
        assertEquals(member.getRole(),savedMember.getRole());
    }


    @Test
    @DisplayName("중복가입 테스트")
    public void saveDuplicateMemberTest() {

        Member member1 = createMember();
        Member member2 = createMember();

        memberService.saveMember(member1);

        //첫 번째 파라미터로 발생할 예외 타입을 넣는다.
        Throwable e = assertThrows(IllegalStateException.class,() -> {
            memberService.saveMember(member2);
        });

        assertEquals("이미 가입된 회원 입니다.",e.getMessage());

    }

}
