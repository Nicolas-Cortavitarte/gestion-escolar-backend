package com.colegio.api.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.colegio.api.models.Curso;

public interface CursoRepository extends JpaRepository<Curso, UUID> {

    // Lista de cursos por anio lectivo, nivel y grado
    List<Curso> findByAnioLectivoAndNivel(Integer anioLectivo, String nivel);

    // Lista de cursos por docente
    List<Curso> findByDocenteId(UUID docenteId);
}
