package com.colegio.api.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.colegio.api.models.EstadoPension;

import lombok.Data;

@Data
public class PensionResponseDto {

    private UUID id;
    private UUID matriculaId;
    private Integer mes;
    private BigDecimal montoBase;
    private BigDecimal moraAcumulada;
    private BigDecimal montoTotal;
    private LocalDate fechaVencimiento;
    private OffsetDateTime fechaPago;
    private EstadoPension estado;

}
