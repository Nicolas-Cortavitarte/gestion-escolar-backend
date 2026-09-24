package com.colegio.api.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class ReporteFinancieroResponseDto {

    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private BigDecimal totalIngresosPensiones;
    private BigDecimal totalIngresosMatriculas;
    private BigDecimal totalIngresosMovimientos;
    private BigDecimal totalIngresos;
    private BigDecimal totalEgresosPagosDocentes;
    private BigDecimal totalEgresosMovimientos;
    private BigDecimal totalEgresos;
    private BigDecimal balance;
    private List<MovimientoFinancieroResponseDto> detalleMovimientos;
}
