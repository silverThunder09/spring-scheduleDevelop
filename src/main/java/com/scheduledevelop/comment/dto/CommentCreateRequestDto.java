package com.scheduledevelop.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentCreateRequestDto {

    @NotBlank(message = "댓글 내용은 필수입니다.")
    @Size(max =100, message = "댓글 내용은 100자 이하여야 합니다.")
    private String content;
}
