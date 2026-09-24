package com.colegio.api.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.colegio.api.models.CategoriaMovimiento;
import com.colegio.api.models.TipoMovimiento;

import lombok.Data;

@Data
public class MovimientoFinancieroResponseDto {

    private UUID id;
    private TipoMovimiento tipo;
    private CategoriaMovimiento categoria;
    private String concepto;
    private BigDecimal monto;
    private LocalDate fecha;
    private String descripcion;
    private UUID registradoPorId;
    private String registradoPorEmail;
    private OffsetDateTime creadoEn;

}