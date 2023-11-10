package com.khuthon.voidteam.service;

import com.khuthon.voidteam.domain.Member;
import com.khuthon.voidteam.domain.Role;
import com.khuthon.voidteam.dto.RegistrationRequest;
import com.khuthon.voidteam.dto.RegistrationResponse;
import com.khuthon.voidteam.repository.MemberRepository;
import com.khuthon.voidteam.util.S3Util;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final S3Util s3Util;

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;


    @Transactional
    public ResponseEntity<?> signUp(RegistrationRequest signUpRequest, MultipartFile profileImg, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {

        String profileImgURL = null;

        Member member = Member.builder()
                .email(signUpRequest.getEmail())
                .job(signUpRequest.getJob())
                .realname(signUpRequest.getRealName())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .role(signUpRequest.getUniversity() != null ? Role.UNIV : Role.USER)
                .age(signUpRequest.getAge())
                .university(signUpRequest.getUniversity())
                .nickname(signUpRequest.getNickname())
                .build();
        memberRepository.save(member);
        if (profileImg != null) {
            profileImgURL = s3Util.uploadProfileImgObjectToS3(member.getId(), profileImg);
        }
        member.setProfileImgURL(profileImgURL);

        HttpHeaders httpHeaders = new HttpHeaders();
        String accessToken = jwtService.generateAccessToken(member.getUsername(), member.getRole().getValue());
        httpHeaders.add("Authorization", accessToken);

        RegistrationResponse registrationResponse = RegistrationResponse.builder()
                .memberId(member.getId())
                .email(member.getEmail())
                .grade(member.getGrade())
                .nickname(member.getNickname())
                .profileImgURL(member.getProfileImgURL())
                .university(member.getUniversity())
                .build();

        return new ResponseEntity<RegistrationResponse>(registrationResponse, httpHeaders, HttpStatus.CREATED);
    }

}
