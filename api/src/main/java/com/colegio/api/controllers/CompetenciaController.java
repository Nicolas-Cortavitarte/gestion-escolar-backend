package com.colegio.api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.api.dtos.CompetenciaRequestDto;
import com.colegio.api.dtos.CompetenciaResponseDto;
import com.colegio.api.services.CompetenciaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/competencias")
public class CompetenciaController {

    private final CompetenciaService competenciaService;

    public CompetenciaController(CompetenciaService competenciaService) {
        this.competenciaService = competenciaService;
    }

    @GetMapping
    public ResponseEntity<List<CompetenciaResponseDto>> obtenerTodasLasCompetencias() {
        return ResponseEntity.ok(competenciaService.obtenerTodasLasCompetencias());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompetenciaResponseDto> obtenerCompetenciaPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(competenciaService.obtenerCompetenciaPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompetenciaResponseDto> crear(@Valid @RequestBody CompetenciaRequestDto requestDto) {
        return new ResponseEntity<>(competenciaService.crear(requestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CompetenciaResponseDto> actualizar(@PathVariable UUID id,
            @Valid @RequestBody CompetenciaRequestDto requestDto) {
        return ResponseEntity.ok(competenciaService.actualizar(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        competenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
