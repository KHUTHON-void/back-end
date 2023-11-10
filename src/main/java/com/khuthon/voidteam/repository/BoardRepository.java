package com.khuthon.voidteam.repository;

import com.khuthon.voidteam.domain.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Boolean existsByMember(Member member);

}
