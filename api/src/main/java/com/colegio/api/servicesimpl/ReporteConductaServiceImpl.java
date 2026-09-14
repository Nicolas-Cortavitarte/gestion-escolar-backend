package com.colegio.api.servicesimpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.colegio.api.dtos.ReporteConductaRequestDto;
import com.colegio.api.dtos.ReporteConductaResponseDto;
import com.colegio.api.mappers.ReporteConductaMapper;
import com.colegio.api.models.Estudiante;
import com.colegio.api.models.ReporteConductaAsistencia;
import com.colegio.api.repositories.EstudianteRepository;
import com.colegio.api.repositories.ReporteConductaAsistenciaRepository;
import com.colegio.api.services.ReporteConductaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ReporteConductaServiceImpl implements ReporteConductaService {

    private final ReporteConductaAsistenciaRepository reporteConductaRepository;
    private final EstudianteRepository estudianteRepository;
    private final ReporteConductaMapper reporteConductaMapper;

    public ReporteConductaServiceImpl(ReporteConductaAsistenciaRepository reporteConductaRepository,
            EstudianteRepository estudianteRepository,
            ReporteConductaMapper reporteConductaMapper) {
        this.reporteConductaRepository = reporteConductaRepository;
        this.estudianteRepository = estudianteRepository;
        this.reporteConductaMapper = reporteConductaMapper;
    }

    @Override
    public List<ReporteConductaResponseDto> obtenerPorEstudiante(UUID estudianteId) {
        return reporteConductaRepository.findByEstudianteId(estudianteId)
                .stream().map(reporteConductaMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public ReporteConductaResponseDto registrarOActualizar(UUID estudianteId, ReporteConductaRequestDto requestDto) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado con ID: " + estudianteId));

        ReporteConductaAsistencia reporte = reporteConductaRepository
                .findByEstudianteIdAndBimestre(estudianteId, requestDto.getBimestre())
                .orElseGet(() -> {
                    ReporteConductaAsistencia nuevo = new ReporteConductaAsistencia();
                    nuevo.setEstudiante(estudiante);
                    nuevo.setBimestre(requestDto.getBimestre());
                    return nuevo;
                });

        reporte.setConductaPuntualidadRespeto(requestDto.getConductaPuntualidadRespeto());
        reporte.setConductaActitudAula(requestDto.getConductaActitudAula());
        reporte.setConductaPresentacionAseo(requestDto.getConductaPresentacionAseo());
        reporte.setInasistenciasJustificadas(requestDto.getInasistenciasJustificadas());
        reporte.setInasistenciasInjustificadas(requestDto.getInasistenciasInjustificadas());
        reporte.setTardanzasJustificadas(requestDto.getTardanzasJustificadas());
        reporte.setTardanzasInjustificadas(requestDto.getTardanzasInjustificadas());
        reporte.setApreciacionTutor(requestDto.getApreciacionTutor());

        return reporteConductaMapper.toDto(reporteConductaRepository.save(reporte));
    }
}
