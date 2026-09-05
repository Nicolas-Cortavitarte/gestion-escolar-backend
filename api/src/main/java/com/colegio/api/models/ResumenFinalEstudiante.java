package com.colegio.api.models;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resumen_final_estudiante", uniqueConstraints = {
        @UniqueConstraint(name = "uq_estudiante_anio_resumen", columnNames = { "estudiante_id", "anio_lectivo" }) })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResumenFinalEstudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @Column(name = "anio_lectivo", nullable = false)
    private Integer anioLectivo;

    @Enumerated(EnumType.STRING)
    @Column(name = "situacion_final", nullable = false)
    private SituacionFinal situacionFinal;

    @Column(name = "area_a_recuperar", columnDefinition = "TEXT")
    private String areaARecuperar;

    @Column(name = "creado_en", updatable = false)
    private OffsetDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = OffsetDateTime.now();
    }
}
