package com.quizfang.quizfang.domain.entity;

import java.time.Instant;
import java.util.List;

import com.quizfang.quizfang.util.constant.QuestionSubtype;
import com.quizfang.quizfang.util.constant.QuestionType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "questions")
@Getter
@Setter
@Builder
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String content;
    private QuestionType type;
    private QuestionSubtype subType;
    private Instant createdDate;
    private Integer orderIndex;
    private Long timeLimit;

    // media
    private String picture;
    private String audio;

    // relationship
    @ManyToMany(mappedBy = "questions")
    private List<Test> tests;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Answer> answers;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    private List<Option> options;
}
