package com.cydeo.exception.handler;

import com.cydeo.dto.response.ExceptionResponse;
import com.cydeo.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            Exception.class,
            RuntimeException.class,
            Throwable.class
    })
    public ResponseEntity<ExceptionResponse> genericExceptionHandler(Throwable exception){
        exception.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ExceptionResponse.builder()
                        .message("An error occurred!")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .build()
                );
    }

    @ExceptionHandler({
            AccessDeniedException.class
    })
    public ResponseEntity<ExceptionResponse> accessDeniedExceptionHandler(Throwable exception){
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ExceptionResponse.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.FORBIDDEN)
                        .localDateTime(LocalDateTime.now())
                        .build()
                );
    }

    @ExceptionHandler({
            AddressNotFoundException.class,
            CustomerNotFoundException.class,
            DiscountNotFoundException.class,
            OrderNotFoundException.class,
            ProductNotFoundException.class
    })
    public ResponseEntity<ExceptionResponse> customNotFoundExceptionHandler(Throwable exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ExceptionResponse.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.NOT_FOUND)
                        .build()
                );
    }

    @ExceptionHandler({
            CurrencyInvalidException.class
    })
    public ResponseEntity<ExceptionResponse> CurrencyInvalidExceptionHandler(CurrencyInvalidException exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .message(exception.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build()
                );
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ExceptionResponse> validationExceptionHandler(MethodArgumentNotValidException exception){
        exception.printStackTrace();

        ExceptionResponse response = new ExceptionResponse("Invalid input(s)", HttpStatus.BAD_REQUEST);

        List<ValidationException> validationErrors = new ArrayList<>();

        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) error).getField();
            Object rejectedValue = ((FieldError) error).getRejectedValue();
            String errorMessage = error.getDefaultMessage();

            ValidationException validationException = new ValidationException(fieldName, rejectedValue, errorMessage);
            validationErrors.add(validationException);
        }

        response.setValidationExceptions(validationErrors);
        response.setErrorCount(validationErrors.size());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

}
