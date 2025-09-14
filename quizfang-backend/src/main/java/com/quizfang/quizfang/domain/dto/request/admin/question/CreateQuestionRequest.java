package com.quizfang.quizfang.domain.dto.request.admin.question;

import java.util.List;

import com.quizfang.quizfang.domain.dto.request.admin.option.CreateOptionRequest;
import com.quizfang.quizfang.domain.dto.response.admin.option.FetchOptionResponse;
import com.quizfang.quizfang.util.constant.QuestionSubtype;
import com.quizfang.quizfang.util.constant.QuestionType;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateQuestionRequest {
    @NotBlank(message = "Content is required")
    @Size(min = 5, max = 5000, message = "Content must be between 5 and 5000 characters")
    private String content;

    @NotNull(message = "Question type is required")
    private QuestionType type;

    @NotNull(message = "Question subtype is required")
    private QuestionSubtype subType;

    @PositiveOrZero(message = "Order index must be >= 0")
    private Integer orderIndex;

    @Positive(message = "Time limit must be positive")
    private Long timeLimit;

    // Optional fields
    private String picture;
    private String audio;

    // Nếu muốn tạo question kèm option
    @Valid
    @NotEmpty(message = "Options must not be empty")
    @Size(min = 1, max = 4, message = "Options list must contain at least 1 and not more than 4")
    private List<Long> optionIds;
}
