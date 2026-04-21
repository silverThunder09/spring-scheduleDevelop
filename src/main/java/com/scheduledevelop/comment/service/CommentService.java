package com.scheduledevelop.comment.service;

import com.scheduledevelop.comment.dto.CommentCreateRequestDto;
import com.scheduledevelop.comment.dto.CommentResponseDto;
import com.scheduledevelop.comment.entity.Comment;
import com.scheduledevelop.comment.repository.CommentRepository;
import com.scheduledevelop.common.exception.ApiException;
import com.scheduledevelop.schedule.entity.Schedule;
import com.scheduledevelop.schedule.repository.ScheduleRepository;
import com.scheduledevelop.user.entity.User;
import com.scheduledevelop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDto create(Long scheduleId,Long loginUserId,
                                     CommentCreateRequestDto request) {

        // 일정이 존재하는지 확인
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> ApiException.notFound("존재하지 않는 일정입니다."));

        // 로그인한 유저 확인
        User user = userRepository.findById(loginUserId)
                .orElseThrow(() -> ApiException.notFound("존재하지 않는 유저입니다."));

        Comment comment = new Comment(schedule, user, request.getContent());
        Comment saved = commentRepository.save(comment);

        return new CommentResponseDto(saved);

    }

    @Transactional(readOnly = true)
    public List<CommentResponseDto> getAll(Long scheduleId) {

        // 일정이 존재하는지 확인
        if(!scheduleRepository.existsById(scheduleId)) {
            throw ApiException.notFound("존재하지 않는 일정입니다.");
        }

        return commentRepository.findByScheduleIdOrderByCreatedAtAsc(scheduleId).stream()
                .map(CommentResponseDto::new)
                .toList();
    }
}
