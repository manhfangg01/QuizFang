package com.quizfang.quizfang.util.error;

public class DuplicatedObjectException extends RuntimeException {
    public DuplicatedObjectException(String message) {
        super(message);
    }
}