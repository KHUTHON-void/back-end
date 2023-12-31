package com.khuthon.voidteam.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class CommentResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateCommentDto{
        private Long commentId;

    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CommentDto{
        private Long commentId;
        private String content;
        private Boolean isLiked;
        private Boolean isMyPost;
        private int likecount;
        private MemberResponseDto.MemberDto member;
        private String mediaList;
        private LocalDateTime createdDate;
        private LocalDateTime updatedDate;

    }
}
