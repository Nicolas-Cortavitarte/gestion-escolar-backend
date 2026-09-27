package com.colegio.api.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.api.dtos.InscripcionRequestDto;
import com.colegio.api.dtos.InscripcionResponseDto;
import com.colegio.api.services.InscripcionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/inscripciones")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @PostMapping
    public ResponseEntity<InscripcionResponseDto> crear(
            @Valid @RequestBody InscripcionRequestDto requestDto) {

        InscripcionResponseDto resultado = inscripcionService.crear(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }

}
