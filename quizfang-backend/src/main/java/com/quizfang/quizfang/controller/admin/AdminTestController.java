package com.quizfang.quizfang.controller.admin;

import com.quizfang.quizfang.domain.dto.request.admin.test.CreateTestRequest;
import com.quizfang.quizfang.domain.dto.request.admin.test.UpdateTestRequest;
import com.quizfang.quizfang.domain.dto.response.admin.test.FetchTestResponse;
import com.quizfang.quizfang.domain.dto.restResponse.ApiMessage;
import com.quizfang.quizfang.service.admin.AdminTestService;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tests")
public class AdminTestController {

    private final AdminTestService adminTestService;

    public AdminTestController(AdminTestService adminTestService) {
        this.adminTestService = adminTestService;
    }

    @GetMapping("/fetch")
    @ApiMessage("fetch all tests")
    public ResponseEntity<List<FetchTestResponse>> fetch() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.adminTestService.handleFetchAllTests());
    }

    @GetMapping("/fetch/{id}")
    @ApiMessage("fetch one test")
    public ResponseEntity<FetchTestResponse> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.adminTestService.handleFetchTest(id));
    }

    @PostMapping("/create")
    @ApiMessage("create a test")
    public ResponseEntity<FetchTestResponse> create(
            @Valid @RequestPart("createTestRequest") CreateTestRequest newTest,
            @RequestPart(value = "testAudio", required = false) MultipartFile testAudio,
            @RequestPart(value = "testPicture", required = false) MultipartFile testPicture) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.adminTestService.handleCreateTest(newTest, testAudio, testPicture));
    }

    @PostMapping("/update")
    @ApiMessage("update a test")
    public ResponseEntity<FetchTestResponse> update(
            @Valid @RequestPart("updateTestRequest") UpdateTestRequest updatedTest,
            @RequestPart(value = "testAudio", required = false) MultipartFile testAudio,
            @RequestPart(value = "testPicture", required = false) MultipartFile testPicture) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.adminTestService.handleUpdateTest(updatedTest, testAudio, testPicture));
    }

    @DeleteMapping("/delete/{id}")
    @ApiMessage("delete a test")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.adminTestService.handleDeleteTest(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }
}
