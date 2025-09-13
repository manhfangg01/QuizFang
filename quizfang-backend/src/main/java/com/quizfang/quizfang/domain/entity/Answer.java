package com.quizfang.quizfang.domain.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "answers")
@Getter
@Setter
@Builder
public class Answer {
    // Properties
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Boolean isCorrect;
    private String content;
    private String audio;

    // Relationship
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne
    @JoinColumn(name = "result_id")
    private Result result;

    /*
     * cascade là một thuộc tính của quan hệ (@OneToMany, @ManyToOne, @ManyToMany,
     * …).
     * Nó cho Hibernate biết rằng: khi ta thực hiện một hành động với entity cha,
     * thì hành động đó có được “truyền xuống” các entity con hay không.
     * 
     * Các loại Cascade
     * 
     * CascadeType.PERSIST
     * Khi lưu entity cha (save), các entity con cũng sẽ được lưu tự động nếu chưa
     * có trong DB.
     * 
     * CascadeType.MERGE
     * Khi cập nhật entity cha (merge), các entity con cũng sẽ được merge.
     * 
     * CascadeType.REMOVE
     * Khi xóa entity cha, các entity con cũng sẽ bị xóa theo.
     * 
     * CascadeType.REFRESH
     * Khi refresh cha từ DB, con cũng được refresh.
     * 
     * CascadeType.DETACH
     * Khi detach cha khỏi EntityManager, con cũng bị detach.
     * 
     * CascadeType.ALL
     * Bao gồm tất cả các loại ở trên.
     */

}