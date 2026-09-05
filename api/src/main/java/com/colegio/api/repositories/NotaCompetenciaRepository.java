package com.colegio.api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.api.models.NotaCompetencia;

public interface NotaCompetenciaRepository extends JpaRepository<NotaCompetencia, UUID> {

    // Notas de un estudiante por bimestre
    List<NotaCompetencia> findByEstudianteIdAndBimestre(UUID estudianteId, Integer bimestre);

    // Buscar nota por estudiante, competencia y bimestre
    Optional<NotaCompetencia> findByEstudianteIdAndCompetenciaIdAndBimestre(UUID estudianteId, UUID competenciaId,
            Integer bimestre);
}
