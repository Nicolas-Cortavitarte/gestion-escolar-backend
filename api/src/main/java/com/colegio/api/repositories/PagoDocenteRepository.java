package com.colegio.api.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.colegio.api.models.PagoDocente;

public interface PagoDocenteRepository extends JpaRepository<PagoDocente, UUID> {

    // Lista de pagos por docente
    List<PagoDocente> findByDocenteId(UUID docenteId);

    // Lista de pagos por mes y año
    List<PagoDocente> findByMesAndAnio(Integer mes, Integer anio);

    // Lista de pagos por docente y estado
    List<PagoDocente> findByDocenteIdAndEstado(UUID docenteId, String estado);
}
