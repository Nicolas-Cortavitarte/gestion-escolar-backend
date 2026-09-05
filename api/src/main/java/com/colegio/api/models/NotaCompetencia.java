package com.colegio.api.models;

import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notas_competencias", uniqueConstraints = {
        @UniqueConstraint(name = "uq_estudiante_competencia_bimestre", columnNames = { "estudiante_id",
                "competencia_id", "bimestre" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaCompetencia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "competencia_id", nullable = false)
    private Competencia competencia;

    @Column(name = "bimestre", nullable = false)
    private Integer bimestre;

    @Enumerated(EnumType.STRING)
    @Column(name = "calificativo", nullable = false)
    private NotaCualitativa calificativo;

    @Column(name = "actualizado_en", updatable = false)
    private OffsetDateTime actualizadoEn;

    @PrePersist
    @PreUpdate
    protected void onSaveOrUpdate() {
        this.actualizadoEn = OffsetDateTime.now();
    }
}
