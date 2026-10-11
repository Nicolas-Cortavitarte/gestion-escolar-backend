package com.colegio.api.servicesimpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.colegio.api.dtos.CursoRequestDto;
import com.colegio.api.dtos.CursoResponseDto;
import com.colegio.api.dtos.EstudianteCursoResponseDto;
import com.colegio.api.mappers.CursoMapper;
import com.colegio.api.models.Curso;
import com.colegio.api.models.Docente;
import com.colegio.api.repositories.CursoRepository;
import com.colegio.api.repositories.DocenteRepository;
import com.colegio.api.repositories.MatriculaRepository;
import com.colegio.api.repositories.NotaCompetenciaRepository;
import com.colegio.api.services.CursoService;
import com.colegio.api.validaciones.ValidacionAcademica;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CursoServiceImpl implements CursoService {

    private final CursoRepository cursoRepository;
    private final CursoMapper cursoMapper;
    private final DocenteRepository docenteRepository;
    private final MatriculaRepository matriculaRepository;
    private final NotaCompetenciaRepository notaCompetenciaRepository;

    public CursoServiceImpl(CursoRepository cursoRepository, CursoMapper cursoMapper,
            DocenteRepository docenteRepository, MatriculaRepository matriculaRepository,
            NotaCompetenciaRepository notaCompetenciaRepository) {
        this.cursoRepository = cursoRepository;
        this.cursoMapper = cursoMapper;
        this.docenteRepository = docenteRepository;
        this.matriculaRepository = matriculaRepository;
        this.notaCompetenciaRepository = notaCompetenciaRepository;
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

    private Docente obtenerDocenteActivo(UUID docenteId) {
        Docente docente = docenteRepository.findById(docenteId)
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado"));

        if (docente.getUsuario() == null
                || !Boolean.TRUE.equals(docente.getUsuario().getActivo())) {

            throw new IllegalStateException(
                    "No se puede asignar un curso a un docente inactivo.");
        }

        return docente;
    }

    @Override
    public CursoResponseDto crear(CursoRequestDto cursoRequestDto) {

        ValidacionAcademica.validarNivelYGrado(
                cursoRequestDto.getNivel(),
                cursoRequestDto.getGrado());

        Curso curso = cursoMapper.toEntity(cursoRequestDto);

        if (cursoRequestDto.getDocenteId() != null) {
            curso.setDocente(obtenerDocenteActivo(cursoRequestDto.getDocenteId()));
        }

        Curso creado = cursoRepository.save(curso);
        return cursoMapper.toDto(creado);
    }

    @Override
    public CursoResponseDto actualizar(UUID id, CursoRequestDto cursoRequestDto) {

        ValidacionAcademica.validarNivelYGrado(
                cursoRequestDto.getNivel(),
                cursoRequestDto.getGrado());

        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El curso con id " + id + " no existe"));

        boolean cambiaConfiguracionAcademica = !java.util.Objects.equals(
                curso.getAnioLectivo(),
                cursoRequestDto.getAnioLectivo())
                || !java.util.Objects.equals(
                        curso.getNivel(),
                        cursoRequestDto.getNivel())
                || !java.util.Objects.equals(
                        curso.getGrado(),
                        cursoRequestDto.getGrado());

        if (cambiaConfiguracionAcademica
                && notaCompetenciaRepository
                        .existsByCompetencia_Curso_Id(id)) {

            throw new IllegalStateException(
                    "Este curso ya tiene notas registradas. "
                            + "No se puede cambiar su año lectivo, nivel o grado.");
        }

        cursoMapper.updateEntityFromDto(cursoRequestDto, curso);

        if (cursoRequestDto.getDocenteId() != null) {
            curso.setDocente(
                    obtenerDocenteActivo(cursoRequestDto.getDocenteId()));
        } else {
            curso.setDocente(null);
        }

        Curso actualizado = cursoRepository.save(curso);
        return cursoMapper.toDto(actualizado);
    }

    @Override
    public void eliminar(UUID id) {
        if (!cursoRepository.existsById(id)) {
            throw new EntityNotFoundException("Curso no encontrado");
        }

        if (notaCompetenciaRepository.existsByCompetencia_Curso_Id(id)) {
            throw new IllegalStateException(
                    "No se puede eliminar un curso con notas registradas.");
        }

        cursoRepository.deleteById(id);
    }

    @Override
    public List<CursoResponseDto> obtenerPorDocente(UUID id) {
        return cursoRepository.findByDocenteId(id)
                .stream()
                .map(cursoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CursoResponseDto> obtenerPorUsuarioDocente(UUID usuarioId) {
        Docente docente = docenteRepository.findByUsuario_Id(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "No se encontró un perfil docente asociado a tu cuenta"));

        return obtenerPorDocente(docente.getId());
    }

    @Override
    public List<EstudianteCursoResponseDto> obtenerEstudiantesPorCurso(
            UUID cursoId) {

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Curso no encontrado"));

        return matriculaRepository
                .findByAnioLectivoAndNivelAndGrado(
                        curso.getAnioLectivo(),
                        curso.getNivel(),
                        curso.getGrado())
                .stream()
                .map(matricula -> {
                    var estudiante = matricula.getEstudiante();

                    EstudianteCursoResponseDto dto = new EstudianteCursoResponseDto();

                    dto.setEstudianteId(estudiante.getId());
                    dto.setNombreEstudiante(
                            estudiante.getNombres() + " "
                                    + estudiante.getApellidos());
                    dto.setAnioLectivo(matricula.getAnioLectivo());
                    dto.setNivel(matricula.getNivel());
                    dto.setGrado(matricula.getGrado());

                    return dto;
                })
                .collect(Collectors.toList());
    }

}
