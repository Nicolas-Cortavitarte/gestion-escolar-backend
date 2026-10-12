package com.colegio.api.servicesimpl;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.colegio.api.dtos.PagoDocenteResponseDto;
import com.colegio.api.mappers.PagoDocenteMapper;
import com.colegio.api.models.Docente;
import com.colegio.api.models.EstadoPagoDocente;
import com.colegio.api.models.PagoDocente;
import com.colegio.api.repositories.DocenteRepository;
import com.colegio.api.repositories.PagoDocenteRepository;
import com.colegio.api.services.PagoDocenteService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PagoDocenteServiceImpl implements PagoDocenteService {

    private static final int MES_INICIO = 3;
    private static final int MES_FIN = 12;
    private static final int DIA_PAGO = 30;

    private final PagoDocenteRepository pagoDocenteRepository;
    private final PagoDocenteMapper pagoDocenteMapper;
    private final DocenteRepository docenteRepository;

    public PagoDocenteServiceImpl(
            PagoDocenteRepository pagoDocenteRepository,
            PagoDocenteMapper pagoDocenteMapper,
            DocenteRepository docenteRepository) {
        this.pagoDocenteRepository = pagoDocenteRepository;
        this.pagoDocenteMapper = pagoDocenteMapper;
        this.docenteRepository = docenteRepository;
    }

    @Override
    public List<PagoDocenteResponseDto> obtenerTodos() {
        return pagoDocenteRepository.findAll().stream()
                .map(pagoDocenteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PagoDocenteResponseDto obtenerPorId(UUID id) {
        PagoDocente pagoDocente = pagoDocenteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Pago no encontrado"));
        return pagoDocenteMapper.toDto(pagoDocente);
    }

    @Override
    public List<PagoDocenteResponseDto> obtenerPorDocente(UUID id) {
        return pagoDocenteRepository.findByDocenteId(id).stream()
                .map(pagoDocenteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void generarPagosDelAnio(Docente docente, Integer anio) {
        if (anio == null || anio < 1 || anio > 9999) {
            throw new IllegalArgumentException("El año no es válido.");
        }

        Docente actualizado = docenteRepository
                .buscarPorIdParaActualizar(docente.getId())
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado"));

        if (actualizado.getUsuario() == null
                || !Boolean.TRUE.equals(
                        actualizado.getUsuario().getActivo())) {

            throw new IllegalStateException(
                    "No se pueden generar pagos para un docente inactivo.");
        }

        Set<Integer> mesesExistentes = pagoDocenteRepository
                .findByDocenteId(actualizado.getId())
                .stream()
                .filter(pago -> anio.equals(pago.getAnio()))
                .map(PagoDocente::getMes)
                .collect(Collectors.toSet());

        List<PagoDocente> nuevos = new ArrayList<>();

        for (int mes = MES_INICIO; mes <= MES_FIN; mes++) {
            if (mesesExistentes.contains(mes)) {
                continue;
            }

            PagoDocente pago = new PagoDocente();
            pago.setDocente(actualizado);
            pago.setMes(mes);
            pago.setAnio(anio);
            pago.setMonto(actualizado.getSueldoMensual());

            LocalDate inicioMes = LocalDate.of(anio, mes, 1);
            int diaSeguro = Math.min(
                    DIA_PAGO, inicioMes.lengthOfMonth());

            pago.setFechaProgramada(inicioMes.withDayOfMonth(diaSeguro));
            pago.setEstado(EstadoPagoDocente.PROGRAMADO);

            nuevos.add(pago);
        }

        pagoDocenteRepository.saveAll(nuevos);
    }

    @Override
    public PagoDocenteResponseDto marcarComoPagado(UUID id, OffsetDateTime fechaPago) {
        PagoDocente pago = pagoDocenteRepository.buscarPorIdParaActualizar(id).orElseThrow(
                () -> new EntityNotFoundException("Pago no encontrado"));

        if (pago.getEstado() == EstadoPagoDocente.PAGADO) {
            throw new IllegalStateException("El pago ya fue marcado como pagado");
        }

        pago.setEstado(EstadoPagoDocente.PAGADO);
        pago.setFechaPago(fechaPago != null ? fechaPago : OffsetDateTime.now());

        return pagoDocenteMapper.toDto(pagoDocenteRepository.save(pago));
    }

    @Override
    public void generarPagosAnuales() {
        int anio = LocalDate.now(ZoneId.of("America/Lima")).getYear();

        List<Docente> activos = docenteRepository.findAll()
                .stream()
                .filter(docente -> docente.getUsuario() != null
                        && Boolean.TRUE.equals(
                                docente.getUsuario().getActivo()))
                .sorted(Comparator.comparing(Docente::getId))
                .collect(Collectors.toList());

        for (Docente docente : activos) {
            generarPagosDelAnio(docente, anio);
        }
    }

}
