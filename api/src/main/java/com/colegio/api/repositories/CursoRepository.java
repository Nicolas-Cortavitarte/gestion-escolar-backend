package com.colegio.api.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.colegio.api.models.Curso;

public interface CursoRepository extends JpaRepository<Curso, UUID> {

    // Lista de cursos por anio lectivo, nivel y grado
    List<Curso> findByAnioLectivoAndNivel(Integer anioLectivo, String nivel);

    // Verificar si un curso existe por nombre
    boolean existsByNombre(String nombre);

    // Lista de cursos por docente
    List<Curso> findByDocenteId(UUID docenteId);

    // Verificar si un docente tiene cursos
    boolean existsByDocenteId(UUID docenteId);

    // Obtener cursos por año lectivo, nivel y grado
    List<Curso> findByAnioLectivoAndNivelAndGrado(Integer anioLectivo, String nivel, String grado);

    // Verificar si un curso existe por docente, año lectivo, nivel y grado
    boolean existsByDocente_Usuario_IdAndAnioLectivoAndNivelAndGrado(
            UUID usuarioId, Integer anioLectivo, String nivel, String grado);
}
