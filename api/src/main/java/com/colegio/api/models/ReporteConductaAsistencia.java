package com.colegio.api.models;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reporte_conducta_asistencia", uniqueConstraints = {
        @UniqueConstraint(name = "uq_estudiante_bimestre_reporte", columnNames = { "estudiante_id", "bimestre" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReporteConductaAsistencia {

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
    @Column(name = "conducta_puntualidad_respeto")
    private NotaCualitativa conductaPuntualidadRespeto;

    @Enumerated(EnumType.STRING)
    @Column(name = "conducta_actitud_aula")
    private NotaCualitativa conductaActitudAula;

    @Enumerated(EnumType.STRING)
    @Column(name = "conducta_presentacion_aseo")
    private NotaCualitativa conductaPresentacionAseo;

    @Builder.Default
    @Column(name = "inasistencias_justificadas")
    private Integer inasistenciasJustificadas = 0;

    @Builder.Default
    @Column(name = "inasistencias_injustificadas")
    private Integer inasistenciasInjustificadas = 0;

    @Builder.Default
    @Column(name = "tardanzas_justificadas")
    private Integer tardanzasJustificadas = 0;

    @Builder.Default
    @Column(name = "tardanzas_injustificadas")
    private Integer tardanzasInjustificadas = 0;

    @Column(name = "apreciacion_tutor", columnDefinition = "TEXT")
    private String apreciacionTutor;

    @Column(name = "anio_lectivo", nullable = false)
    private Integer anioLectivo;
}