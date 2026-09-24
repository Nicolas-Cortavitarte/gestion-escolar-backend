package com.colegio.api.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.colegio.api.models.CategoriaMovimiento;
import com.colegio.api.models.TipoMovimiento;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class MovimientoFinancieroRequestDto {

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private TipoMovimiento tipo;

    @NotNull(message = "La categoría es obligatoria")
    private CategoriaMovimiento categoria;

    @NotBlank(message = "El concepto es obligatorio")
    @Size(max = 255, message = "El concepto no puede exceder 255 caracteres")
    private String concepto;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.00", inclusive = false, message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String descripcion;

}
