package com.colegio.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.colegio.api.dtos.ReporteConductaRequestDto;
import com.colegio.api.dtos.ReporteConductaResponseDto;
import com.colegio.api.services.ReporteConductaService;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/estudiantes/{estudianteId}/reportes-conducta")
public class ReporteConductaController {

    private final ReporteConductaService reporteConductaService;

    public ReporteConductaController(ReporteConductaService reporteConductaService) {
        this.reporteConductaService = reporteConductaService;
    }

    @GetMapping
    public ResponseEntity<List<ReporteConductaResponseDto>> obtenerPorEstudiante(
            @PathVariable UUID estudianteId) {
        return ResponseEntity.ok(reporteConductaService.obtenerPorEstudiante(estudianteId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN','DOCENTE')")
    public ResponseEntity<ReporteConductaResponseDto> registrarOActualizar(
            @PathVariable UUID estudianteId,
            @Valid @RequestBody ReporteConductaRequestDto requestDto) {
        ReporteConductaResponseDto responseDto = reporteConductaService.registrarOActualizar(estudianteId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

}
