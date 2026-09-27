package com.colegio.api.dtos;

import java.math.BigDecimal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InscripcionRequestDto {
    @Valid
    @NotNull(message = "Los datos del estudiante son obligatorios")
    private EstudianteRequestDto estudiante;

    @NotNull(message = "El año lectivo es obligatorio")
    private Integer anioLectivo;

    @NotBlank(message = "El nivel es obligatorio")
    private String nivel;

    @NotBlank(message = "El grado es obligatorio")
    private String grado;

    @NotNull(message = "El monto de la matrícula es obligatorio")
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal montoMatricula;

    @NotNull(message = "El monto de la pensión mensual es obligatorio")
    @DecimalMin(value = "0.00", inclusive = false)
    private BigDecimal montoPensionMensual;

    @NotNull(message = "El día de vencimiento es obligatorio")
    @Min(1)
    @Max(31)
    private Integer fechaVencimiento;
}
