package com.khuthon.voidteam.repository;

import com.khuthon.voidteam.domain.Board;
import com.khuthon.voidteam.domain.Comment;
import com.khuthon.voidteam.domain.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Boolean existsCommmentLikeByMemberAndBoardAndComment(Member member, Board board, Comment comment);

    Comment findByMemberAndBoardAndComment(Member member, Board board, Comment comment);
}
