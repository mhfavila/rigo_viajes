package com.controlviajesv2.exception;

import com.controlviajesv2.dto.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Maneja errores cuando no se encuentra un recurso (ej. empresa, usuario, etc.)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> manejarRecursoNoEncontrado(ResourceNotFoundException ex,HttpServletRequest request) {
        logger.warn("Recurso no encontrado: {}", ex.getMessage()); // log tipo WARN

        String path = request.getRequestURI();

        ApiError error = new ApiError(HttpStatus.NOT_FOUND, ex.getMessage(), path);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);


    }

    // Maneja errores de validación (por ejemplo, anotaciones como @NotBlank, @Email, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> manejarErroresValidacion(MethodArgumentNotValidException ex, WebRequest request) {

        // Construimos un mensaje concatenando todos los errores
        String errores = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        logger.info("Error de validación: {}", errores); // log tipo INFO
        String path = ((ServletWebRequest) request).getRequest().getRequestURI();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, errores, path);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    // Maneja cualquier otro tipo de error no controlado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> manejarErroresGenerales(Exception ex, HttpServletRequest request) {

        logger.error("Error interno en la aplicación", ex); // log tipo ERROR con stacktrace
        String path = request.getRequestURI(); // Obtener la URI que causó el error
        ApiError error = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ha ocurrido un error interno: " + ex.getLocalizedMessage(),
                path
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
