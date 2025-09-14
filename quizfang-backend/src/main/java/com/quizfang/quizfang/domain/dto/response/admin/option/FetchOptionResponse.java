package com.quizfang.quizfang.domain.dto.response.admin.option;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FetchOptionResponse {
    private Long id;
    private Integer orderIndex;
    private String content;
    private Boolean isCorrect;
    private String audio;
    private String picture;
    private Long questionId;
}
