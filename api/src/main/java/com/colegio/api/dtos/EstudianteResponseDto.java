package com.colegio.api.dtos;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class EstudianteResponseDto {

    private UUID id;
    private String dni;
    private String nombres;
    private String apellidos;
    private LocalDate fechaNacimiento;
    private String direccion;
    private UUID apoderadoId;
    private String nombreApoderado;
    private OffsetDateTime creadoEn;

}
