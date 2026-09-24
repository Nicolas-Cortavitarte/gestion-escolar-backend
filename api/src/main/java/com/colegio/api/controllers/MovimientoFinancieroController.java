package com.colegio.api.controllers;

import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.api.dtos.MovimientoFinancieroRequestDto;
import com.colegio.api.dtos.MovimientoFinancieroResponseDto;
import com.colegio.api.services.MovimientoFinancieroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/movimientos-financieros")
@PreAuthorize("hasRole('ADMIN')")
public class MovimientoFinancieroController {

    private final MovimientoFinancieroService movimientoService;

    public MovimientoFinancieroController(MovimientoFinancieroService movimientoService) {
        this.movimientoService = movimientoService;
    }

    @GetMapping
    public ResponseEntity<List<MovimientoFinancieroResponseDto>> obtener(
            @RequestParam LocalDate desde,
            @RequestParam LocalDate hasta) {
        return ResponseEntity.ok(movimientoService.obtenerPorRangoFechas(desde, hasta));
    }

    @PostMapping
    public ResponseEntity<MovimientoFinancieroResponseDto> crear(
            @Valid @RequestBody MovimientoFinancieroRequestDto requestDto) {
        return new ResponseEntity<>(movimientoService.crear(requestDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable UUID id) {
        movimientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
