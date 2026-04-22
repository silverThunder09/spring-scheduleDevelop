package com.scheduledevelop.comment.controller;

import com.scheduledevelop.comment.dto.CommentCreateRequestDto;
import com.scheduledevelop.comment.dto.CommentResponseDto;
import com.scheduledevelop.comment.service.CommentService;
import com.scheduledevelop.common.Util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/schedules/{scheduleId}/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDto> createComment(
            @PathVariable Long scheduleId,
            @Valid @RequestBody CommentCreateRequestDto request,
            HttpSession session) {

        Long loginUserId = SessionUtil.getLoginUserId(session);

        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.create(scheduleId, loginUserId, request));

    }

    // 댓글 전체 조회
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> getAllComment(@PathVariable Long scheduleId) {
        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAll(scheduleId));
    }

}
