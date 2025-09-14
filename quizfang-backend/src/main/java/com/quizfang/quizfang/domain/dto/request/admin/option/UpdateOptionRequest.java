package com.quizfang.quizfang.domain.dto.request.admin.option;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateOptionRequest {

    @NotNull(message = "ID must not be blank")
    private Long id;
    @Min(value = 1, message = "Order index must be >= 1")
    private Integer orderIndex;

    @Size(max = 500, message = "Content must not exceed 500 characters")
    private String content;

    private Boolean isCorrect;
    private String audio;
}
