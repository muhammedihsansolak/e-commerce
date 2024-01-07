package com.cydeo.dto.response;

import com.cydeo.exception.ValidationException;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ExceptionResponse {

    private String message;
    private HttpStatus status;
    private LocalDateTime localDateTime = LocalDateTime.now();
    private Integer errorCount;
    private List<ValidationException> validationExceptions;

    public ExceptionResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }
}
