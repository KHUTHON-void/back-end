package com.khuthon.voidteam.repository;

import com.khuthon.voidteam.domain.Board;
import com.khuthon.voidteam.domain.Category;
import com.khuthon.voidteam.domain.Member;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Boolean existsByMember(Member member);
    List<Board> findAllByCategory(Category interestType, Sort sort);

    List<Board> findAll(Sort sort);

}
