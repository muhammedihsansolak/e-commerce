package com.cydeo.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@Setter
public class ExceptionResponse {

    private String message;
    private HttpStatus status;
    private LocalDateTime localDateTime;
    private Integer errorCount;
    private List<ValidationException> validationExceptions;

    public ExceptionResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
        this.localDateTime = LocalDateTime.now();
    }
}
