package com.khuthon.voidteam.dto;

import com.khuthon.voidteam.domain.Category;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class BoardResponseDto {

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BoardDto{
        private Long boardId;
        private Boolean isLiked;
        private MemberResponseDto.MemberDto member;
        private String title;
        private String content;
        private Category category;

        private List<String> mediaList;

        private LocalDateTime createdDate;
        private LocalDateTime modifiedDate;
        private int viewCount;
        private int likeCount;
        private int commentCount;

        private Boolean isMyPost;
    }

    @Builder
    @Getter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateBoardDto {
        private Long boardId;
    }
}
