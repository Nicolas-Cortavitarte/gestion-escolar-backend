package com.colegio.api.servicesimpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.colegio.api.dtos.CursoRequestDto;
import com.colegio.api.dtos.CursoResponseDto;
import com.colegio.api.mappers.CursoMapper;
import com.colegio.api.models.Curso;
import com.colegio.api.models.Docente;
import com.colegio.api.repositories.CursoRepository;
import com.colegio.api.repositories.DocenteRepository;
import com.colegio.api.services.CursoService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final CursoMapper cursoMapper;
    private final DocenteRepository docenteRepository;

    public CursoServiceImpl(CursoRepository cursoRepository, CursoMapper cursoMapper,
            DocenteRepository docenteRepository) {
        this.cursoRepository = cursoRepository;
        this.cursoMapper = cursoMapper;
        this.docenteRepository = docenteRepository;
    }

    @Override
    public List<CursoResponseDto> obtenerTodos() {
        return cursoRepository.findAll()
                .stream()
                .map(cursoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CursoResponseDto obtenerPorId(UUID id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El curso con id " + id + " no existe"));
        return cursoMapper.toDto(curso);
    }

    @Override
    public CursoResponseDto crear(CursoRequestDto cursoRequestDto) {

        Curso curso = cursoMapper.toEntity(cursoRequestDto);

        if (cursoRequestDto.getDocenteId() != null) {
            Docente docente = docenteRepository.findById(cursoRequestDto.getDocenteId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "El docente con id " + cursoRequestDto.getDocenteId() + " no existe"));

            curso.setDocente(docente);
        }

        Curso creado = cursoRepository.save(curso);
        return cursoMapper.toDto(creado);
    }

    @Override
    public CursoResponseDto actualizar(UUID id, CursoRequestDto cursoRequestDto) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El curso con id " + id + " no existe"));

        cursoMapper.updateEntityFromDto(cursoRequestDto, curso);

        if (cursoRequestDto.getDocenteId() != null) {
            Docente docente = docenteRepository.findById(cursoRequestDto.getDocenteId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "El docente con id " + cursoRequestDto.getDocenteId() + " no existe"));

            curso.setDocente(docente);
        } else {
            curso.setDocente(null);
        }

        Curso actualizado = cursoRepository.save(curso);
        return cursoMapper.toDto(actualizado);
    }

    @Override
    public void eliminar(UUID id) {
        if (!cursoRepository.existsById(id)) {
            throw new IllegalArgumentException("El curso con id " + id + " no existe");
        }
        cursoRepository.deleteById(id);
    }

}
