package com.khuthon.voidteam.dto;

import com.khuthon.voidteam.domain.Grade;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class MemberResponseDto {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberDto{
        private Long memberId;
        private String nickname;
        private String profileImgUrl;

    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class MemberRecruitDto{
        private Long memberId;
        private String nickname;
        private String profileImgUrl;
        private String university;
        private Double temperature;
        private Grade grade;

    }
}
