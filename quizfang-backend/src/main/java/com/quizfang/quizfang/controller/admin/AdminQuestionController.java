package com.quizfang.quizfang.controller.admin;

import com.quizfang.quizfang.domain.dto.request.admin.question.CreateQuestionRequest;
import com.quizfang.quizfang.domain.dto.request.admin.question.UpdateQuestionRequest;
import com.quizfang.quizfang.domain.dto.response.admin.question.FetchQuestionResponse;
import com.quizfang.quizfang.domain.dto.restResponse.ApiMessage;
import com.quizfang.quizfang.service.admin.AdminQuestionService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/questions")
public class AdminQuestionController {

    private final AdminQuestionService adminQuestionService;

    public AdminQuestionController(AdminQuestionService adminQuestionService) {
        this.adminQuestionService = adminQuestionService;
    }

    @GetMapping("/fetch")
    @ApiMessage("fetch all questions")
    public ResponseEntity<List<FetchQuestionResponse>> fetch() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.adminQuestionService.handleFetchAllQuestions());
    }

    @GetMapping("/fetch/{id}")
    @ApiMessage("fetch one question")
    public ResponseEntity<FetchQuestionResponse> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.adminQuestionService.handleFetchQuestion(id));
    }

    @PostMapping("/create")
    @ApiMessage("create a question")
    public ResponseEntity<FetchQuestionResponse> create(
            @Valid @RequestPart("createQuestionRequest") CreateQuestionRequest newQuestion,
            @RequestPart(value = "questionAudio", required = false) MultipartFile questionAudio,
            @RequestPart(value = "questionPicture", required = false) MultipartFile questionPicture) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.adminQuestionService.handleCreateQuestion(newQuestion, questionAudio, questionPicture));
    }

    @PostMapping("/update")
    @ApiMessage("update a question")
    public ResponseEntity<FetchQuestionResponse> update(
            @Valid @RequestPart("updateQuestionRequest") UpdateQuestionRequest updatedQuestion,
            @RequestPart(value = "questionAudio", required = false) MultipartFile questionAudio,
            @RequestPart(value = "questionPicture", required = false) MultipartFile questionPicture) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.adminQuestionService.handleUpdateQuestion(updatedQuestion, questionAudio, questionPicture));
    }

    @DeleteMapping("/delete/{id}")
    @ApiMessage("delete a question")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.adminQuestionService.handleDeleteQuestion(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
