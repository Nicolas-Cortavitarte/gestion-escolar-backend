package com.colegio.api.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.api.dtos.DocenteRequestDto;
import com.colegio.api.dtos.DocenteResponseDto;
import com.colegio.api.dtos.DocenteUpdateDto;
import com.colegio.api.services.DocenteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/docentes")
public class DocenteController {

    private final DocenteService docenteService;

    public DocenteController(DocenteService docenteService) {
        this.docenteService = docenteService;
    }

    @GetMapping
    public ResponseEntity<List<DocenteResponseDto>> listarTodos() {
        return ResponseEntity.ok(docenteService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DocenteResponseDto> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(docenteService.ontenerPorId(id));
    }

    @GetMapping("/buscar/{dni}")
    public ResponseEntity<DocenteResponseDto> obtenerPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(docenteService.buscarPorDni(dni));
    }

    @PostMapping
    public ResponseEntity<DocenteResponseDto> crear(@Valid @RequestBody DocenteRequestDto requestDto) {
        DocenteResponseDto creado = docenteService.crear(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DocenteResponseDto> actualizar(@PathVariable UUID id,
            @Valid @RequestBody DocenteUpdateDto requestDto) {
        return ResponseEntity.ok(docenteService.actualizar(id, requestDto));
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable UUID id) {
        docenteService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/reactivar")
    public ResponseEntity<Void> reactivar(@PathVariable UUID id) {
        docenteService.reactivar(id);
        return ResponseEntity.noContent().build();
    }

}
