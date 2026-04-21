package com.scheduledevelop.comment.repository;


import com.scheduledevelop.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByScheduleIdOrderByCreatedAtAsc(Long scheduleId);

    void deleteAllByScheduleId(Long scheduleId);
}
