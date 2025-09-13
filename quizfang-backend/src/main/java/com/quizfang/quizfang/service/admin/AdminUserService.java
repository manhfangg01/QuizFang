package com.quizfang.quizfang.service.admin;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quizfang.quizfang.domain.dto.request.admin.user.CreateUserRequest;
import com.quizfang.quizfang.domain.dto.request.admin.user.UpdateUserRequest;
import com.quizfang.quizfang.domain.dto.response.admin.user.FetchUserResponse;
import com.quizfang.quizfang.domain.entity.User;
import com.quizfang.quizfang.repository.RoleRepository;
import com.quizfang.quizfang.repository.UserRepository;
import com.quizfang.quizfang.service.azure.AzureBlobService;
import com.quizfang.quizfang.util.error.DuplicatedObjectException;
import com.quizfang.quizfang.util.error.InvalidUploadedFile;
import com.quizfang.quizfang.util.error.ObjectNotFoundException;

@Service
public class AdminUserService {
    private final UserRepository userRepository;
    private final AzureBlobService azureBlobService;
    private final RoleRepository roleRepository;

    public AdminUserService(UserRepository userRepository, AzureBlobService azureBlobService,
            RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.azureBlobService = azureBlobService;
        this.roleRepository = roleRepository;
    }

    public FetchUserResponse convertToDto(User user) {

        return FetchUserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .createdBy(user.getCreatedBy())
                .updatedBy(user.getUpdatedBy())
                .role(user.getRole() != null ? user.getRole().getName() : "NULL")
                .avatar(user.getAvatar())
                .build();
    }

    public List<FetchUserResponse> handleFetchAllUsers() {
        List<User> dbUsers = this.userRepository.findAll();
        List<FetchUserResponse> allUserDto = dbUsers.stream().map(this::convertToDto).collect(Collectors.toList());
        return allUserDto;
    }

    public FetchUserResponse handleFetchUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return FetchUserResponse.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .avatar(user.getAvatar())
                .about(user.getAbout())
                .role(user.getRole() != null ? user.getRole().getName() : "NULL")
                .build();
    }

    public void handleAssginingUserAvatar(User user, MultipartFile userAvatar) {
        try {
            if (userAvatar != null && !userAvatar.isEmpty()) {
                String fileName = userAvatar.getOriginalFilename();
                List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx", "webp");
                boolean isValid = allowedExtensions.stream().anyMatch(item -> fileName.toLowerCase().endsWith(item));
                if (!isValid) {
                    throw new InvalidUploadedFile(
                            "Invalid file extension. only allows: " + allowedExtensions.toString());
                }
                String imageUrl = azureBlobService.uploadFile(userAvatar);
                user.setAvatar(imageUrl);
            } else {
                user.setAvatar("null");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload avatar", e);
        }

    }

    public FetchUserResponse handleCreateUser(CreateUserRequest newUser, MultipartFile userAvatar) {
        if (this.userRepository.findByEmail(newUser.getEmail()).isPresent()) {
            throw new DuplicatedObjectException("Email: " + newUser.getEmail() + " is duplicated");
        }
        if (this.userRepository.findByFullName(newUser.getFullName()).isPresent()) {
            throw new DuplicatedObjectException("Duplicated UserName");
        }
        User createdUser = User.builder()
                .about(newUser.getAbout())
                .fullName(newUser.getFullName())
                .email(newUser.getEmail())
                .password(newUser.getPassword())
                .createdAt(Instant.now())
                .createdBy("HARDCODE")
                .role(this.roleRepository.findByName(newUser.getRole()))
                .build();
        handleAssginingUserAvatar(createdUser, userAvatar);
        return this.convertToDto(this.userRepository.save(createdUser));
    }

    public FetchUserResponse handleUpdateUser(UpdateUserRequest updatedUser, MultipartFile userAvatar) {
        Optional<User> checkUser = this.userRepository.findById(updatedUser.getId());
        if (checkUser.isEmpty()) {
            throw new ObjectNotFoundException("There is no user has id: " + updatedUser.getId());
        }
        User realUser = checkUser.get();
        if ((realUser.getFullName() != null)) {
            if (!realUser.getFullName().equalsIgnoreCase(updatedUser.getFullName())) {
                if (this.userRepository.findByFullName(updatedUser.getFullName()).isPresent()) {
                    throw new DuplicatedObjectException("Duplicated UserName");
                }
            }
        }

        realUser.setFullName(updatedUser.getFullName());
        realUser.setRole(this.roleRepository.findByName(updatedUser.getRole()));
        realUser.setUpdatedAt(Instant.now());
        realUser.setUpdatedBy("HARDCODE");
        realUser.setAbout(updatedUser.getAbout());
        handleAssginingUserAvatar(realUser, userAvatar);
        return this.convertToDto(realUser);
    }

    public void handleDeleteUser(Long id) {
        Optional<User> checkUser = this.userRepository.findById(id);
        if (checkUser.isEmpty()) {
            throw new ObjectNotFoundException("There is no user has id: " + id);
        }
        User realUser = checkUser.get();
        if (realUser.getAvatar() != null && !realUser.getAvatar().isEmpty()) {
            String blobName = azureBlobService.getBlobNameFromUrl(realUser.getAvatar());
            if (blobName != null) {
                azureBlobService.deleteFile(blobName);
            }
        }
        this.userRepository.delete(realUser);
    }
}
