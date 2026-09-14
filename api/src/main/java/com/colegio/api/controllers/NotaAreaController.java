package com.colegio.api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.api.dtos.NotaAreaResponseDto;
import com.colegio.api.services.NotaAreaService;

@RestController
@RequestMapping("/api/v1/estudiantes/{estudianteId}/notas-areas")
public class NotaAreaController {

    private final NotaAreaService notaAreaService;

    public NotaAreaController(NotaAreaService notaAreaService) {
        this.notaAreaService = notaAreaService;
    }

    @GetMapping
    public ResponseEntity<List<NotaAreaResponseDto>> obtener(
            @PathVariable UUID estudianteId,
            @RequestParam(required = false) Integer bimestre) {
        if (bimestre != null) {
            return ResponseEntity.ok(notaAreaService.obtenerPorEstudianteYBimestre(estudianteId, bimestre));
        }
        return ResponseEntity.ok(notaAreaService.obtenerPorEstudiante(estudianteId));
    }

}
