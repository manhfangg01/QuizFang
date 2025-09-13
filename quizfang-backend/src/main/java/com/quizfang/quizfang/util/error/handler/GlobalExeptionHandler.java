package com.quizfang.quizfang.util.error.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.quizfang.quizfang.domain.dto.restResponse.RestResponse;
import com.quizfang.quizfang.util.error.DuplicatedObjectException;
import com.quizfang.quizfang.util.error.ObjectNotFoundException;

@RestControllerAdvice
public class GlobalExeptionHandler {
    @ExceptionHandler(value = ObjectNotFoundException.class)
    public ResponseEntity<RestResponse<Object>> handleDuplication(ObjectNotFoundException ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("ObjectNotFoundException");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }

    @ExceptionHandler(value = DuplicatedObjectException.class)
    public ResponseEntity<RestResponse<Object>> handleDuplication(DuplicatedObjectException ex) {
        RestResponse<Object> res = new RestResponse<Object>();
        res.setStatusCode(HttpStatus.BAD_REQUEST.value());
        res.setError("DuplicatedObjectException");
        res.setMessage(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(res);
    }
}
