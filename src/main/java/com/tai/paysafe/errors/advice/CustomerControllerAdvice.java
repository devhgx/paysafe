package com.tai.paysafe.errors.advice;

import com.tai.paysafe.dto.response.ErrorResponse;
import com.tai.paysafe.errors.exception.ApproveException;
import com.tai.paysafe.errors.exception.BadRequstException;
import com.tai.paysafe.errors.exception.WithdrawException;
import lombok.extern.slf4j.Slf4j;
//import org.postgresql.util.PSQLException;
import org.postgresql.util.PSQLException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class CustomerControllerAdvice {
    private List<String> getErrorList(BindException ex) {
        List<String> errors = new ArrayList<String>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        return errors;
    }

    @ExceptionHandler({MethodArgumentNotValidException.class })
    public ResponseEntity<ErrorResponse> handleBadRequestException(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "validate fail", getErrorList(ex));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(PSQLException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(PSQLException ex) {
        log.error(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "internal server error", List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundException(NotFoundException ex) {
        log.error(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), "not found", List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    @ExceptionHandler({BadRequstException.class})
    public ResponseEntity<ErrorResponse> badRequestException(Exception ex) {
        log.error(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "baf request", List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    @ExceptionHandler({Exception.class, ApproveException.class, WithdrawException.class})
    public ResponseEntity<ErrorResponse> runTimeException(Exception ex) {
        log.error(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "internal server error", List.of(ex.getMessage()));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }


}
