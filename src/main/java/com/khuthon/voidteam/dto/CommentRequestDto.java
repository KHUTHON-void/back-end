package com.khuthon.voidteam.dto;

import lombok.*;

import java.util.List;

public class CommentRequestDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateCommentDto{
        private String content;
    }

}
