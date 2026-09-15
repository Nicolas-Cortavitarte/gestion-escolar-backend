package com.colegio.api.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.api.dtos.ResumenFinalResponseDto;
import com.colegio.api.services.ResumenFinalService;

@RestController
@RequestMapping("/api/v1/estudiantes/{estudianteId}/resumen-final")
public class ResumenFinalController {

    private final ResumenFinalService resumenFinalService;

    public ResumenFinalController(ResumenFinalService resumenFinalService) {
        this.resumenFinalService = resumenFinalService;
    }

    @GetMapping
    public ResponseEntity<ResumenFinalResponseDto> obtener(
            @PathVariable UUID estudianteId,
            @RequestParam Integer anioLectivo) {
        return ResponseEntity.ok(resumenFinalService.obtenerPorEstudianteYAnio(estudianteId, anioLectivo));
    }

    @PostMapping("/calcular")
    public ResponseEntity<ResumenFinalResponseDto> calcular(
            @PathVariable UUID estudianteId,
            @RequestParam Integer anioLectivo) {
        return ResponseEntity.ok(resumenFinalService.calcularYGuardar(estudianteId, anioLectivo));
    }
}
