package com.colegio.api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.api.models.NotaArea;

public interface NotaAreaRepository extends JpaRepository<NotaArea, UUID> {

    // Obtener las notas de un alumno
    List<NotaArea> findByEstudianteId(UUID estudiandteId);

    // Obtener las notas de un alumno en un área y bimestre
    List<NotaArea> findByEstudianteIdAndBimestre(UUID estudianteId, Integer bimestre);

    // Obtener las notas de un alumno en un área y bimestre
    Optional<NotaArea> findByEstudianteIdAndCursoIdAndBimestre(UUID estudianteId, UUID cursoId, Integer bimestre);

}
