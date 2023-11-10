package com.khuthon.voidteam.repository;

import com.khuthon.voidteam.domain.Comment;
import com.khuthon.voidteam.domain.CommentFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentFileRepository extends JpaRepository<CommentFile, Long> {

    List<CommentFile> findAllByComment(Comment comment);
}
