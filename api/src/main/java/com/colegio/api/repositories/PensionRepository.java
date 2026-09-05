package com.colegio.api.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.api.models.EstadoPension;
import com.colegio.api.models.Pension;

public interface PensionRepository extends JpaRepository<Pension, UUID> {

    // Obtener pensiones de una matrícula
    List<Pension> findByMatriculaId(UUID matriculaId);

    // Obtener pensiones de una matrícula con estado específico
    List<Pension> findByMatriculaIdAndEstado(UUID matriculaId, EstadoPension estado);
}
