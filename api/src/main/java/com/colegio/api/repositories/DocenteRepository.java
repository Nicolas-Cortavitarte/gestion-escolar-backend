package com.colegio.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.colegio.api.models.Docente;

public interface DocenteRepository extends JpaRepository<Docente, UUID> {

    // Buscar Docentes por DNI
    Optional<Docente> findByDni(String dni);

    // Buscar Docente por ID
    Optional<Docente> findById(UUID usuarioUuid);

    // Verificar si existe un Docente por email
    boolean existsByEmail(String email);

    // Verificar si existe un Docente por DNI
    boolean existsByDni(String dni);
}
