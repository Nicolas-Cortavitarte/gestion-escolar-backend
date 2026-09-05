package com.colegio.api.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "apoderados")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Apoderado {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "dni", nullable = false, unique = true, length = 8)
    private String dni;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", nullable = false, length = 100)
    private String apellidos;

    @Column(name = "telefono", length = 15)
    private String telefono;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "parentesco", length = 50)
    private String parentesco;

    @Column(name = "creado_en", updatable = false)
    private OffsetDateTime creadoEn;

    @PrePersist
    protected void onCreate() {
        this.creadoEn = OffsetDateTime.now();
    }
}