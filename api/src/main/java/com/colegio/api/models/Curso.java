package com.colegio.api.models;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cursos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "nivel", nullable = false, length = 20)
    private String nivel;

    @Column(name = "grado", nullable = false, length = 50)
    private String grado;

    @Column(name = "anio_lectivo", nullable = false)
    private Integer anioLectivo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "docente_id", nullable = true)
    private Docente docente;
}