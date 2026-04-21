package com.scheduledevelop.comment.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.scheduledevelop.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({ "id", "scheduleId", "userId", "username", "content", "createdAt", "updatedAt" })
public class CommentResponseDto {

    private final Long id;
    private final Long scheduleId;
    private final Long userId;
    private final String username;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.scheduleId = comment.getSchedule().getId();
        this.userId = comment.getUser().getId();
        this.username = comment.getUser().getUsername();
        this.content = comment.getContent();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}
