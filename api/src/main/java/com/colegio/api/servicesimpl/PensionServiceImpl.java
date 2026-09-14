package com.colegio.api.servicesimpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.colegio.api.dtos.PensionResponseDto;
import com.colegio.api.mappers.PensionMapper;
import com.colegio.api.models.EstadoPension;
import com.colegio.api.models.Matricula;
import com.colegio.api.models.Pension;
import com.colegio.api.repositories.PensionRepository;
import com.colegio.api.services.PensionService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PensionServiceImpl implements PensionService {

    private final PensionRepository pensionRepository;
    private final PensionMapper pensionMapper;

    public PensionServiceImpl(PensionRepository pensionRepository, PensionMapper pensionMapper) {
        this.pensionRepository = pensionRepository;
        this.pensionMapper = pensionMapper;
    }

    @Override
    public List<PensionResponseDto> obtenerTodos() {
        return pensionRepository.findAll()
                .stream()
                .map(pensionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PensionResponseDto obtenerPorId(UUID id) {
        Pension pension = pensionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pension no encontrada"));
        return pensionMapper.toDto(pension);
    }

    @Override
    public List<PensionResponseDto> obtenerPorMatricula(UUID id) {
        return pensionRepository.findByMatriculaId(id)
                .stream()
                .map(pensionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void generarPensionesDelAnio(Matricula matricula) {
        List<Pension> pensiones = new ArrayList<>();

        final int MES_INICIO = 3;
        final int MES_FIN = 12;

        for (int mes = MES_INICIO; mes <= MES_FIN; mes++) {
            Pension pension = new Pension();
            pension.setMatricula(matricula);
            pension.setMes(mes);
            pension.setMontoBase(matricula.getMontoPensionMensual());
            pension.setMoraAcumulada(BigDecimal.ZERO);

            int diaSeguro = Math.min(matricula.getDiaVencimientoPension(),
                    LocalDate.of(matricula.getAnioLectivo(), mes, 1).lengthOfMonth());

            pension.setFechaVencimiento(LocalDate.of(matricula.getAnioLectivo(), mes, diaSeguro));
            pension.setEstado(EstadoPension.PENDIENTE);

            pensiones.add(pension);
        }

        pensionRepository.saveAll(pensiones);
    }

    @Override
    public PensionResponseDto marcarComoPagado(UUID id, OffsetDateTime fechaPago) {
        Pension pension = pensionRepository.findAllById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pension no encontrada"));

        if (pension.getEstado() == EstadoPension.PAGADO) {
            throw new IllegalStateException("La pension ya se encuentra pagada");
        }

        pension.setEstado(EstadoPension.PAGADO);
        pension.setFechaPago(fechaPago != null ? fechaPago : OffsetDateTime.now());

        return pensionMapper.toDto(pensionRepository.save(pension));
    }

    @Override
    public PensionResponseDto actualizarMora(UUID id, BigDecimal nuevaMora) {
        Pension pension = pensionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pension no encontrada"));

        if (pension.getEstado() == EstadoPension.PAGADO) {
            throw new IllegalStateException("No se puede modificar la mora de una pensión ya pagada");
        }

        pension.setMoraAcumulada(nuevaMora);

        if (nuevaMora.compareTo(BigDecimal.ZERO) > 0) {
            pension.setEstado(EstadoPension.EN_MORA);
        }

        return pensionMapper.toDto(pensionRepository.save(pension));
    }
}
