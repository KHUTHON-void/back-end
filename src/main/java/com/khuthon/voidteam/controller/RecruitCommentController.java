package com.khuthon.voidteam.controller;

import com.khuthon.voidteam.domain.RecruitComment;
import com.khuthon.voidteam.dto.RecruitCommentRequestDto;
import com.khuthon.voidteam.dto.RecruitCommentResponseDto;
import com.khuthon.voidteam.service.RecruitCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RecruitCommentController {

    private final RecruitCommentService recruitCommentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value = "/recruit/{recruitId}/comment")
    public ResponseEntity<RecruitCommentResponseDto.CreateRecruitCommentDto> createComment(Principal principal, @PathVariable(name = "recruitId")Long recruitId,
                                                                                                       @RequestBody RecruitCommentRequestDto.CreateRecruitCommentDto request) throws Exception {
        RecruitComment comment = recruitCommentService.create(recruitId, request, principal);
        return ResponseEntity.ok(RecruitCommentResponseDto.CreateRecruitCommentDto.builder().commentId(comment.getId()).build());
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/recruit/{recruitId}/comment/{commentId}")
    public ResponseEntity<RecruitCommentResponseDto.CreateRecruitCommentDto> updateComment(@PathVariable(name = "recruitId") Long recruitId, @PathVariable(name = "commentId")Long commentId, @RequestBody RecruitCommentRequestDto.CreateRecruitCommentDto request){
        RecruitComment comment = recruitCommentService.update(recruitId, commentId, request);
        return ResponseEntity.ok(RecruitCommentResponseDto.CreateRecruitCommentDto.builder().commentId(comment.getId()).build());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/recruit/{recruitId}/comment")
    public ResponseEntity<List<RecruitCommentResponseDto.RecruitCommentDto>> getComment(@PathVariable(name = "recruitId") Long recruitId, Principal principal){
        return ResponseEntity.ok(recruitCommentService.getCommentList(recruitId, principal));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/recruit/{recruitId}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable(name = "recruitId")Long recruitId, @PathVariable(name = "commentId")Long commentId){
        recruitCommentService.delete(recruitId,commentId);
        return ResponseEntity.ok().build();
    }
}
