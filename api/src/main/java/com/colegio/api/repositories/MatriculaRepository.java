package com.colegio.api.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.api.models.Matricula;

public interface MatriculaRepository extends JpaRepository<Matricula, UUID> {

    // Listar matrículas por estudiante
    List<Matricula> findByEstudianteId(UUID estudianteId);

    // Verificar matrícula vigente
    Optional<Matricula> findByEstudianteAndAnioLectivo(UUID estudianteId, Integer anioLectivo);

    // Obtener alumnos de un grado específico en un año lectivo
    List<Matricula> findByAnioLectivoAndNivelAndGrado(Integer anioLectivo, String nivel, String grado);
}
