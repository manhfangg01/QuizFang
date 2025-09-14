package com.quizfang.quizfang.service.admin;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quizfang.quizfang.domain.dto.request.admin.question.CreateQuestionRequest;
import com.quizfang.quizfang.domain.dto.request.admin.question.UpdateQuestionRequest;
import com.quizfang.quizfang.domain.dto.response.admin.option.FetchOptionResponse;
import com.quizfang.quizfang.domain.dto.response.admin.question.FetchQuestionResponse;
import com.quizfang.quizfang.domain.entity.Option;
import com.quizfang.quizfang.domain.entity.Question;
import com.quizfang.quizfang.repository.OptionRepository;
import com.quizfang.quizfang.repository.QuestionRepository;
import com.quizfang.quizfang.service.azure.AzureBlobService;
import com.quizfang.quizfang.util.error.InvalidUploadedFile;
import com.quizfang.quizfang.util.error.ObjectNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class AdminQuestionService {
    private final QuestionRepository questionRepository;
    private final AzureBlobService azureBlobService;
    private final OptionRepository optionRepository;

    public AdminQuestionService(QuestionRepository questionRepository, AzureBlobService azureBlobService,
            OptionRepository optionRepository) {
        this.questionRepository = questionRepository;
        this.azureBlobService = azureBlobService;
        this.optionRepository = optionRepository;
    }

    public FetchQuestionResponse convertToDto(Question question) {
        return FetchQuestionResponse.builder()
                .id(question.getId())
                .content(question.getContent())
                .type(question.getType())
                .subType(question.getSubType())
                .orderIndex(question.getOrderIndex())
                .timeLimit(question.getTimeLimit())
                .picture(question.getPicture())
                .audio(question.getAudio())
                .createdDate(question.getCreatedDate())
                .optionIds(question.getOptions() != null
                        ? question.getOptions().stream().map(Option::getId).collect(Collectors.toList())
                        : List.of())
                .build();

    }

    public void handleAssignQuestionPicture(Question question, MultipartFile picture) {
        try {
            if (picture != null && !picture.isEmpty()) {

                List<String> allowedExtensions = Arrays.asList("pdf", "jpg", "jpeg", "png", "doc", "docx", "webp");
                String fileName = picture.getOriginalFilename().toLowerCase();
                String contentType = picture.getContentType();
                boolean validMine = contentType != null && contentType.startsWith("image/");
                boolean validExt = allowedExtensions.stream().anyMatch(fileName::endsWith);
                if (!validMine || !validExt) {
                    throw new InvalidUploadedFile(
                            "Invalid file type. Only allows: " + allowedExtensions.toString());
                }
                String imageUrl = azureBlobService.uploadFile(picture);
                question.setPicture(imageUrl);
            } else {
                question.setPicture("");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload avatar", e);
        }

    }

    public void handleAssignQuestionAudio(Question question, MultipartFile audio) {
        try {
            if (audio != null && !audio.isEmpty()) {
                List<String> allowedExtensions = List.of(".mp3", ".wav", ".ogg", ".m4a");

                String fileName = audio.getOriginalFilename().toLowerCase();
                boolean validExt = allowedExtensions.stream().anyMatch(fileName::endsWith); // Kiểm tra phần mở rộng
                if (!validExt) {
                    throw new InvalidUploadedFile(
                            "Invalid file type. Only allows: " + allowedExtensions.toString());
                }

                String audioUrl = azureBlobService.uploadFile(audio);
                question.setAudio(audioUrl);
            } else {
                question.setAudio(null);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload audio", e);
        }
    }

    public FetchQuestionResponse handleFetchQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Question not found with id: " + id));
        return this.convertToDto(question);
    }

    public List<FetchQuestionResponse> handleFetchAllQuestions() {
        List<Question> dbQuestions = this.questionRepository.findAll();
        List<FetchQuestionResponse> allQuestionDto = dbQuestions.stream().map(this::convertToDto)
                .collect(Collectors.toList());
        return allQuestionDto;
    }

    @Transactional
    public FetchQuestionResponse handleCreateQuestion(CreateQuestionRequest request, MultipartFile audio,
            MultipartFile picture) {
        // Lấy danh sách Option theo id
        List<Option> options = optionRepository.findAllById(request.getOptionIds());

        if (options.size() != request.getOptionIds().size()) {
            throw new ObjectNotFoundException("Some optionIds are invalid");
        }

        // Tạo entity Question
        Question question = Question.builder()
                .content(request.getContent())
                .type(request.getType())
                .subType(request.getSubType())
                .orderIndex(request.getOrderIndex())
                .timeLimit(request.getTimeLimit())
                .createdDate(Instant.now())
                .options(options) // gắn options
                .build();
        handleAssignQuestionAudio(question, audio);
        handleAssignQuestionPicture(question, picture);

        // Gắn quan hệ 2 chiều
        options.forEach(opt -> opt.setQuestion(question));

        // Lưu xuống DB
        Question saved = questionRepository.save(question);

        // Trả về response
        return FetchQuestionResponse.builder()
                .id(saved.getId())
                .content(saved.getContent())
                .type(saved.getType())
                .subType(saved.getSubType())
                .orderIndex(saved.getOrderIndex())
                .timeLimit(saved.getTimeLimit())
                .picture(saved.getPicture())
                .audio(saved.getAudio())
                .createdDate(saved.getCreatedDate())
                .optionIds(
                        saved.getOptions() != null
                                ? saved.getOptions().stream()
                                        .map(Option::getId)
                                        .toList()
                                : List.of())
                .build();
    }

    @Transactional // Chứa thao tác chỉnh sửa và lưu hàng loạt ==> xong tất cả các thao tác rồi mới
                   // commit lên DB
    public FetchQuestionResponse handleUpdateQuestion(UpdateQuestionRequest request,
            MultipartFile audio,
            MultipartFile picture) {
        Question question = questionRepository.findById(request.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Question not found with id: " + request.getId()));

        question.setContent(request.getContent());
        question.setType(request.getType());
        question.setSubType(request.getSubType());
        question.setOrderIndex(request.getOrderIndex());
        question.setTimeLimit(request.getTimeLimit());
        handleAssignQuestionAudio(question, audio);
        handleAssignQuestionPicture(question, picture);
        if (request.getOptionIds() != null && !request.getOptionIds().isEmpty()) {
            List<Option> options = optionRepository.findAllById(request.getOptionIds());

            if (options.size() != request.getOptionIds().size()) {
                throw new IllegalArgumentException("Some optionIds are invalid");
            }

            // reset quan hệ
            options.forEach(opt -> opt.setQuestion(question));
            question.setOptions(options);
        }

        // 5. Lưu lại
        Question updated = questionRepository.save(question);

        // 6. Trả về response
        return FetchQuestionResponse.builder()
                .id(updated.getId())
                .content(updated.getContent())
                .type(updated.getType())
                .subType(updated.getSubType())
                .orderIndex(updated.getOrderIndex())
                .timeLimit(updated.getTimeLimit())
                .picture(updated.getPicture())
                .audio(updated.getAudio())
                .createdDate(updated.getCreatedDate())
                .optionIds(
                        updated.getOptions() != null
                                ? updated.getOptions().stream().map(Option::getId).toList()
                                : List.of())
                .build();
    }

    @Transactional
    public void handleDeleteQuestion(Long id) {
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Question not found with id: " + id));

        if (question.getPicture() != null && !question.getPicture().isEmpty()) {
            String blobName = azureBlobService.getBlobNameFromUrl(question.getPicture());
            if (blobName != null) {
                azureBlobService.deleteFile(blobName);
            }
        }

        if (question.getAudio() != null && !question.getAudio().isEmpty()) {
            String blobName = azureBlobService.getBlobNameFromUrl(question.getAudio());
            if (blobName != null) {
                azureBlobService.deleteFile(blobName);
            }
        }
        if (question.getTests() != null) {
            question.getTests().forEach(test -> test.getQuestions().remove(question));
            question.getTests().clear();
        }

        questionRepository.delete(question);
    }

}
