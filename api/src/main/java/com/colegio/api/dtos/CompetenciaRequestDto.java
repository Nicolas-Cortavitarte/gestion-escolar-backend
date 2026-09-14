package com.colegio.api.dtos;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CompetenciaRequestDto {

    @NotNull(message = "El curso es requerido")
    private UUID cursoId;

    @NotNull(message = "El nombre de la competencia es requerido")
    @Size(max = 500, message = "El nombre de la competencia debe tener al menos 1 caracter y maximo 50")
    private String nombreCompetencia;

}
