package com.colegio.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.api.models.EvaluacionPadreFamilia;

public interface EvaluacionPadreFamiliaRepository extends JpaRepository<EvaluacionPadreFamilia, UUID> {

    // Obtener la evaluación de un padre de familia
    Optional<EvaluacionPadreFamilia> findByEstudianteIdAndBimestre(UUID estudianteId, Integer bimestre);

}
