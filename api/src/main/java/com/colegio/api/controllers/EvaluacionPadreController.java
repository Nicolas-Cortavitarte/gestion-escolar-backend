package com.colegio.api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.api.dtos.EvaluacionPadreRequestDto;
import com.colegio.api.dtos.EvaluacionPadreResponseDto;
import com.colegio.api.services.EvaluacionPadreService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/estudiantes/{estudianteId}/evaluacion-padre")
public class EvaluacionPadreController {

    private final EvaluacionPadreService evaluacionPadreService;

    public EvaluacionPadreController(EvaluacionPadreService evaluacionPadreService) {
        this.evaluacionPadreService = evaluacionPadreService;
    }

    @GetMapping
    public ResponseEntity<List<EvaluacionPadreResponseDto>> obtener(@PathVariable UUID estudianteId) {
        return ResponseEntity.ok(evaluacionPadreService.obtenerPorEstudiante(estudianteId));
    }

    @PostMapping
    public ResponseEntity<EvaluacionPadreResponseDto> registrar(
            @PathVariable UUID estudianteId,
            @Valid @RequestBody EvaluacionPadreRequestDto requestDto) {
        return ResponseEntity.ok(evaluacionPadreService.registrarOActualizar(estudianteId, requestDto));
    }
}
