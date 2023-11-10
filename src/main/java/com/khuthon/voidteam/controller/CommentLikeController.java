package com.khuthon.voidteam.controller;

import com.khuthon.voidteam.service.CommentLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{boardId}/{commentId}/like")
    public ResponseEntity<Void> createLike(Principal principal, @PathVariable(name = "boardId") Long boardId,
                                           @PathVariable(name = "commentId") Long commentId){
        commentLikeService.commentLike(boardId, commentId, principal);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{boardId}/{commentId}/like")
    public ResponseEntity<Void> deleteLike(Principal principal, @PathVariable(name = "boardId") Long boardId,
                                           @PathVariable(name = "commentId") Long commentId){
        commentLikeService.commentUnlike(boardId, commentId, principal);
        return ResponseEntity.ok().build();
    }

}
