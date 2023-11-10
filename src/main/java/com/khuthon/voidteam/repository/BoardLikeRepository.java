package com.khuthon.voidteam.repository;

import com.khuthon.voidteam.domain.Board;
import com.khuthon.voidteam.domain.BoardLike;
import com.khuthon.voidteam.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    Boolean existsBoardLikeByMemberAndBoard(Member member, Board board);

    Optional<BoardLike> findByMemberAndBoard(Member member, Board board);


}
