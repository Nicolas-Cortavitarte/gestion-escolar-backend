package com.colegio.api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.api.models.Estudiante;

public interface EstudianteRepository extends JpaRepository<Estudiante, UUID> {

    // Buscar estudiante por DNI
    Optional<Estudiante> findByDni(String dni);

    // Validar que existe DNI
    boolean existsByDni(String dni);

    // Listar estudiante por apoderado
    List<Estudiante> findByApoderadoId(UUID apoderadoId);
}
