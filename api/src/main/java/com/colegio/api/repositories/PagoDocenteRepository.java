package com.colegio.api.repositories;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import com.colegio.api.models.PagoDocente;
import com.colegio.api.models.EstadoPagoDocente;

public interface PagoDocenteRepository extends JpaRepository<PagoDocente, UUID> {

    // Lista de pagos por docente
    List<PagoDocente> findByDocenteId(UUID docenteId);

    // Lista de pagos por mes y año
    List<PagoDocente> findByMesAndAnio(Integer mes, Integer anio);

    // Lista de pagos por docente y estado
    List<PagoDocente> findByDocenteIdAndEstado(UUID docenteId, String estado);

    // Lista de pagos por estado y rango de fechas de pago
    List<PagoDocente> findByEstadoAndFechaPagoBetween(EstadoPagoDocente estado, OffsetDateTime fechaInicio,
            OffsetDateTime fechaFin);
}
