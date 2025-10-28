package org.example.cuidadodemascotas.usermicroservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiError> handleNotFoundException(
            NotFoundException ex,
            WebRequest request) {

        log.error("NotFoundException: {}", ex.getMessage());

        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgumentException(
            IllegalArgumentException ex,
            WebRequest request) {

        log.error("IllegalArgumentException: {}", ex.getMessage());

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            MethodArgumentNotValidException ex,
            WebRequest request) {

        log.error("Validation error: {}", ex.getMessage());

        String errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ApiError error = new ApiError(
                HttpStatus.BAD_REQUEST.value(),
                "Validation Failed",
                errors,
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiError> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            WebRequest request) {

        log.error("NoHandlerFoundException: {}", ex.getMessage());

        ApiError error = new ApiError(
                HttpStatus.NOT_FOUND.value(),
                "Endpoint Not Found",
                "El endpoint solicitado no existe: " + ex.getRequestURL(),
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGlobalException(
            Exception ex,
            WebRequest request) {

        log.error("Internal Server Error: ", ex);

        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Ha ocurrido un error inesperado en el servidor",
                request.getDescription(false).replace("uri=", "")
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
