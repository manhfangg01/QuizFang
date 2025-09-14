package com.quizfang.quizfang.service.admin;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quizfang.quizfang.domain.dto.request.admin.test.CreateTestRequest;
import com.quizfang.quizfang.domain.dto.request.admin.test.UpdateTestRequest;
import com.quizfang.quizfang.domain.dto.response.admin.test.FetchTestResponse;
import com.quizfang.quizfang.domain.entity.Question;
import com.quizfang.quizfang.domain.entity.Test;
import com.quizfang.quizfang.repository.QuestionRepository;
import com.quizfang.quizfang.repository.TestRepository;
import com.quizfang.quizfang.service.azure.AzureBlobService;
import com.quizfang.quizfang.util.error.InvalidUploadedFile;
import com.quizfang.quizfang.util.error.ObjectNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class AdminTestService {

    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final AzureBlobService azureBlobService;

    public AdminTestService(TestRepository testRepository,
            QuestionRepository questionRepository,
            AzureBlobService azureBlobService) {
        this.testRepository = testRepository;
        this.questionRepository = questionRepository;
        this.azureBlobService = azureBlobService;
    }

    // Convert Entity -> DTO
    public FetchTestResponse convertToDto(Test test) {
        return FetchTestResponse.builder()
                .id(test.getId())
                .title(test.getTitle())
                .topic(test.getTopic())
                .skill(test.getSkill())
                .timeLimit(test.getTimeLimit())
                .type(test.getType())
                .publishedDate(test.getPublishedDate())
                .totalParticipants(test.getTotalParticipants())
                .isActive(test.getIsActive())
                .difficulty(test.getDifficulty())
                .picture(test.getPicture())
                .audio(test.getAudio())
                .questionIds(
                        test.getQuestions() != null
                                ? test.getQuestions().stream().map(Question::getId).toList()
                                : List.of())
                .build();
    }

    // Upload hình
    public void handleAssignTestPicture(Test test, MultipartFile picture) {
        try {
            if (picture != null && !picture.isEmpty()) {
                List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx", "webp");
                String fileName = picture.getOriginalFilename().toLowerCase();
                String contentType = picture.getContentType();
                boolean validMime = contentType != null && contentType.startsWith("image/");
                boolean validExt = allowedExtensions.stream().anyMatch(fileName::endsWith);
                if (!validMime || !validExt) {
                    throw new InvalidUploadedFile("Invalid file type. Only allows: " + allowedExtensions);
                }
                String imageUrl = azureBlobService.uploadFile(picture);
                test.setPicture(imageUrl);
            } else {
                test.setPicture(null);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload test picture", e);
        }
    }

    // Upload audio
    public void handleAssignTestAudio(Test test, MultipartFile audio) {
        try {
            if (audio != null && !audio.isEmpty()) {
                List<String> allowedExtensions = List.of(".mp3", ".wav", ".ogg", ".m4a");
                String fileName = audio.getOriginalFilename().toLowerCase();
                boolean validExt = allowedExtensions.stream().anyMatch(fileName::endsWith);
                if (!validExt) {
                    throw new InvalidUploadedFile("Invalid file type. Only allows: " + allowedExtensions);
                }
                String audioUrl = azureBlobService.uploadFile(audio);
                test.setAudio(audioUrl);
            } else {
                test.setAudio(null);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload test audio", e);
        }
    }

    // Fetch one
    public FetchTestResponse handleFetchTest(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Test not found with id: " + id));
        return this.convertToDto(test);
    }

    // Fetch all
    public List<FetchTestResponse> handleFetchAllTests() {
        return testRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Create
    @Transactional
    public FetchTestResponse handleCreateTest(CreateTestRequest request,
            MultipartFile picture,
            MultipartFile audio) {
        List<Question> questions = questionRepository.findAllById(request.getQuestionIds());
        if (questions.size() != request.getQuestionIds().size()) {
            throw new ObjectNotFoundException("Some questionIds are invalid");
        }

        Test test = Test.builder()
                .title(request.getTitle())
                .topic(request.getTopic())
                .skill(request.getSkill())
                .timeLimit(request.getTimeLimit())
                .type(request.getType())
                .publishedDate(Instant.now())
                .totalParticipants(0L)
                .isActive(request.getIsActive())
                .difficulty(request.getDifficulty())
                .questions(questions)
                .build();

        handleAssignTestPicture(test, picture);
        handleAssignTestAudio(test, audio);

        Test saved = testRepository.save(test);
        return convertToDto(saved);
    }

    // Update
    @Transactional
    public FetchTestResponse handleUpdateTest(UpdateTestRequest request,
            MultipartFile picture,
            MultipartFile audio) {
        Test test = testRepository.findById(request.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Test not found with id: " + request.getId()));

        test.setTitle(request.getTitle());
        test.setTopic(request.getTopic());
        test.setSkill(request.getSkill());
        test.setTimeLimit(request.getTimeLimit());
        test.setType(request.getType());
        test.setIsActive(request.getIsActive());
        test.setDifficulty(request.getDifficulty());

        handleAssignTestPicture(test, picture);
        handleAssignTestAudio(test, audio);

        if (request.getQuestionIds() != null && !request.getQuestionIds().isEmpty()) {
            List<Question> questions = questionRepository.findAllById(request.getQuestionIds());
            if (questions.size() != request.getQuestionIds().size()) {
                throw new ObjectNotFoundException("Some questionIds are invalid");
            }
            test.setQuestions(questions);
        }

        Test updated = testRepository.save(test);
        return convertToDto(updated);
    }

    // Delete
    @Transactional
    public void handleDeleteTest(Long id) {
        Test test = testRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Test not found with id: " + id));

        if (test.getPicture() != null && !test.getPicture().isEmpty()) {
            String blobName = azureBlobService.getBlobNameFromUrl(test.getPicture());
            if (blobName != null) {
                azureBlobService.deleteFile(blobName);
            }
        }

        if (test.getAudio() != null && !test.getAudio().isEmpty()) {
            String blobName = azureBlobService.getBlobNameFromUrl(test.getAudio());
            if (blobName != null) {
                azureBlobService.deleteFile(blobName);
            }
        }

        if (test.getQuestions() != null) {
            test.getQuestions().forEach(q -> q.getTests().remove(test));
            test.getQuestions().clear();
        }

        testRepository.delete(test);
    }
}
