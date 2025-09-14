package com.quizfang.quizfang.domain.dto.request.admin.test;

import java.time.Instant;
import java.util.List;

import com.quizfang.quizfang.util.constant.TestDifficulty;
import com.quizfang.quizfang.util.constant.TestSkill;
import com.quizfang.quizfang.util.constant.TestType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTestRequest {

    @NotNull(message = "Test ID is required")
    private Long id;

    @NotBlank(message = "Title must not be blank")
    @Size(min = 3, max = 255, message = "Title must be between 3 and 255 characters")
    private String title;

    @NotBlank(message = "Topic must not be blank")
    private String topic;

    @NotNull(message = "Skill is required")
    private TestSkill skill;

    @NotNull(message = "Time limit is required")
    @Positive(message = "Time limit must be greater than 0")
    private Long timeLimit;

    @NotNull(message = "Test type is required")
    private TestType type;

    @NotNull(message = "Published date is required")
    private Instant publishedDate;

    @NotNull(message = "Difficulty is required")
    private TestDifficulty difficulty;

    @NotNull(message = "Active status is required")
    private Boolean isActive;

    // Media
    private String picture;
    private String audio;

    // Cập nhật lại câu hỏi trong bài test
    @NotEmpty(message = "Question list must not be empty")
    private List<Long> questionIds;
}