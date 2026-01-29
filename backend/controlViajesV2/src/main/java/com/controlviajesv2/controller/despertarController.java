package com.controlviajesv2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "*")
@Tag(name = "Health Check", description = "Endpoints de utilidad para mantener el servidor activo")
public class despertarController {
    @Operation(summary = "Ping al servidor", description = "Endpoint ligero para despertar la instancia en Render o comprobar conectividad.")
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        // Solo retorna un texto simple.
        // El hecho de recibir la petición despierta a Render.
        return ResponseEntity.ok("Pong! Servidor despierto.");
    }
}
