package com.khuthon.voidteam.service;

import com.khuthon.voidteam.domain.Member;
import com.khuthon.voidteam.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Member member = memberRepository.findByEmail(username).orElseThrow(()-> new UsernameNotFoundException("사용자 인증 실패"));
            log.info("사용자 이메일 정보: " + member.getUsername());
            return member;
        }catch (UsernameNotFoundException usernameNotFoundException){
            log.info("사용자 인증 실패");
            throw usernameNotFoundException;
        }
    }
}
