package com.colegio.api.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import lombok.Data;

@Data
public class DocenteResponseDto {

    private UUID id;
    private UUID usuarioId;
    private String dni;
    private String nombres;
    private String apellidos;
    private BigDecimal sueldoMensual;
    private boolean activo;

}
