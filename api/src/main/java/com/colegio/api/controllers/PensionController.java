package com.colegio.api.controllers;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.colegio.api.dtos.PensionResponseDto;
import com.colegio.api.services.PensionService;

@RestController
@RequestMapping("/api/pensiones")
public class PensionController {

    private final PensionService pensionService;

    public PensionController(PensionService pensionService) {
        this.pensionService = pensionService;
    }

    @GetMapping
    public ResponseEntity<List<PensionResponseDto>> obtenerTodas() {
        return ResponseEntity.ok(pensionService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PensionResponseDto> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(pensionService.obtenerPorId(id));
    }

    @GetMapping("/matricula/{matriculaId}")
    public ResponseEntity<List<PensionResponseDto>> obtenerPorMatricula(@PathVariable UUID matriculaId) {
        return ResponseEntity.ok(pensionService.obtenerPorMatricula(matriculaId));
    }

    @PatchMapping("/{id}/pagar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PensionResponseDto> marcarComoPagado(
            @PathVariable UUID id,
            @RequestParam(required = false) OffsetDateTime fechaPago) {
        return ResponseEntity.ok(pensionService.marcarComoPagado(id, fechaPago));
    }
}
