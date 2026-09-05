package com.colegio.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.api.models.ReporteConductaAsistencia;

public interface ReporteConductaAsistenciaRepository extends JpaRepository<ReporteConductaAsistencia, UUID> {

    // Obtener el reporte de conducta y asistencia de un alumno en un bimestre
    Optional<ReporteConductaAsistencia> findByEstudianteIdAndBimestre(UUID estudianteId, Integer bimestre);
}