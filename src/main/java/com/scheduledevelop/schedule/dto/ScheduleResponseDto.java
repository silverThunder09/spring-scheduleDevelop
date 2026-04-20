package com.scheduledevelop.schedule.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.scheduledevelop.schedule.entity.Schedule;
import com.scheduledevelop.user.entity.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonPropertyOrder({ "id", "userId", "username", "title", "content", "createdAt", "updatedAt" })
// @JsonPropertyOrder을 사용하면 개발자가 의도한 대로 순서를 고정할 수 있다
public class ScheduleResponseDto {

    private final Long id;
    private final Long userId;
    private final String username;
    private final String title;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.userId = schedule.getUser().getId();
        this.username = schedule.getUser().getUsername();
        this.title = schedule.getTitle();
        this.content = schedule.getContent();
        this.createdAt = schedule.getCreatedAt();
        this.updatedAt = schedule.getUpdatedAt();
    }
}
