package com.colegio.api.servicesimpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.colegio.api.dtos.ReporteFinancieroResponseDto;
import com.colegio.api.mappers.MovimientoFinancieroMapper;
import com.colegio.api.models.Matricula;
import com.colegio.api.models.MovimientoFinanciero;
import com.colegio.api.models.PagoDocente;
import com.colegio.api.models.Pension;
import com.colegio.api.models.TipoMovimiento;
import com.colegio.api.models.EstadoPagoDocente;
import com.colegio.api.models.EstadoPension;
import com.colegio.api.repositories.MatriculaRepository;
import com.colegio.api.repositories.MovimientoFinancieroRepository;
import com.colegio.api.repositories.PagoDocenteRepository;
import com.colegio.api.repositories.PensionRepository;
import com.colegio.api.services.ReporteFinancieroService;

@Service
public class ReporteFinancieroServiceImpl implements ReporteFinancieroService {
    private final PensionRepository pensionRepository;
    private final MatriculaRepository matriculaRepository;
    private final PagoDocenteRepository pagoDocenteRepository;
    private final MovimientoFinancieroRepository movimientoRepository;
    private final MovimientoFinancieroMapper movimientoMapper;

    public ReporteFinancieroServiceImpl(PensionRepository pensionRepository,
            MatriculaRepository matriculaRepository,
            PagoDocenteRepository pagoDocenteRepository,
            MovimientoFinancieroRepository movimientoRepository,
            MovimientoFinancieroMapper movimientoMapper) {
        this.pensionRepository = pensionRepository;
        this.matriculaRepository = matriculaRepository;
        this.pagoDocenteRepository = pagoDocenteRepository;
        this.movimientoRepository = movimientoRepository;
        this.movimientoMapper = movimientoMapper;
    }

    @Override
    public ReporteFinancieroResponseDto generarReporte(LocalDate fechaInicio, LocalDate fechaFin) {
        OffsetDateTime inicioOffset = fechaInicio.atStartOfDay().atOffset(ZoneOffset.UTC);
        OffsetDateTime finOffset = fechaFin.atTime(23, 59, 59).atOffset(ZoneOffset.UTC);

        BigDecimal totalPensiones = pensionRepository
                .findByEstadoAndFechaPagoBetween(EstadoPension.PAGADO, inicioOffset, finOffset)
                .stream()
                .map(Pension::getMontoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalMatriculas = matriculaRepository
                .findByMatriculaPagadaTrueAndFechaPagoMatriculaBetween(inicioOffset, finOffset)
                .stream()
                .map(Matricula::getMontoMatricula)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPagosDocentes = pagoDocenteRepository
                .findByEstadoAndFechaPagoBetween(EstadoPagoDocente.PAGADO, inicioOffset, finOffset)
                .stream()
                .map(PagoDocente::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        List<MovimientoFinanciero> movimientos = movimientoRepository
                .findByFechaBetween(fechaInicio, fechaFin);

        BigDecimal totalMovimientosIngreso = movimientos.stream()
                .filter(m -> m.getTipo() == TipoMovimiento.INGRESO)
                .map(MovimientoFinanciero::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalMovimientosEgreso = movimientos.stream()
                .filter(m -> m.getTipo() == TipoMovimiento.EGRESO)
                .map(MovimientoFinanciero::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalIngresos = totalPensiones.add(totalMatriculas).add(totalMovimientosIngreso);
        BigDecimal totalEgresos = totalPagosDocentes.add(totalMovimientosEgreso);

        ReporteFinancieroResponseDto reporte = new ReporteFinancieroResponseDto();
        reporte.setFechaInicio(fechaInicio);
        reporte.setFechaFin(fechaFin);
        reporte.setTotalIngresosPensiones(totalPensiones);
        reporte.setTotalIngresosMatriculas(totalMatriculas);
        reporte.setTotalIngresosMovimientos(totalMovimientosIngreso);
        reporte.setTotalIngresos(totalIngresos);
        reporte.setTotalEgresosPagosDocentes(totalPagosDocentes);
        reporte.setTotalEgresosMovimientos(totalMovimientosEgreso);
        reporte.setTotalEgresos(totalEgresos);
        reporte.setBalance(totalIngresos.subtract(totalEgresos));
        reporte.setDetalleMovimientos(movimientos.stream().map(movimientoMapper::toDto).collect(Collectors.toList()));

        return reporte;
    }
}
