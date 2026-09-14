package com.colegio.api.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.api.dtos.BoletaResponseDto;
import com.colegio.api.services.BoletaService;

@RestController
@RequestMapping("/api/v1/boletas")
public class BoletaController {

    private final BoletaService boletaService;

    public BoletaController(BoletaService boletaService) {
        this.boletaService = boletaService;
    }

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<BoletaResponseDto> getBoletaByEstudianteId(
            @PathVariable UUID estudianteId, @RequestParam Integer anioLectivo) {
        return ResponseEntity.ok(boletaService.generarBoleta(estudianteId, anioLectivo));
    }

}
