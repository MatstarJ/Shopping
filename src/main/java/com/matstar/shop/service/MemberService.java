package com.matstar.shop.service;

import com.matstar.shop.entity.Member;
import com.matstar.shop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {

        validateDuplicateMember(member);
        return memberRepository.save(member);

    }

    // 중복 검사
    private void validateDuplicateMember(Member member) {

        Member findMember = memberRepository.findByEmail(member.getEmail());

        if(findMember != null) {
            throw new IllegalStateException("이미 가입된 회원 입니다.");
        }

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        //로그인할 유저의 email을 파라미터로 받는다.
        Member member = memberRepository.findByEmail(email);

        if(member == null) {
            throw new UsernameNotFoundException(email);
        }

        //UserDetails를 구현하고 있는 User 객체를 반환
        // 객체 생성을 위해 생성자로 파라미터를 넘겨준다.
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();

    }
}
