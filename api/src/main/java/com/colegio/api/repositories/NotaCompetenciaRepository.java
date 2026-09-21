package com.colegio.api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.api.models.NotaCompetencia;

public interface NotaCompetenciaRepository extends JpaRepository<NotaCompetencia, UUID> {

        // Buscar por Estudiante
        List<NotaCompetencia> findByEstudianteId(UUID estudianteId);

        // Notas de un estudiante por bimestre
        List<NotaCompetencia> findByEstudianteIdAndBimestre(UUID estudianteId, Integer bimestre);

        // Notas de un estudiante por competencia
        List<NotaCompetencia> findByEstudianteIdAndCompetenciaId(UUID estudianteId, UUID competenciaId);

        // Buscar nota por estudiante, competencia y bimestre
        Optional<NotaCompetencia> findByEstudianteIdAndCompetenciaIdAndBimestre(UUID estudianteId, UUID competenciaId,
                        Integer bimestre);

        // Buscar notas de un estudiante por competencia en varios bimestres
        List<NotaCompetencia> findByEstudianteIdAndCompetenciaIdAndBimestreIn(UUID estudianteId, UUID competenciaId,
                        List<Integer> bimestres);

        // Buscar notas de un estudiante por area y bimestre
        List<NotaCompetencia> findByEstudianteIdAndCompetencia_CursoIdAndBimestre(UUID estudianteId, UUID cursoId,
                        Integer bimestre);
}
