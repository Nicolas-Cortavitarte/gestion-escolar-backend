package com.colegio.api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.api.models.ReporteConductaAsistencia;

public interface ReporteConductaAsistenciaRepository
        extends JpaRepository<ReporteConductaAsistencia, UUID> {

    // Obtener todos los reportes de conducta y asistencia de un alumno
    List<ReporteConductaAsistencia> findByEstudianteId(UUID estudianteId);

    // Obtener todos los reportes de conducta y asistencia de un alumno en un año
    // lectivo
    List<ReporteConductaAsistencia> findByEstudianteIdAndAnioLectivo(
            UUID estudianteId, Integer anioLectivo);

    // Obtener el reporte de conducta y asistencia de un alumno en un año lectivo y
    Optional<ReporteConductaAsistencia> findByEstudianteIdAndAnioLectivoAndBimestre(
            UUID estudianteId, Integer anioLectivo, Integer bimestre);
}