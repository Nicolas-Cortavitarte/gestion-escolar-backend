package com.colegio.api.models;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "matriculas", uniqueConstraints = {
        @UniqueConstraint(name = "uq_estudiante_anio", columnNames = { "estudiante_id", "anio_lectivo" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Matricula {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @Column(name = "anio_lectivo", nullable = false)
    private Integer anioLectivo;

    @Column(name = "nivel", nullable = false, length = 20)
    private String nivel;

    @Column(name = "grado", nullable = false, length = 50)
    private String grado;

    @Column(name = "monto_matricula", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoMatricula;

    @Column(name = "monto_pension_mensual", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoPensionMensual;

    @Builder.Default
    @Column(name = "dia_vencimiento_pension")
    private Integer diaVencimientoPension = 1;

    @Column(name = "fecha_registro", updatable = false)
    private OffsetDateTime fechaRegistro;

    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = OffsetDateTime.now();
    }

    @Builder.Default
    @Column(name = "matricula_pagada", nullable = false)
    private Boolean matriculaPagada = false;

    @Column(name = "fecha_pago_matricula")
    private OffsetDateTime fechaPagoMatricula;
}