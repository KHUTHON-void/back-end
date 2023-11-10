package com.khuthon.voidteam.dto;

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
}
