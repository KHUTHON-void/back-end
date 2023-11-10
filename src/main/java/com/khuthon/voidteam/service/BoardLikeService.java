package com.khuthon.voidteam.service;

import com.amazonaws.services.kms.model.NotFoundException;
import com.khuthon.voidteam.domain.Board;
import com.khuthon.voidteam.domain.BoardLike;
import com.khuthon.voidteam.exception.DuplicateInsertionException;
import com.khuthon.voidteam.repository.BoardLikeRepository;
import com.khuthon.voidteam.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class BoardLikeService {

    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;
    //private final MemberRepository memberRepository;

    @Transactional
    public void boardLike(Long boardId, Principal principal){
//        Member member = memberRepository.findByEmail(principal.getName())
//                .orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("Board를 찾을 수 없습니다"));
//        if(boardLikeRepository.existsBoardLikeByMemberAndBoard(member, board)){
//            throw new DuplicateInsertionException("이미 좋아요가 존재합니다.");
//        }

        BoardLike boardLike = BoardLike.builder()
                //.member(member)
                .board(board)
                .build();

        boardLikeRepository.save(boardLike);
        board.increaseLike();

    }

    @Transactional
    public void boardUnlike(Long boardId, Principal principal){
//        Member member = memberRepository.findByEmail(principal.getName())
//                .orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다"));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("Board를 찾을 수 없습니다"));
//        BoardLike boardLike = boardLikeRepository.findByMemberAndBoard(member, board)
//                .orElseThrow(() -> new NotFoundException("좋아요를 찾을 수 없습니다"));
//
//        boardLikeRepository.delete(boardLike);
        board.decreaseLike();

    }

}
