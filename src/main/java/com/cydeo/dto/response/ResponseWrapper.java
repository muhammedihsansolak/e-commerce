package com.cydeo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper {

    private boolean success;
    private String message;
    private Integer statusCode;
    private Object data;

    public ResponseWrapper(String message, Object data, HttpStatus httpStatus) {
        this.success = true;
        this.message = message;
        this.statusCode = httpStatus.value();
        this.data = data;
    }

}