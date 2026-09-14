package com.colegio.api.dtos;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MatriculaRequestDto {

    @NotNull(message = "El ID del estudiante es requerido")
    private UUID estudianteId;

    @NotNull(message = "El año lectivo es requerido")
    private Integer anioLectivo;

    @NotBlank(message = "El nivel es requerido")
    private String nivel;

    @NotBlank(message = "El grado es requerido")
    private String grado;

    @NotNull(message = "El monto de la matrícula es requerido")
    @DecimalMin(value = "0.00", inclusive = false, message = "El monto de la matrícula debe ser mayor a 0")
    private BigDecimal montoMatricula;

    @NotNull(message = "El monto de la pensión mensual es requerido")
    @DecimalMin(value = "0.00", inclusive = false, message = "El monto de la pensión mensual debe ser mayor a 0")
    private BigDecimal montoPensionMensual;

    @NotNull(message = "El día de vencimiento debe ser mayor o igual a 1")
    @Min(value = 1, message = "El día de vencimiento debe ser mayor o igual a 1")
    @Max(value = 31, message = "El día de vencimiento debe ser menor o igual a 31")
    private Integer fechaVencimiento;

}
