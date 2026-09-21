package com.colegio.api.controllers;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.api.dtos.PagoDocenteResponseDto;
import com.colegio.api.services.PagoDocenteService;

@RestController
@RequestMapping("/api/pagos-docentes")
public class PagoDocenteController {

    private final PagoDocenteService pagoDocenteService;

    public PagoDocenteController(PagoDocenteService pagoDocenteService) {
        this.pagoDocenteService = pagoDocenteService;
    }

    @GetMapping
    public ResponseEntity<List<PagoDocenteResponseDto>> obtenerTodos() {
        return ResponseEntity.ok(pagoDocenteService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDocenteResponseDto> obtenerPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(pagoDocenteService.obtenerPorId(id));
    }

    @GetMapping("/docente/{docenteId}")
    public ResponseEntity<List<PagoDocenteResponseDto>> obtenerPorDocente(@PathVariable UUID docenteId) {
        return ResponseEntity.ok(pagoDocenteService.obtenerPorDocente(docenteId));
    }

    @PatchMapping("/{id}/pagar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagoDocenteResponseDto> marcarComoPagado(
            @PathVariable UUID id,
            @RequestParam(required = false) OffsetDateTime fechaPago) {
        return ResponseEntity.ok(pagoDocenteService.marcarComoPagado(id, fechaPago));
    }
}
