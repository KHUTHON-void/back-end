package com.khuthon.voidteam.repository;

import com.khuthon.voidteam.domain.Category;
import com.khuthon.voidteam.domain.Member;
import com.khuthon.voidteam.domain.Recruit;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RecruitRepository extends JpaRepository<Recruit, Long> {
    List<Recruit> findAllByCategoryOrderByCreateAtDesc(Category category);
    Boolean existsByMember(Member member);

    List<Recruit> findAllByCategoryOrderByViewCountDesc(Category category);
}
