package com.khuthon.voidteam.controller;

import com.khuthon.voidteam.domain.Comment;
import com.khuthon.voidteam.dto.CommentRequestDto;
import com.khuthon.voidteam.dto.CommentResponseDto;
import com.khuthon.voidteam.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/board/{boardId}/comment", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<CommentResponseDto.CreateCommentDto> createComment(Principal principal, @PathVariable(name = "boardId")Long boardId,
                                                                             @RequestPart(value = "data") CommentRequestDto.CreateCommentDto request,
                                                                             @RequestPart(value = "mediaList", required= false) List<MultipartFile> mediaList) throws Exception {
        Comment comment = commentService.create(mediaList, boardId, request, principal);
        return ResponseEntity.ok(CommentResponseDto.CreateCommentDto.builder().commentId(comment.getId()).build());
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/board/{boardId}/comment/{commentId}")
    public ResponseEntity<CommentResponseDto.CreateCommentDto> updateComment(@PathVariable(name = "boardId")Long boardId, @PathVariable(name = "commentId")Long commentId, @RequestBody CommentRequestDto.CreateCommentDto request){
        Comment comment = commentService.update(boardId, commentId, request);
        return ResponseEntity.ok(CommentResponseDto.CreateCommentDto.builder().commentId(comment.getId()).build());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/board/{boardId}/comment")
    public ResponseEntity<List<CommentResponseDto.CommentDto>> getComment(@PathVariable(name = "boardId") Long boardId, Principal principal){
        return ResponseEntity.ok(commentService.getCommentList(boardId, principal));
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/board/{boardId}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable(name = "boardId")Long boardId, @PathVariable(name = "commentId")Long commentId){
        commentService.delete(boardId,commentId);
        return ResponseEntity.ok().build();
    }



}
