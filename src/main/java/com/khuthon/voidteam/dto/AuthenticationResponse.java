package com.khuthon.voidteam.dto;

import com.khuthon.voidteam.domain.Grade;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    Long memberId;
    String nickname;
    String profileImgURL;
    Grade grade;
    String university;
    String email;
}

