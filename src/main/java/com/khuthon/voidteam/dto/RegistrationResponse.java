package com.khuthon.voidteam.dto;


import com.khuthon.voidteam.domain.Grade;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationResponse {
    Long memberId;
    String nickname;
    String profileImgURL;
    Grade grade;
    String university;
    String email;
}
