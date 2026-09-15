package com.colegio.api.servicesimpl;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.colegio.api.dtos.PagoDocenteResponseDto;
import com.colegio.api.mappers.PagoDocenteMapper;
import com.colegio.api.models.Docente;
import com.colegio.api.models.EstadoPagoDocente;
import com.colegio.api.models.PagoDocente;
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

    public PagoDocenteServiceImpl(
            PagoDocenteRepository pagoDocenteRepository,
            PagoDocenteMapper pagoDocenteMapper) {
        this.pagoDocenteRepository = pagoDocenteRepository;
        this.pagoDocenteMapper = pagoDocenteMapper;
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
    public void genererPagoDelAnio(Docente docente, Integer anio) {
        List<PagoDocente> pagos = new ArrayList<>();

        for (int mes = MES_INICIO; mes <= MES_FIN; mes++) {
            PagoDocente pago = new PagoDocente();
            pago.setDocente(docente);
            pago.setMes(mes);
            pago.setAnio(anio);
            pago.setMonto(docente.getSueldoMensual());

            int diaSeguro = Math.min(DIA_PAGO,
                    LocalDate.of(anio, mes, 1).lengthOfMonth());
            pago.setFechaProgramada(LocalDate.of(anio, mes, diaSeguro));

            pago.setEstado(EstadoPagoDocente.PROGRAMADO);

            pagos.add(pago);
        }

        pagoDocenteRepository.saveAll(pagos);
    }

    @Override
    public PagoDocenteResponseDto marcarComoPagado(UUID id, OffsetDateTime fechaPago) {
        PagoDocente pago = pagoDocenteRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Pago no encontrado"));

        if (pago.getEstado() == EstadoPagoDocente.PAGADO) {
            throw new IllegalStateException("El pago ya fue marcado como pagado");
        }

        pago.setEstado(EstadoPagoDocente.PAGADO);
        pago.setFechaPago(fechaPago != null ? fechaPago : OffsetDateTime.now());

        return pagoDocenteMapper.toDto(pagoDocenteRepository.save(pago));
    }
}
