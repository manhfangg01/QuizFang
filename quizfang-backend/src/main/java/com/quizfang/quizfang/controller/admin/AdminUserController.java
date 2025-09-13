package com.quizfang.quizfang.controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.quizfang.quizfang.domain.dto.request.admin.user.CreateUserRequest;
import com.quizfang.quizfang.domain.dto.request.admin.user.UpdateUserRequest;
import com.quizfang.quizfang.domain.dto.response.admin.user.FetchUserResponse;
import com.quizfang.quizfang.domain.dto.restResponse.ApiMessage;
import com.quizfang.quizfang.service.admin.AdminUserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {
    private final AdminUserService adminUserService;

    public AdminUserController(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @GetMapping("/fetch")
    @ApiMessage("fetch all users")
    public ResponseEntity<List<FetchUserResponse>> fetch() {
        return ResponseEntity.status(HttpStatus.OK).body(this.adminUserService.handleFetchAllUsers());
    }

    @GetMapping("/fetch/{id}")
    @ApiMessage("fetch one user")
    public ResponseEntity<FetchUserResponse> fetchOne(@PathVariable("id") Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.adminUserService.handleFetchUser(id));
    }

    @PostMapping("/create")
    @ApiMessage("create a user")
    public ResponseEntity<FetchUserResponse> create(
            @Valid @RequestPart("createUserRequest") CreateUserRequest newUser,
            @RequestPart(value = "userAvatar", required = false) MultipartFile userAvatar) {
        return ResponseEntity.status(HttpStatus.OK).body(this.adminUserService.handleCreateUser(newUser, userAvatar));
    }

    @PostMapping("/update")
    public ResponseEntity<FetchUserResponse> update(
            @RequestPart("updateUserRequest") @Valid UpdateUserRequest updatedUser,
            @RequestPart(value = "userAvatar", required = false) MultipartFile userAvatar) {
        return ResponseEntity.status(HttpStatus.OK).body(
                this.adminUserService.handleUpdateUser(updatedUser, userAvatar));
    }

    @DeleteMapping("/delete/{id}")
    @ApiMessage("delete a user")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        this.adminUserService.handleDeleteUser(id);
        return ResponseEntity.status(HttpStatus.OK).body(null);
    }

}
