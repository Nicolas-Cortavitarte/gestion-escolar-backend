package com.colegio.api.controllers;

import java.util.List;
import java.util.UUID;

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

import com.colegio.api.dtos.CursoRequestDto;
import com.colegio.api.dtos.CursoResponseDto;
import com.colegio.api.services.CursoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/cursos")
public class CursoController {

    private final CursoService cursoService;

    public CursoController(CursoService cursoService) {
        this.cursoService = cursoService;
    }

    @GetMapping
    public ResponseEntity<List<CursoResponseDto>> obtenerTodos() {
        return ResponseEntity.ok(cursoService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursoResponseDto> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(cursoService.obtenerPorId(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CursoResponseDto> crear(@Valid @RequestBody CursoRequestDto cursoRequestDto) {
        return ResponseEntity.ok(cursoService.crear(cursoRequestDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CursoResponseDto> actualizar(@Valid @PathVariable UUID id,
            @RequestBody CursoRequestDto cursoRequestDto) {
        return ResponseEntity.ok(cursoService.actualizar(id, cursoRequestDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        cursoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

}
