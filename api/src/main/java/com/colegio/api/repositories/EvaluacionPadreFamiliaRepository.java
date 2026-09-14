package com.colegio.api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.api.models.EvaluacionPadreFamilia;

public interface EvaluacionPadreFamiliaRepository extends JpaRepository<EvaluacionPadreFamilia, UUID> {

    // Obtener la evaluación de un padre de familia
    List<EvaluacionPadreFamilia> findByEstudianteIdAndAnioLectivo(UUID estudianteId, Integer anioLectivo);

    // Obtener la evaluación de un padre de familia
    Optional<EvaluacionPadreFamilia> findByEstudianteId(UUID estudianteId);

    // Obtener la evaluación de un padre de familia
    Optional<EvaluacionPadreFamilia> findByEstudianteIdAndBimestre(UUID estudianteId, Integer bimestre);

}
