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
import com.colegio.api.models.Curso;
import com.colegio.api.models.Matricula;
import com.colegio.api.repositories.MatriculaRepository;
import com.colegio.api.services.ResumenFinalService;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.colegio.api.models.RolUsuario;
import com.colegio.api.models.Usuario;
import com.colegio.api.security.UsuarioPrincipal;

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
        private final MatriculaRepository matriculaRepository;
        private final ResumenFinalService resumenFinalService;

        public NotaCompetenciaServiceImpl(NotaCompetenciaRepository notaCompetenciaRepository,
                        EstudianteRepository estudianteRepository, CompetenciaRepository competenciaRepository,
                        NotaCompetenciaMapper notaCompetenciaMapper, NotaAreaCalculoService notaAreaCalculoService,
                        NotaFinalCalculoService notaFinalCalculoService, MatriculaRepository matriculaRepository,
                        ResumenFinalService resumenFinalService) {
                this.notaCompetenciaRepository = notaCompetenciaRepository;
                this.estudianteRepository = estudianteRepository;
                this.competenciaRepository = competenciaRepository;
                this.notaCompetenciaMapper = notaCompetenciaMapper;
                this.notaAreaCalculoService = notaAreaCalculoService;
                this.notaFinalCalculoService = notaFinalCalculoService;
                this.matriculaRepository = matriculaRepository;
                this.resumenFinalService = resumenFinalService;
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

                if (dto.getBimestre() == null
                                || dto.getBimestre() < 1
                                || dto.getBimestre() > 4) {
                        throw new IllegalArgumentException(
                                        "Solo se pueden registrar notas de los bimestres 1 al 4");
                }

                Curso curso = competencia.getCurso();

                validarPermisoSobreCurso(curso);

                Matricula matricula = matriculaRepository
                                .findByEstudianteIdAndAnioLectivo(
                                                estudianteId, curso.getAnioLectivo())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "El estudiante no tiene matrícula para el año del curso"));

                if (!curso.getNivel().equals(matricula.getNivel())
                                || !curso.getGrado().equals(matricula.getGrado())) {
                        throw new IllegalArgumentException(
                                        "El curso no corresponde al nivel y grado de la matrícula");
                }

                NotaCompetencia nota = notaCompetenciaRepository
                                .findByEstudianteIdAndCompetenciaIdAndBimestre(estudianteId, dto.getCompetenciaId(),
                                                dto.getBimestre())
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

                resumenFinalService.recalcularSiCompleto(estudianteId, curso.getAnioLectivo());

                return notaCompetenciaMapper.toDto(guardada);
        }

        private void validarPermisoSobreCurso(Curso curso) {
                Authentication autenticacion = SecurityContextHolder.getContext()
                                .getAuthentication();

                if (autenticacion == null
                                || !autenticacion.isAuthenticated()
                                || !(autenticacion.getPrincipal() instanceof UsuarioPrincipal principal)) {
                        throw new AccessDeniedException(
                                        "No tienes permiso para registrar notas");
                }

                Usuario usuario = principal.getUsuario();

                if (!Boolean.TRUE.equals(usuario.getActivo())) {
                        throw new AccessDeniedException("La cuenta está inactiva");
                }

                if (usuario.getRol() == RolUsuario.ADMIN) {
                        return;
                }

                if (usuario.getRol() != RolUsuario.DOCENTE
                                || curso.getDocente() == null
                                || !usuario.getId().equals(
                                                curso.getDocente().getUsuario().getId())) {
                        throw new AccessDeniedException(
                                        "Solo puedes registrar notas de tus cursos asignados");
                }
        }
}