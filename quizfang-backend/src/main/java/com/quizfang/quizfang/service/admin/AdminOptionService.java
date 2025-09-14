package com.quizfang.quizfang.service.admin;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.quizfang.quizfang.domain.dto.request.admin.option.CreateOptionRequest;
import com.quizfang.quizfang.domain.dto.request.admin.option.UpdateOptionRequest;
import com.quizfang.quizfang.domain.dto.response.admin.option.FetchOptionResponse;
import com.quizfang.quizfang.domain.entity.Option;
import com.quizfang.quizfang.domain.entity.User;
import com.quizfang.quizfang.repository.OptionRepository;
import com.quizfang.quizfang.service.azure.AzureBlobService;
import com.quizfang.quizfang.util.error.InvalidUploadedFile;
import com.quizfang.quizfang.util.error.ObjectNotFoundException;

@Service
public class AdminOptionService {
    private final OptionRepository optionRepository;
    private final AzureBlobService azureBlobService;

    public AdminOptionService(OptionRepository optionRepository, AzureBlobService azureBlobService) {
        this.optionRepository = optionRepository;
        this.azureBlobService = azureBlobService;
    }

    public FetchOptionResponse convertToDto(Option option) {
        return FetchOptionResponse.builder()
                .id(option.getId())
                .content(option.getContent())
                .orderIndex(option.getOrderIndex())
                .isCorrect(option.getIsCorrect())
                .audio(option.getAudio())
                .picture(option.getPicture())
                .questionId(option.getQuestion() != null ? option.getQuestion().getId() : null)
                .build();
    }

    public void handleAssignOptionPicture(Option option, MultipartFile picture) {
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
                option.setPicture(imageUrl);
            } else {
                option.setPicture("");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload avatar", e);
        }

    }

    public void handleAssignOptionAudio(Option option, MultipartFile audio) {
        try {
            if (audio != null && !audio.isEmpty()) {
                List<String> allowedExtensions = List.of(".mp3", ".wav", ".ogg", ".m4a");

                String fileName = audio.getOriginalFilename().toLowerCase();
                String contentType = audio.getContentType();
                // boolean validMime = contentType != null && contentType.startsWith("audio/");
                // // Kiểm tra định dạng MINE
                boolean validExt = allowedExtensions.stream().anyMatch(fileName::endsWith); // Kiểm tra phần mở rộng
                if (!validExt) {
                    throw new InvalidUploadedFile(
                            "Invalid file type. Only allows: " + allowedExtensions.toString());
                }

                String audioUrl = azureBlobService.uploadFile(audio);
                option.setAudio(audioUrl);
            } else {
                option.setAudio(null);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload audio", e);
        }
    }

    public List<FetchOptionResponse> handleFetchAllOptions() {
        List<Option> dbOptions = this.optionRepository.findAll();
        List<FetchOptionResponse> allOptionDto = dbOptions.stream().map(this::convertToDto)
                .collect(Collectors.toList());
        return allOptionDto;
    }

    public FetchOptionResponse handleFetchOption(Long id) {
        Option option = optionRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Option not found with id: " + id));
        return this.convertToDto(option);
    }

    public FetchOptionResponse handleCreateOption(CreateOptionRequest newOption, MultipartFile audio,
            MultipartFile picture) {
        Option createdOption = Option.builder()
                .orderIndex(newOption.getOrderIndex())
                .content(newOption.getContent())
                .isCorrect(newOption.getIsCorrect())
                .build();
        handleAssignOptionAudio(createdOption, audio);
        handleAssignOptionPicture(createdOption, picture);
        return this.convertToDto(this.optionRepository.save(createdOption));
    }

    public FetchOptionResponse handleUpdateOption(UpdateOptionRequest updatedOption, MultipartFile audio,
            MultipartFile picture) {
        Optional<Option> checkOption = this.optionRepository.findById(updatedOption.getId());
        if (checkOption.isEmpty()) {
            throw new ObjectNotFoundException("There is no user has id: " + updatedOption.getId());
        }
        Option realOption = checkOption.get();
        realOption.setContent(updatedOption.getContent());
        realOption.setIsCorrect(updatedOption.getIsCorrect());
        realOption.setOrderIndex(updatedOption.getOrderIndex());
        // Set Question
        handleAssignOptionAudio(realOption, audio);
        handleAssignOptionPicture(realOption, picture);

        return this.convertToDto(realOption);
    }

    public void handleDeleteOption(Long id) {
        Optional<Option> checkOption = this.optionRepository.findById(id);
        if (checkOption.isEmpty()) {
            throw new ObjectNotFoundException("There is no option has id: " + id);
        }
        Option realOption = checkOption.get();
        if (realOption.getPicture() != null && !realOption.getPicture().isEmpty()) {
            String blobName = azureBlobService.getBlobNameFromUrl(realOption.getPicture());
            if (blobName != null) {
                azureBlobService.deleteFile(blobName);
            }
        }

        if (realOption.getAudio() != null && !realOption.getAudio().isEmpty()) {
            String blobName = azureBlobService.getBlobNameFromUrl(realOption.getAudio());
            if (blobName != null) {
                azureBlobService.deleteFile(blobName);
            }
        }
        this.optionRepository.delete(realOption);

    }

}
