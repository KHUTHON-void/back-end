package com.khuthon.voidteam.dto;

import lombok.*;

import java.time.LocalDateTime;

public class RecruitCommentResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateRecruitCommentDto{
        private Long commentId;

    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecruitCommentDto{
        private Long commentId;
        private String content;
        private Boolean isMyPost;
        private MemberResponseDto.MemberRecruitDto member;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;

    }
}
