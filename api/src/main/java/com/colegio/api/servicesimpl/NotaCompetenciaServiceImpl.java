package com.colegio.api.servicesimpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.colegio.api.dtos.NotaCompetenciaRequestDto;
import com.colegio.api.dtos.NotaCompetenciaResponseDto;
import com.colegio.api.mappers.NotaCompetenciaMapper;
import com.colegio.api.models.Competencia;
import com.colegio.api.models.Estudiante;
import com.colegio.api.models.NotaCompetencia;
import com.colegio.api.repositories.CompetenciaRepository;
import com.colegio.api.repositories.EstudianteRepository;
import com.colegio.api.repositories.NotaCompetenciaRepository;
import com.colegio.api.services.NotaAreaCalculoService;
import com.colegio.api.services.NotaCompetenciaService;
import com.colegio.api.services.NotaFinalCalculoService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotaCompetenciaServiceImpl implements NotaCompetenciaService {

    private final NotaCompetenciaRepository notaCompetenciaRepository;
    private final EstudianteRepository estudianteRepository;
    private final CompetenciaRepository competenciaRepository;
    private final NotaCompetenciaMapper notaCompetenciaMapper;
    private final NotaAreaCalculoService notaAreaCalculoService;
    private final NotaFinalCalculoService notaFinalCalculoService;

    public NotaCompetenciaServiceImpl(NotaCompetenciaRepository notaCompetenciaRepository,
            EstudianteRepository estudianteRepository, CompetenciaRepository competenciaRepository,
            NotaCompetenciaMapper notaCompetenciaMapper, NotaAreaCalculoService notaAreaCalculoService,
            NotaFinalCalculoService notaFinalCalculoService) {
        this.notaCompetenciaRepository = notaCompetenciaRepository;
        this.estudianteRepository = estudianteRepository;
        this.competenciaRepository = competenciaRepository;
        this.notaCompetenciaMapper = notaCompetenciaMapper;
        this.notaAreaCalculoService = notaAreaCalculoService;
        this.notaFinalCalculoService = notaFinalCalculoService;
    }

    @Override
    public List<NotaCompetenciaResponseDto> obtenerPorEstudiante(UUID estudianteId) {
        return notaCompetenciaRepository.findByEstudianteId(estudianteId)
                .stream()
                .map(notaCompetenciaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotaCompetenciaResponseDto> obtenerPorEstudianteYBimestre(UUID estudianteId, Integer bimestre) {
        return notaCompetenciaRepository.findByEstudianteIdAndBimestre(estudianteId, bimestre)
                .stream()
                .map(notaCompetenciaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public NotaCompetenciaResponseDto registrarOActualizar(UUID estudianteId, NotaCompetenciaRequestDto dto) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado"));

        Competencia competencia = competenciaRepository.findById(dto.getCompetenciaId())
                .orElseThrow(() -> new EntityNotFoundException("Competencia no encontrada"));

        NotaCompetencia nota = notaCompetenciaRepository
                .findByEstudianteIdAndCompetenciaIdAndBimestre(estudianteId, dto.getCompetenciaId(), dto.getBimestre())
                .orElseGet(() -> {
                    NotaCompetencia nueva = new NotaCompetencia();
                    nueva.setEstudiante(estudiante);
                    nueva.setCompetencia(competencia);
                    nueva.setBimestre(dto.getBimestre());
                    return nueva;
                });

        nota.setCalificativo(dto.getCalificativo());
        NotaCompetencia guardada = notaCompetenciaRepository.save(nota);

        UUID cursoId = competencia.getCurso().getId();

        notaAreaCalculoService.recalcularPromedioArea(estudianteId, cursoId, dto.getBimestre());
        notaFinalCalculoService.recalcularPromedioFinalCompetencia(estudianteId, dto.getCompetenciaId());
        notaFinalCalculoService.recalcularPromedioFinalArea(estudianteId, cursoId);

        return notaCompetenciaMapper.toDto(guardada);
    }
}