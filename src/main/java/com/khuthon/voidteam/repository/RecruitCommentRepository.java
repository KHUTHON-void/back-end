package com.khuthon.voidteam.repository;

import com.khuthon.voidteam.domain.Recruit;
import com.khuthon.voidteam.domain.RecruitComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecruitCommentRepository extends JpaRepository<RecruitComment, Long> {

    RecruitComment findByIdAndRecruit(Long commentId, Recruit recruit);

    List<RecruitComment> findAllByRecruit(Recruit recruit);

}
