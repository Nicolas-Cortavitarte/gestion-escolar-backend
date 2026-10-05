package com.colegio.api.servicesimpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.colegio.api.dtos.CompetenciaRequestDto;
import com.colegio.api.dtos.CompetenciaResponseDto;
import com.colegio.api.mappers.CompetenciaMapper;
import com.colegio.api.models.Competencia;
import com.colegio.api.models.Curso;
import com.colegio.api.repositories.CompetenciaRepository;
import com.colegio.api.repositories.CursoRepository;
import com.colegio.api.repositories.NotaAreaRepository;
import com.colegio.api.repositories.ResumenFinalEstudianteRepository;
import com.colegio.api.services.CompetenciaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CompetenciaServiceImpl implements CompetenciaService {

    private final CompetenciaRepository competenciaRepository;
    private final CursoRepository cursoRepository;
    private final CompetenciaMapper competenciaMapper;
    private final NotaAreaRepository notaAreaRepository;
    private final ResumenFinalEstudianteRepository resumenFinalRepository;

    public CompetenciaServiceImpl(CompetenciaRepository competenciaRepository, CursoRepository cursoRepository,
            CompetenciaMapper competenciaMapper, NotaAreaRepository notaAreaRepository,
            ResumenFinalEstudianteRepository resumenFinalRepository) {
        this.competenciaRepository = competenciaRepository;
        this.cursoRepository = cursoRepository;
        this.competenciaMapper = competenciaMapper;
        this.notaAreaRepository = notaAreaRepository;
        this.resumenFinalRepository = resumenFinalRepository;
    }

    @Override
    public List<CompetenciaResponseDto> obtenerTodasLasCompetencias() {
        return competenciaRepository.findAll().stream()
                .map(competenciaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompetenciaResponseDto obtenerCompetenciaPorId(UUID id) {
        Competencia competencia = competenciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Competencia no encontrada con id: " + id));

        return competenciaMapper.toDto(competencia);
    }

    @Override
    public CompetenciaResponseDto crear(CompetenciaRequestDto requestDto) {
        Curso curso = cursoRepository.findById(requestDto.getCursoId())
                .orElseThrow(
                        () -> new EntityNotFoundException("Curso no encontrado con id: " + requestDto.getCursoId()));

        Competencia competencia = competenciaMapper.toEntity(requestDto);
        competencia.setCurso(curso);

        Competencia guardada = competenciaRepository.save(competencia);

        invalidarResultadosDelCurso(curso);

        return competenciaMapper.toDto(guardada);
    }

    @Override
    public CompetenciaResponseDto actualizar(UUID id, CompetenciaRequestDto requestDto) {
        Competencia competencia = competenciaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Competencia no encontrada con id: " + id));

        competenciaMapper.updateEntityFromDto(requestDto, competencia);
        return competenciaMapper.toDto(competenciaRepository.save(competencia));
    }

    @Override
    public void eliminar(UUID id) {
        if (!this.competenciaRepository.existsById(id)) {
            throw new EntityNotFoundException("Competencia no encontrada con id: " + id);
        }
        this.competenciaRepository.deleteById(id);
    }

    private void invalidarResultadosDelCurso(Curso curso) {
        List<UUID> estudiantesAfectados = notaAreaRepository
                .findByCursoId(curso.getId())
                .stream()
                .map(nota -> nota.getEstudiante().getId())
                .distinct()
                .collect(Collectors.toList());

        notaAreaRepository.deleteByCursoId(curso.getId());

        for (UUID estudianteId : estudiantesAfectados) {
            resumenFinalRepository.deleteByEstudianteIdAndAnioLectivo(
                    estudianteId, curso.getAnioLectivo());
        }
    }
}
