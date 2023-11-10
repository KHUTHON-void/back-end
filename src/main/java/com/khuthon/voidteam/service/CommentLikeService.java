package com.khuthon.voidteam.service;


import com.amazonaws.services.kms.model.NotFoundException;
import com.khuthon.voidteam.domain.Board;
import com.khuthon.voidteam.domain.Comment;
import com.khuthon.voidteam.domain.CommentLike;
import com.khuthon.voidteam.exception.DuplicateInsertionException;
import com.khuthon.voidteam.repository.BoardRepository;
import com.khuthon.voidteam.repository.CommentLikeRepository;
import com.khuthon.voidteam.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class CommentLikeService {

    private final CommentRepository commentRepository;
    private final CommentLikeRepository commentLikeRepository;
    private final BoardRepository boardRepository;
    //private final MemberRepository memberRepository;

    @Transactional
    public void commentLike(Long boardId, Long commentId, Principal principal){
//        Member member = memberRepository.findByEmail(principal.getName())
//                .orElseThrow(() -> new NotFoundException("유저를 찾을 수 없습니다."));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("Board를 찾을 수 없습니다."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment를 찾을 수 없습니다"));
        if(commentLikeRepository.existsCommmentLikeByMemberAndBoardAndComment(member, board, comment)){
            throw new DuplicateInsertionException("이미 좋아요가 존재합니다.");
        }

        CommentLike commentLike = CommentLike.builder()
                //.member(member)
                .comment(comment)
                .build();

        commentLikeRepository.save(commentLike);
        comment.increaseLike();

    }

    @Transactional
    public void commentUnlike(Long boardId, Long commentId, Principal principal){
//        Member member = memberRepository.findByEmail(principal.getName())
//                .orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다"));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundException("Board를 찾을 수 없습니다"));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment를 찾을 수 없습니다"));
//        CommentLike commentLike = commentLikeRepository.findByMemberAndBoardAndComment(member, board, comment)
//                .orElseThrow(() -> new NotFoundException("좋아요를 찾을 수 없습니다"));

//        commentLikeRepository.delete(commentLike);
        comment.decreaseLike();

    }

}
