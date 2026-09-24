package com.colegio.api.repositories;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.api.models.EstadoPension;
import com.colegio.api.models.Pension;

public interface PensionRepository extends JpaRepository<Pension, UUID> {

    // Buscar pension por Id
    Optional<Pension> findAllById(UUID id);

    // Obtener pensiones de una matrícula
    List<Pension> findByMatriculaId(UUID matriculaId);

    // Obtener pensiones de una matrícula con estado específico
    List<Pension> findByMatriculaIdAndEstado(UUID matriculaId, EstadoPension estado);

    // Obtener pensiones por estado y rango de fechas de pago
    List<Pension> findByEstadoAndFechaPagoBetween(EstadoPension estado, OffsetDateTime fechaInicio,
            OffsetDateTime fechaFin);
}
