package com.quizfang.quizfang.domain.dto.response.admin.test;

import java.time.Instant;
import java.util.List;

import com.quizfang.quizfang.util.constant.TestDifficulty;
import com.quizfang.quizfang.util.constant.TestSkill;
import com.quizfang.quizfang.util.constant.TestType;

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
public class FetchTestResponse {
    private Long id;
    private String title;
    private String topic;
    private TestSkill skill;
    private Long timeLimit;
    private TestType type;
    private Instant publishedDate;
    private Long totalParticipants;
    private Boolean isActive;
    private TestDifficulty difficulty;
    private String audio;
    private String picture;

    // danh sách id câu hỏi
    private List<Long> questionIds;
}