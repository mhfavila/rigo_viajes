package com.rigoV2.controlViajesV2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Maneja errores cuando no se encuentra un recurso (ej. empresa, usuario, etc.)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>> manejarRecursoNoEncontrado(ResourceNotFoundException ex) {
        logger.warn("Recurso no encontrado: {}", ex.getMessage()); // log tipo WARN

        Map<String, String> respuesta = new HashMap<>();

        respuesta.put("error", ex.getMessage());
        return new ResponseEntity<>(respuesta, HttpStatus.NOT_FOUND);
    }

    // Maneja errores de validación (por ejemplo, anotaciones como @NotBlank, @Email, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarErroresValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();

        // Extrae todos los mensajes de error de los campos con fallos de validación
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errores.put(error.getField(), error.getDefaultMessage())
        );
        logger.info("Error de validación: {}", errores); // log tipo INFO
        return new ResponseEntity<>(errores, HttpStatus.BAD_REQUEST);
    }

    // Maneja cualquier otro tipo de error no controlado
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> manejarErroresGenerales(Exception ex) {

        logger.error("Error interno en la aplicación", ex); // log tipo ERROR con stacktrace
         Map<String, String> respuesta = new HashMap<>();
        respuesta.put("error", "Ha ocurrido un error interno: " + ex.getLocalizedMessage());
        return new ResponseEntity<>(respuesta, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
