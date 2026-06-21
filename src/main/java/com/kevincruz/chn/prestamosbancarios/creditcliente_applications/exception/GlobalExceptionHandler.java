package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarValidaciones(
        MethodArgumentNotValidException exception){
            Map<String, String> errores = new LinkedHashMap<>();

            exception.getBindingResult().getFieldErrors().forEach(error -> {
                String nombreCampo = error.getField();
                String mensajeError = error.getDefaultMessage();
                errores.put(nombreCampo, mensajeError);
            });

            Map<String, Object> respuesta = new LinkedHashMap<>();
            respuesta.put("message", "Campos invalidos");
            respuesta.put("errors", errores);

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }
}
