package com.scheduledevelop.schedule.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleCreateRequestDto {

    @NotBlank(message = "할일 제목은 필수입니다.")
    @Size(max = 10, message = "할일 제목은 10자 이하여야 합니다.")
    private String title;

    @NotBlank(message = "할일 내용은 필수입니다.")
    @Size(max = 200, message = "할일 내용은 200자 이하여야 합니다.")
    private String content;
}
