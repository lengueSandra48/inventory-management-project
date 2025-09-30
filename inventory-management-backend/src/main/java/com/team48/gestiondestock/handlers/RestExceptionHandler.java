package com.team48.gestiondestock.handlers;

import com.team48.gestiondestock.exception.EntityNotFoundException;
import com.team48.gestiondestock.exception.InvalidEntityException;
import com.team48.gestiondestock.exception.BadCredentialsException;
import com.team48.gestiondestock.exception.InvalidImageFormatException;
import com.team48.gestiondestock.exception.ErrorCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice //Avec cette annotation, on a pas besoin d'ajouter @ResponseBody à chaque methodes
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    //premier type d'exception levée
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDto> handleException(EntityNotFoundException exception, WebRequest webRequest) {
    final HttpStatus notFound = HttpStatus.NOT_FOUND;
    final ErrorDto errorDto = ErrorDto.builder()
                .errorCodes(exception.getErrorCode())
                .httpCode(notFound.value())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorDto, notFound);
    }

    //deuxieme type d'exception levée
    @ExceptionHandler(InvalidEntityException.class)
    public ResponseEntity<ErrorDto> handleException(InvalidEntityException invalidEntityException, WebRequest webRequest) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        final ErrorDto errorDto = ErrorDto.builder()
                .errorCodes(invalidEntityException.getErrorCode())
                .httpCode(badRequest.value())
                .message(invalidEntityException.getMessage())
                .errors(invalidEntityException.getErrors())
                .build();
        return new ResponseEntity<>(errorDto, badRequest);
    }

    @ExceptionHandler(InvalidImageFormatException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDto handleInvalidImageFormatException(InvalidImageFormatException ex) {
        ErrorDto errorDto = new ErrorDto();
        errorDto.setHttpCode(HttpStatus.BAD_REQUEST.value());
        errorDto.setMessage(ex.getMessage());
        errorDto.setErrorCodes(ErrorCodes.IMAGE_FORMAT_NOT_SUPPORTED);
        return errorDto;
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleException(BadCredentialsException exception, WebRequest webRequest) {
        final HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        final ErrorDto errorDto = ErrorDto.builder()
                .errorCodes(exception.getErrorCode())
                .httpCode(badRequest.value())
                .message(exception.getMessage())
                .build();
        return new ResponseEntity<>(errorDto, badRequest);
    }


}
