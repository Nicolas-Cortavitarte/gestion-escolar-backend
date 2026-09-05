package com.colegio.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.api.models.ResumenFinalEstudiante;

public interface ResumenFinalEstudianteRepository extends JpaRepository<ResumenFinalEstudiante, UUID> {

    // Obtener el resumen final de un estudiante en un año lectivo
    Optional<ResumenFinalEstudiante> findByEstudianteIdAndAnioLectivo(UUID estudianteId, Integer anioLectivo);
}
