package com.colegio.api.repositories;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.api.models.MovimientoFinanciero;
import com.colegio.api.models.TipoMovimiento;

public interface MovimientoFinancieroRepository extends JpaRepository<MovimientoFinanciero, UUID> {

    List<MovimientoFinanciero> findByFechaBetween(LocalDate fechaInicio, LocalDate fechaFin);

    List<MovimientoFinanciero> findByTipoAndFechaBetween(TipoMovimiento tipo, LocalDate fechaInicio,
            LocalDate fechaFin);
}