package com.colegio.api.models;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "competencias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Competencia {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "curso_id", nullable = false)
    private Curso curso;

    @Column(name = "nombre_competencia", nullable = false)
    private String nombreCompetencia;
}
