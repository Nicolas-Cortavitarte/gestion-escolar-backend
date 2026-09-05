package com.colegio.api.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.colegio.api.models.Competencia;

public interface CompetenciaRepository extends JpaRepository<Competencia, UUID> {

    // Lista de competencias por curso
    List<Competencia> findByCursoId(UUID cursoId);
}
