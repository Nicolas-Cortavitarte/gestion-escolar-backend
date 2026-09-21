package com.colegio.api.models;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "evaluacion_padre_familia", uniqueConstraints = {
        @UniqueConstraint(name = "uq_estudiante_bimestre_padre", columnNames = { "estudiante_id", "bimestre" }) })
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
    private NotaCualitativa enviaPuntualmenteHijo;

    @Enumerated(EnumType.STRING)
    @Column(name = "apoya_tareas_casa")
    private NotaCualitativa apoyaTareasCasa;

    @Enumerated(EnumType.STRING)
    @Column(name = "envia_hijo_uniformado")
    private NotaCualitativa enviaHijoUniformado;

    @Enumerated(EnumType.STRING)
    @Column(name = "asiste_reuniones_colegio")
    private NotaCualitativa asisteReunionesColegio;

    @Enumerated(EnumType.STRING)
    @Column(name = "cumple_pagos_institucion")
    private NotaCualitativa cumplePagosInstitucion;

    @Column(name = "anio_lectivo", nullable = false)
    private Integer anioLectivo;
}
