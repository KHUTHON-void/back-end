package com.khuthon.voidteam.controller;


import com.khuthon.voidteam.dto.AuthenticationResponse;
import com.khuthon.voidteam.dto.RegistrationRequest;
import com.khuthon.voidteam.service.MemberService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthenticationController {

    private final MemberService memberService;

    @PostMapping(value = "/sign-up")
    public ResponseEntity<?> signUp(
            @AuthenticationPrincipal Principal principal,
            @RequestPart RegistrationRequest signUpRequest,
            @RequestPart(required = false) MultipartFile profileImg,
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws Exception {
        return memberService.signUp(signUpRequest, profileImg, httpServletRequest, httpServletResponse);
    }

    @GetMapping("/validate-jwt")
    public ResponseEntity<AuthenticationResponse> validateJwt(HttpServletRequest httpServletRequest) throws ServletException, IOException {
        return ResponseEntity.ok(memberService.validateJwt(httpServletRequest));
    }

}
