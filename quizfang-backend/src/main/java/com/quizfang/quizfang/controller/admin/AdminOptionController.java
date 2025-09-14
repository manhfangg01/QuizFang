package com.quizfang.quizfang.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quizfang.quizfang.domain.dto.request.admin.option.CreateOptionRequest;
import com.quizfang.quizfang.domain.dto.request.admin.option.UpdateOptionRequest;
import com.quizfang.quizfang.domain.dto.response.admin.option.FetchOptionResponse;
import com.quizfang.quizfang.domain.dto.restResponse.ApiMessage;
import com.quizfang.quizfang.service.admin.AdminOptionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin/options")
public class AdminOptionController {
    private final AdminOptionService adminOptionService;

    public AdminOptionController(AdminOptionService adminOptionService) {
        this.adminOptionService = adminOptionService;
    }

    @GetMapping("/fetch")
    @ApiMessage("fetch all options")
    public ResponseEntity<List<FetchOptionResponse>> fetch() {
        return ResponseEntity.status(HttpStatus.OK).body(this.adminOptionService.handleFetchAllOptions());
    }

    @GetMapping("/fetch/{id}")
    @ApiMessage("fetch one option")
    public ResponseEntity<FetchOptionResponse> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.adminOptionService.handleFetchOption(id));
    }

    @PostMapping("/create")
    @ApiMessage("create an option")
    public ResponseEntity<FetchOptionResponse> create(
            @Valid @RequestPart("createOptionRequest") CreateOptionRequest newOption,
            @RequestPart(value = "optionAudio", required = false) MultipartFile optionAudio,
            @RequestPart(value = "optionPicture", required = false) MultipartFile optionPicture) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.adminOptionService.handleCreateOption(newOption, optionAudio, optionPicture));
    }

    @PostMapping("/update")
    @ApiMessage("update an option")
    public ResponseEntity<FetchOptionResponse> update(
            @Valid @RequestPart("updateOptionRequest") UpdateOptionRequest updatedOption,
            @RequestPart(value = "optionAudio", required = false) MultipartFile optionAudio,
            @RequestPart(value = "optionPicture", required = false) MultipartFile optionPicture) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.adminOptionService.handleUpdateOption(updatedOption, optionAudio, optionPicture));
    }

    @DeleteMapping("/delete/{id}")
    @ApiMessage("delete an option")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.adminOptionService.handleDeleteOption(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
