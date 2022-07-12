package com.example.getcoins.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status,
                                                                  WebRequest request) {
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        return buildErrorResponse(null, HttpStatus.BAD_REQUEST, errors);

    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> handleCoinServerException(InvalidRequestException ex) {
        return buildErrorResponse(null, HttpStatus.BAD_REQUEST, List.of(ex.getMessage()));
    }

    private ResponseEntity<Object> buildErrorResponse(String message, HttpStatus httpStatus, List<String> errors) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", httpStatus.value());
        if (StringUtils.hasLength(message)) {
            body.put("message", message);
        }
        if (!CollectionUtils.isEmpty(errors)) {
            body.put("errors", errors);
        }

        return new ResponseEntity<>(body, httpStatus);
    }
}
