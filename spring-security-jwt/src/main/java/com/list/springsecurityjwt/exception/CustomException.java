package com.list.springsecurityjwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class CustomException extends RuntimeException {
    private HttpStatus httpStatus;
    private String info;
}
