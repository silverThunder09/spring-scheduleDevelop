package com.scheduledevelop.schedule.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ScheduleCreateRequestDto {

    private Long userId;
    private String title;
    private String content;
}
