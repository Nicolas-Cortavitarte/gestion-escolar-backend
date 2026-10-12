package com.colegio.api.exceptions;

import java.time.OffsetDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.persistence.EntityNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(EntityNotFoundException ex) {
        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleBadRequest(IllegalArgumentException ex) {
        return buildResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Object> handleConflict(IllegalStateException ex) {
        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    private ResponseEntity<Object> buildResponse(HttpStatus status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);

        return ResponseEntity.status(status).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidation(
            MethodArgumentNotValidException ex) {

        Map<String, String> erroresCampos = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String mensaje = error.getDefaultMessage();

            erroresCampos.putIfAbsent(
                    error.getField(),
                    mensaje != null ? mensaje : "El valor no es válido.");
        });

        String mensajePrincipal = erroresCampos.values()
                .stream()
                .findFirst()
                .orElse("Revisa los datos enviados.");

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", "Datos inválidos");
        body.put("message", mensajePrincipal);
        body.put("campos", erroresCampos);

        return ResponseEntity.badRequest().body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrity(
            DataIntegrityViolationException ex) {

        return buildResponse(
                HttpStatus.CONFLICT,
                "No se pudo guardar o eliminar el registro. "
                        + "Puede existir un duplicado, faltar un dato obligatorio "
                        + "o haber registros relacionados.");
    }

    @ExceptionHandler(LimiteLoginException.class)
    public ResponseEntity<Object> handleLoginLimit(LimiteLoginException ex) {
        Map<String, Object> body = new LinkedHashMap<>();

        body.put("timestamp", OffsetDateTime.now().toString());
        body.put("status", 429);
        body.put("error", "Demasiadas solicitudes");
        body.put("message", ex.getMessage());

        return ResponseEntity.status(429)
                .header("Retry-After", "60")
                .body(body);
    }

}