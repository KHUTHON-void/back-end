package com.khuthon.voidteam.dto;

import lombok.*;

public class BoardRequestDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateBoardDto{
        private String title;
        private String content;
    }



}
