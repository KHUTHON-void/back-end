package com.khuthon.voidteam.controller;


import com.khuthon.voidteam.service.BoardLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BoardLikeController {

    private final BoardLikeService boardLikeService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{boardId}/like")
    public ResponseEntity<Void> createLike(Principal principal, @PathVariable(name = "boardId")Long boardId){
        boardLikeService.boardLike(boardId, principal);
        return ResponseEntity.ok().build();
    }


    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{boardId}/like")
    public ResponseEntity<Void> deleteLike(Principal principal, @PathVariable(name = "boardId")Long boardId){
        boardLikeService.boardUnlike(boardId, principal);
        return ResponseEntity.ok().build();
    }

}
