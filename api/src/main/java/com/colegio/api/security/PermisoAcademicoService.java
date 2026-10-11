package com.colegio.api.security;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.colegio.api.models.RolUsuario;
import com.colegio.api.models.Usuario;
import com.colegio.api.repositories.CompetenciaRepository;
import com.colegio.api.repositories.CursoRepository;
import com.colegio.api.repositories.MatriculaRepository;

@Service("permisoAcademicoService")
@Transactional(readOnly = true)
public class PermisoAcademicoService {

    private final CursoRepository cursoRepository;
    private final MatriculaRepository matriculaRepository;
    private final CompetenciaRepository competenciaRepository;

    public PermisoAcademicoService(
            CursoRepository cursoRepository,
            MatriculaRepository matriculaRepository,
            CompetenciaRepository competenciaRepository) {
        this.cursoRepository = cursoRepository;
        this.matriculaRepository = matriculaRepository;
        this.competenciaRepository = competenciaRepository;
    }

    public boolean puedeAcceder(
            UUID estudianteId,
            Integer anioLectivo) {

        Authentication autenticacion = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (autenticacion == null
                || !autenticacion.isAuthenticated()
                || !(autenticacion.getPrincipal() instanceof UsuarioPrincipal principal)) {
            return false;
        }

        Usuario usuario = principal.getUsuario();

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            return false;
        }

        if (usuario.getRol() == RolUsuario.ADMIN) {
            return true;
        }

        if (usuario.getRol() != RolUsuario.DOCENTE
                || estudianteId == null
                || anioLectivo == null) {
            return false;
        }

        return matriculaRepository
                .findByEstudianteIdAndAnioLectivo(
                        estudianteId,
                        anioLectivo)
                .map(matricula -> cursoRepository
                        .existsByDocente_Usuario_IdAndAnioLectivoAndNivelAndGrado(
                                usuario.getId(),
                                matricula.getAnioLectivo(),
                                matricula.getNivel(),
                                matricula.getGrado()))
                .orElse(false);
    }

    public boolean puedeAccederCurso(UUID cursoId) {
        Authentication autenticacion = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (autenticacion == null
                || !autenticacion.isAuthenticated()
                || !(autenticacion.getPrincipal() instanceof UsuarioPrincipal principal)) {
            return false;
        }

        Usuario usuario = principal.getUsuario();

        if (!Boolean.TRUE.equals(usuario.getActivo())) {
            return false;
        }

        if (usuario.getRol() == RolUsuario.ADMIN) {
            return true;
        }

        if (usuario.getRol() != RolUsuario.DOCENTE || cursoId == null) {
            return false;
        }

        return cursoRepository.findById(cursoId)
                .map(curso -> curso.getDocente() != null
                        && usuario.getId().equals(
                                curso.getDocente().getUsuario().getId()))
                .orElse(false);
    }

    public boolean puedeLeerNotaCompetencia(
            UUID estudianteId,
            UUID competenciaId) {

        if (estudianteId == null || competenciaId == null) {
            return false;
        }

        return competenciaRepository.findById(competenciaId)
                .map(competencia -> puedeLeerNotaArea(
                        estudianteId,
                        competencia.getCurso().getId()))
                .orElse(false);
    }

    public boolean puedeLeerNotaArea(
            UUID estudianteId,
            UUID cursoId) {

        if (estudianteId == null || cursoId == null) {
            return false;
        }

        return cursoRepository.findById(cursoId)
                .map(curso -> puedeAcceder(
                        estudianteId,
                        curso.getAnioLectivo()))
                .orElse(false);
    }

}