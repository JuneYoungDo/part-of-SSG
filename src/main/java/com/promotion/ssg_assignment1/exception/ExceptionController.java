package com.promotion.ssg_assignment1.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity customException(BaseException exception) {
        return new ResponseEntity(new BaseResponse(exception.getStatus()), HttpStatus.valueOf(exception.getStatus().getStatus()));
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity dateTimeException(DateTimeParseException e) {
        BaseResponseStatus exception = BaseResponseStatus.INVALID_DATE_TYPE;
        return new ResponseEntity(new BaseResponse(exception), HttpStatus.valueOf(exception.getStatus()));
    }
}
