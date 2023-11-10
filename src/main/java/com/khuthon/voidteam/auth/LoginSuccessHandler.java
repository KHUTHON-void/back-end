package com.khuthon.voidteam.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.khuthon.voidteam.domain.Member;
import com.khuthon.voidteam.dto.AuthenticationResponse;
import com.khuthon.voidteam.repository.MemberRepository;
import com.khuthon.voidteam.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String email = extractUsername(authentication);
        GrantedAuthority roles = authentication.getAuthorities().stream().toList().get(0);
        log.info("사용자의 권한 : " + roles.getAuthority());
        String jwtToken = jwtService.generateAccessToken(email, roles.getAuthority());


        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.setHeader("Authorization", jwtToken);
        Member member = memberRepository.findByEmail(email).get();
        ObjectMapper objectMapper = new ObjectMapper();
        AuthenticationResponse authenticationResponse = AuthenticationResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .grade(member.getGrade())
                .profileImgURL(member.getProfileImgURL())
                .university(member.getUniversity())
                .temperature(member.getTemperature())
                .nickname(member.getNickname()).build();
        response.getWriter().write(objectMapper.writeValueAsString(authenticationResponse));


    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
