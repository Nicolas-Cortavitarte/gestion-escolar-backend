package com.colegio.api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.api.dtos.EstudianteRequestDto;
import com.colegio.api.dtos.EstudianteResponseDto;
import com.colegio.api.services.EstudianteService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @GetMapping
    public ResponseEntity<List<EstudianteResponseDto>> listarTodos() {
        return ResponseEntity.ok(estudianteService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstudianteResponseDto> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(estudianteService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudianteResponseDto> crear(@Valid @RequestBody EstudianteRequestDto requestDto) {
        return new ResponseEntity<>(estudianteService.crear(requestDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudianteResponseDto> actualizar(@PathVariable UUID id,
            @Valid @RequestBody EstudianteRequestDto requestDto) {
        return ResponseEntity.ok(estudianteService.actualizar(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        estudianteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
