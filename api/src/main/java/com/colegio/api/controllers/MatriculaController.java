package com.colegio.api.controllers;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.api.dtos.MatriculaRequestDto;
import com.colegio.api.dtos.MatriculaResponseDto;
import com.colegio.api.services.MatriculaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/matriculas")
public class MatriculaController {

    private final MatriculaService matriculaService;

    public MatriculaController(MatriculaService matriculaService) {
        this.matriculaService = matriculaService;
    }

    @GetMapping
    public ResponseEntity<List<MatriculaResponseDto>> obtenerTodos() {
        return ResponseEntity.ok(matriculaService.obtenerTodos());
    }

    @GetMapping("/estudiante/{estudianteId}")
    public ResponseEntity<List<MatriculaResponseDto>> obtenerPorEstudiante(@PathVariable UUID estudianteId) {
        return ResponseEntity.ok(matriculaService.obtenerPorEstudiante(estudianteId));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MatriculaResponseDto> crear(@Valid @RequestBody MatriculaRequestDto requestDto) {
        MatriculaResponseDto creada = matriculaService.crear(requestDto);
        return new ResponseEntity<>(creada, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MatriculaResponseDto> actualizar(
            @PathVariable UUID id,
            @Valid @RequestBody MatriculaRequestDto requestDto) {
        return ResponseEntity.ok(matriculaService.actualizar(id, requestDto));
    }

    @PatchMapping("/{id}/pagar-matricula")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MatriculaResponseDto> marcarMatriculaComoPagada(
            @PathVariable UUID id,
            @RequestParam(required = false) OffsetDateTime fechaPago) {
        return ResponseEntity.ok(matriculaService.marcarMatriculaComoPagada(id, fechaPago));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        matriculaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
