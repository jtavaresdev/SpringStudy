package org.example.handler;


import org.example.exception.BadRequestException;
import org.example.exception.BadRequestExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handleBadRequest(BadRequestException ex) {
        return new ResponseEntity<>(
                BadRequestExceptionDetails.builder()
                        .timeStamp(LocalDateTime.now())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .title("BAD REQUEST EXCEPTION, CHECK THE DOCUMENTATION")
                        .details(ex.getMessage())
                        .developerMessage(ex.getClass().getCanonicalName())
                        .build(),
                HttpStatus.BAD_REQUEST
        );
    }

}
