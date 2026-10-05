package com.colegio.api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.api.models.EvaluacionPadreFamilia;

public interface EvaluacionPadreFamiliaRepository
        extends JpaRepository<EvaluacionPadreFamilia, UUID> {

    // Obtener todas las evaluaciones de un padre de familia
    List<EvaluacionPadreFamilia> findByEstudianteId(UUID estudianteId);

    // Obtener todas las evaluaciones de un padre de familia en un año lectivo
    List<EvaluacionPadreFamilia> findByEstudianteIdAndAnioLectivo(
            UUID estudianteId, Integer anioLectivo);

    // Obtener la evaluación de un padre de familia en un año lectivo y un bimestre
    Optional<EvaluacionPadreFamilia> findByEstudianteIdAndAnioLectivoAndBimestre(
            UUID estudianteId, Integer anioLectivo, Integer bimestre);
}