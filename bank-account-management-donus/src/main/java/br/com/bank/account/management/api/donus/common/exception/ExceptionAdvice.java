package br.com.bank.account.management.api.donus.common.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.time.LocalDateTime;

@RestControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<CustomErrorResponse> handleGenericNotFoundException(NotFoundException e) {
        e.printStackTrace();
        CustomErrorResponse error = new CustomErrorResponse(
                HttpStatus.NOT_FOUND.getReasonPhrase(),
                e.getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setStatus((HttpStatus.NOT_FOUND.value()));
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<CustomErrorResponse> handleGenericConstraintViolationException(
            DataIntegrityViolationException e){
        e.printStackTrace();
        CustomErrorResponse error = new CustomErrorResponse(
                HttpStatus.PRECONDITION_FAILED.getReasonPhrase(),
                e.getMostSpecificCause().getMessage());
        error.setTimestamp(LocalDateTime.now());
        error.setStatus((HttpStatus.PRECONDITION_FAILED.value()));
        return new ResponseEntity<>(error, HttpStatus.PRECONDITION_FAILED);
    }

    @ExceptionHandler({BadRequestException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<CustomErrorResponse> handleGenericBadRequestException(Exception e) {
        String message;

        if(e instanceof BadRequestException){
            message = e.getMessage();
        } else {
            message = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors().get(0).getDefaultMessage();
        }

        e.printStackTrace();
        CustomErrorResponse error = new CustomErrorResponse(
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                message);
        error.setTimestamp(LocalDateTime.now());
        error.setStatus((HttpStatus.BAD_REQUEST.value()));
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
