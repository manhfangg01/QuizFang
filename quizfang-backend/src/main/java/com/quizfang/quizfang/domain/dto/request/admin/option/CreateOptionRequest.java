package com.quizfang.quizfang.domain.dto.request.admin.option;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateOptionRequest {
    @NotNull(message = "Order index cannot be null")
    @Min(value = 1, message = "Order index must be >= 1")
    private Integer orderIndex;

    @NotBlank(message = "Content cannot be blank")
    @Size(max = 500, message = "Content must not exceed 500 characters")
    private String content;

    @NotNull(message = "isCorrect must not be null")
    private Boolean isCorrect;

    private String audio; // optional
    private String picture; // optional

    @NotNull(message = "Question ID is required")
    private Long questionId;
}
