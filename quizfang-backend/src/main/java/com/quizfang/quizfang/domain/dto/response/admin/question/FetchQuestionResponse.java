package com.quizfang.quizfang.domain.dto.response.admin.question;

import java.time.Instant;
import java.util.List;

import com.quizfang.quizfang.util.constant.QuestionSubtype;
import com.quizfang.quizfang.util.constant.QuestionType;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FetchQuestionResponse {
    private Long id;
    private String content;
    private QuestionType type;
    private QuestionSubtype subType;
    private Integer orderIndex;
    private Long timeLimit;
    private String picture;
    private String audio;
    private Instant createdDate;

    private List<Long> optionIds;
}
