package com.colegio.api.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PagoDocenteRequestDto {

    @NotNull(message = "El docente es obligatorio")
    private UUID docenteId;

    @NotNull(message = "El mes es obligatorio")
    @Min(value = 3, message = "El mes debe estar entre 3 y 12")
    @Max(value = 12, message = "El mes debe estar entre 3 y 12")
    private Integer mes;

    @NotNull(message = "El año es obligatorio")
    private Integer anio;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.00", inclusive = false, message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @NotNull(message = "La fecha programada es obligatoria")
    private LocalDate fechaProgramada;
}
