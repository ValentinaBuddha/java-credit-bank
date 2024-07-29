package ru.neoflex.gateway.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;


@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validateException(BadRequestException e) {
        log.info(e.getMessage());
        return new ErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse validateException(MethodArgumentNotValidException e) {
        log.info(e.getMessage());
        String message = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));
        return new ErrorResponse(HttpStatus.BAD_REQUEST, message);
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse conflictException(ConflictException e) {
        log.info(e.getMessage());
        return new ErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse entityNotFoundException(NotFoundException e) {
        log.info(e.getMessage());
        return new ErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler({Throwable.class, ServerException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(Throwable e) {
        log.info(e.getMessage());
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}