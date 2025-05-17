package com.tds.challenge.urlshortener.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tds.challenge.urlshortener.domain.model.ErrorResponse;
import com.tds.challenge.urlshortener.suport.expection.BadRequestException;
import com.tds.challenge.urlshortener.suport.expection.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@ControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @JsonFormat
    ErrorResponse notFoundRequestException(final NotFoundException e){
        return new ErrorResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }

    @ExceptionHandler({BadRequestException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @JsonFormat
    ErrorResponse badRequestException(final BadRequestException e) {
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpectedException(final Exception e) {
        log.error("Erro interno inesperado", e);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Erro interno no servidor");
    }
}
