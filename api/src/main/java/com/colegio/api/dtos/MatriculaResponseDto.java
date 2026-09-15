package com.colegio.api.dtos;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import lombok.Data;

@Data
public class MatriculaResponseDto {

    private UUID estudianteId;
    private Integer anioLectivo;
    private String nombreEstudiante;
    private String nivel;
    private String grado;
    private BigDecimal montoMatricula;
    private BigDecimal montoPensionMensual;
    private Integer fechaVencimiento;
    private OffsetDateTime fechaRegistro;
    private Boolean matriculaPagada;
    private OffsetDateTime fechaPagoMatricula;

}
