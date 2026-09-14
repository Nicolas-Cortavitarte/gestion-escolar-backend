package com.colegio.api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.api.dtos.NotaCompetenciaRequestDto;
import com.colegio.api.dtos.NotaCompetenciaResponseDto;
import com.colegio.api.services.NotaCompetenciaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/estudiantes/{estudianteId}/notas-competencias")
public class NotaCompetenciaController {

    private final NotaCompetenciaService notaCompetenciaService;

    public NotaCompetenciaController(NotaCompetenciaService notaCompetenciaService) {
        this.notaCompetenciaService = notaCompetenciaService;
    }

    @GetMapping
    public ResponseEntity<List<NotaCompetenciaResponseDto>> obtener(
            @PathVariable UUID estudianteId,
            @RequestParam(required = false) Integer bimestre) {
        if (bimestre != null) {
            return ResponseEntity.ok(notaCompetenciaService.obtenerPorEstudianteYBimestre(estudianteId, bimestre));
        }
        return ResponseEntity.ok(notaCompetenciaService.obtenerPorEstudiante(estudianteId));
    }

    @PostMapping
    public ResponseEntity<NotaCompetenciaResponseDto> registrar(
            @PathVariable UUID estudianteId,
            @Valid @RequestBody NotaCompetenciaRequestDto requestDto) {
        return ResponseEntity.ok(notaCompetenciaService.registrarOActualizar(estudianteId, requestDto));
    }
}
