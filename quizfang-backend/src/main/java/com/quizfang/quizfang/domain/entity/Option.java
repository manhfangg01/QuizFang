package com.quizfang.quizfang.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "options")
@Builder
public class Option {
    // Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer orderIndex;
    private String content;
    private Boolean isCorrect;
    // media
    private String audio;
    private String picture;

    // Relationship
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

}