package com.quizfang.quizfang.domain.entity;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.quizfang.quizfang.util.constant.TestDifficulty;
import com.quizfang.quizfang.util.constant.TestSkill;
import com.quizfang.quizfang.util.constant.TestType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tests")
@Getter
@Setter
@Builder
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String title;
    private String topic;
    @Enumerated(EnumType.STRING)
    private TestSkill skill;
    private Long timeLimit;
    @Enumerated(EnumType.STRING)
    private TestType type;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
    private Instant publishedDate;
    private Long totalParticipants;
    private Boolean isActive;
    @Enumerated(EnumType.STRING)
    private TestDifficulty difficulty;

    // Media
    private String audio;

    // relationship
    @ManyToMany
    @JoinTable(name = "test_question", joinColumns = @JoinColumn(name = "test_id"), inverseJoinColumns = @JoinColumn(name = "ques_id"))
    private List<Question> questions; // Bên test là owner do test quản lý nhiều câu hỏi
                                      // Còn câu hỏi muốn biết mình thuộc tets nào thì phải tham chiếu lại

    @OneToMany(mappedBy = "test")
    private List<Result> results;
}
