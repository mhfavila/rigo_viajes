package com.controlviajesv2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = "*")
public class despertarController {

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        // Solo retorna un texto simple.
        // El hecho de recibir la petición despierta a Render.
        return ResponseEntity.ok("Pong! Servidor despierto.");
    }
}
