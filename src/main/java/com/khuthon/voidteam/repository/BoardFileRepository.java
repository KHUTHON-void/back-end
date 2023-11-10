package com.khuthon.voidteam.repository;

import com.khuthon.voidteam.domain.Board;
import com.khuthon.voidteam.domain.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository extends JpaRepository<BoardFile, Long> {

    List<BoardFile> findAllByBoard(Board board);

}
