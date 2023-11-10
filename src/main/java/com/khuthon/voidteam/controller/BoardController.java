package com.khuthon.voidteam.controller;

import com.khuthon.voidteam.domain.Board;
import com.khuthon.voidteam.domain.BoardFile;
import com.khuthon.voidteam.dto.BoardRequestDto;
import com.khuthon.voidteam.dto.BoardResponseDto;
import com.khuthon.voidteam.service.BoardService;
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
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping(value ="", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<BoardResponseDto.CreateBoardDto> createBoard(@RequestPart(value = "mediaList", required= false) List<MultipartFile> mediaList,
                                                                           @RequestPart(value = "data")String data, Principal principal) throws Exception {

        Board board = boardService.create(mediaList, data, principal);
        return ResponseEntity.ok(BoardResponseDto.CreateBoardDto.builder().boardId(board.getId()).build());
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto.CreateBoardDto> updateBoard(@PathVariable(name = "boardId")Long boardId, @RequestBody BoardRequestDto.CreateBoardDto data){
        Board board = boardService.update(boardId, data);
        return ResponseEntity.ok(BoardResponseDto.CreateBoardDto.builder().boardId(board.getId()).build());
    }

    // 개별 조회
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponseDto.BoardDto> getBoard(@PathVariable(name = "boardId") Long boardId, Principal principal){
        return ResponseEntity.ok(boardService.findBoard(boardId, principal));
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{boardId}")
    public ResponseEntity<Void> deleteBoard(@PathVariable(name = "boardId")Long boardId){
        boardService.delete(boardId);
        return ResponseEntity.ok().build();
    }

}
