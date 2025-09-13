package com.quizfang.quizfang.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.quizfang.quizfang.domain.entity.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {

}
