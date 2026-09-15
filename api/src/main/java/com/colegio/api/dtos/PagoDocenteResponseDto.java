package com.colegio.api.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.colegio.api.models.EstadoPagoDocente;

import lombok.Data;

@Data
public class PagoDocenteResponseDto {

    private UUID id;
    private UUID docenteId;
    private String nombresDocente;
    private Integer mes;
    private Integer anio;
    private BigDecimal monto;
    private LocalDate fechaProgramada;
    private OffsetDateTime fechaPago;
    private EstadoPagoDocente estado;

}
