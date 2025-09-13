package com.quizfang.quizfang.util.error;

public class InvalidUploadedFile extends RuntimeException {
    public InvalidUploadedFile(String message) {
        super(message);
    }
}