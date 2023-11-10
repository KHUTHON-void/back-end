package com.khuthon.voidteam.dto;

import com.khuthon.voidteam.domain.Category;
import lombok.*;

import java.time.LocalDateTime;

public class RecruitResponseDto {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class RecruitDto{
        private Long recruitId;
        private MemberResponseDto.MemberRecruitDto member;
        private String title;
        private String content;
        private Category category;

        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
        private int viewCount;
        private int commentCount;

        private Boolean isMyPost;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateRecruitDto {
        private Long recruitId;
    }
}
