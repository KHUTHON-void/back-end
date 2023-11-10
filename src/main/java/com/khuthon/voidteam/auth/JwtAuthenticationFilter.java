package com.khuthon.voidteam.auth;


import com.khuthon.voidteam.service.JwtService;
import com.khuthon.voidteam.service.MemberDetailsService;
import com.khuthon.voidteam.service.RedisService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MemberDetailsService memberDetailsService;

    private final RedisService redisService;

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {
        //Login 요청이라면 필터체인 패스
        if (request.getRequestURI().equals("/login")) {
            filterChain.doFilter(request, response); // "/login" 요청이 들어오면, 다음 필터 호출
            return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
        }


        final Optional<String> accessToken = jwtService.extractAccessToken(request);

        log.info("[doFilterInternal] token 값 유효성 체크 시작" + " 토큰 : " + accessToken);
        if (accessToken.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null
                && jwtService.validateToken(accessToken.get()) && !redisService.hasKey(accessToken.get())) {
            String email = jwtService.extractUsername(accessToken.get());
            UserDetails userDetails = memberDetailsService.loadUserByUsername(email);
            Authentication authentication = jwtService.getAuthentication(userDetails); //Authentication 객체 생성

            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);

        }

        filterChain.doFilter(request, response);
    }
}
