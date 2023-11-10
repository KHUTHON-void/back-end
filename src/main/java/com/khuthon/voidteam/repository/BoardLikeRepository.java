package com.khuthon.voidteam.repository;

import com.khuthon.voidteam.domain.Board;
import com.khuthon.voidteam.domain.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {

    Boolean existsBoardLikeByMemberAndBoard(Member member, Board board);

    BoardLike findByMemberAndBoard(Member member, Board board);


}
