package com.khuthon.voidteam.dto;

import com.khuthon.voidteam.domain.Category;
import lombok.*;

public class RecruitRequestDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateRecruitDto{
        private String title;
        private String content;
        private Category category;
    }
}
