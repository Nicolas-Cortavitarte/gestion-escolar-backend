package com.colegio.api.models;

import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "evaluacion_padre_familia", uniqueConstraints = {
        @UniqueConstraint(name = "uq_estudiante_anio_bimestre_padre", columnNames = { "estudiante_id", "anio_lectivo",
                "bimestre" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EvaluacionPadreFamilia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @Column(name = "bimestre", nullable = false)
    private Integer bimestre;

    @Enumerated(EnumType.STRING)
    @Column(name = "envia_puntualmente_hijo")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private NotaCualitativa enviaPuntualmenteHijo;

    @Enumerated(EnumType.STRING)
    @Column(name = "apoya_tareas_casa")
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private NotaCualitativa apoyaTareasCasa;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "envia_hijo_uniformado")
    private NotaCualitativa enviaHijoUniformado;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "asiste_reuniones_colegio")
    private NotaCualitativa asisteReunionesColegio;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "cumple_pagos_institucion")
    private NotaCualitativa cumplePagosInstitucion;

    @Column(name = "anio_lectivo", nullable = false)
    private Integer anioLectivo;
}
