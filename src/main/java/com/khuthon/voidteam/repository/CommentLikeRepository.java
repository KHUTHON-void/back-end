package com.khuthon.voidteam.repository;

import com.khuthon.voidteam.domain.Board;
import com.khuthon.voidteam.domain.Comment;
import com.khuthon.voidteam.domain.CommentLike;
import com.khuthon.voidteam.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {

    Boolean existsCommentLikeByMemberAndBoardAndComment(Member member, Board board, Comment comment);

    Optional<CommentLike> findByMemberAndBoardAndComment(Member member, Board board, Comment comment);
}
