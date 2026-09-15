package com.colegio.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.api.dtos.ApoderadoResponseDto;
import com.colegio.api.services.ApoderadoService;

@RestController
@RequestMapping("/api/v1/apoderados")
public class ApoderadoController {

    private final ApoderadoService apoderadoService;

    public ApoderadoController(ApoderadoService apoderadoService) {
        this.apoderadoService = apoderadoService;
    }

    @GetMapping("/{dni}")
    public ResponseEntity<ApoderadoResponseDto> buscarPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(apoderadoService.buscarPorDni(dni));
    }

}
