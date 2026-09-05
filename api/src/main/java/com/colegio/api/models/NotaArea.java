package com.colegio.api.models;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "notas_areas", uniqueConstraints = {
        @UniqueConstraint(name = "uq_estudiante_curso_bimestre", columnNames = {
                "estudiante_id", "curso_id", "bimestre" }) })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotaArea {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(name = "bimestre", nullable = false)
    private Integer bimestre;

    @Enumerated(EnumType.STRING)
    @Column(name = "calificativo_area", nullable = false)
    private NotaCualitativa calificativoArea;
}
