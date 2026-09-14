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
import com.colegio.api.services.CompetenciaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CompetenciaServiceImpl implements CompetenciaService {

    private final CompetenciaRepository competenciaRepository;
    private final CursoRepository cursoRepository;
    private final CompetenciaMapper competenciaMapper;

    public CompetenciaServiceImpl(CompetenciaRepository competenciaRepository, CursoRepository cursoRepository,
            CompetenciaMapper competenciaMapper) {
        this.competenciaRepository = competenciaRepository;
        this.cursoRepository = cursoRepository;
        this.competenciaMapper = competenciaMapper;
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

        return competenciaMapper.toDto(competenciaRepository.save(competencia));
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

}
