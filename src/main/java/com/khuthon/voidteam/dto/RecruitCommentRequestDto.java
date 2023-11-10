package com.khuthon.voidteam.dto;

import lombok.*;

public class RecruitCommentRequestDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateRecruitCommentDto{
        private String content;
    }
}
