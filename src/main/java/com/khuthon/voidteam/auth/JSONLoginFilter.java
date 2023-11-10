package com.khuthon.voidteam.auth;


import com.khuthon.voidteam.dto.UsernamePasswordRequest;
import com.khuthon.voidteam.util.ObjectToDtoUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Slf4j
public class JSONLoginFilter extends AbstractAuthenticationProcessingFilter {

    @Autowired
    private ObjectToDtoUtil objectToDtoUtil;
    private static final String CONTENT_TYPE = "application/json";

    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher("/api/login", "POST");


    public JSONLoginFilter() {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if(request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)  ) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
        }



        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);
        log.info(messageBody.toString());

        UsernamePasswordRequest usernamePasswordMap =  (UsernamePasswordRequest) objectToDtoUtil.StrToObj(messageBody, UsernamePasswordRequest.class);


        String email = usernamePasswordMap.getEmail();
        String password = usernamePasswordMap.getPassword();


        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);//principal 과 credentials 전달

        return this.getAuthenticationManager().authenticate(authenticationToken);
    }
}
