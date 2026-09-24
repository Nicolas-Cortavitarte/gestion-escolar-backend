package com.colegio.api.controllers;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.api.dtos.ReporteFinancieroResponseDto;
import com.colegio.api.services.ReporteFinancieroService;

@RestController
@RequestMapping("/api/v1/reportes-financieros")
@PreAuthorize("hasRole('ADMIN')")
public class ReporteFinancieroController {

    private final ReporteFinancieroService reporteFinancieroService;

    public ReporteFinancieroController(ReporteFinancieroService reporteFinancieroService) {
        this.reporteFinancieroService = reporteFinancieroService;
    }

    @GetMapping
    public ResponseEntity<ReporteFinancieroResponseDto> generar(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta) {
        return ResponseEntity.ok(reporteFinancieroService.generarReporte(desde, hasta));
    }
}
