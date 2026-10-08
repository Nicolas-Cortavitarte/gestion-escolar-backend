package com.colegio.api.servicesimpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.colegio.api.dtos.ResumenFinalResponseDto;
import com.colegio.api.mappers.ResumenFinalMapper;
import com.colegio.api.models.Curso;
import com.colegio.api.models.Estudiante;
import com.colegio.api.models.Matricula;
import com.colegio.api.models.NotaArea;
import com.colegio.api.models.NotaCualitativa;
import com.colegio.api.models.ResumenFinalEstudiante;
import com.colegio.api.models.SituacionFinal;
import com.colegio.api.repositories.CursoRepository;
import com.colegio.api.repositories.EstudianteRepository;
import com.colegio.api.repositories.MatriculaRepository;
import com.colegio.api.repositories.NotaAreaRepository;
import com.colegio.api.repositories.ResumenFinalEstudianteRepository;
import com.colegio.api.services.ResumenFinalService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ResumenFinalServiceImpl implements ResumenFinalService {

        private static final int BIMESTRE_FINAL = 5;
        private static final List<Integer> BIMESTRES_REQUERIDOS = List.of(1, 2, 3, 4, BIMESTRE_FINAL);

        private final ResumenFinalEstudianteRepository resumenFinalRepository;
        private final EstudianteRepository estudianteRepository;
        private final NotaAreaRepository notaAreaRepository;
        private final ResumenFinalMapper resumenFinalMapper;
        private final CursoRepository cursoRepository;
        private final MatriculaRepository matriculaRepository;

        public ResumenFinalServiceImpl(
                        ResumenFinalEstudianteRepository resumenFinalRepository,
                        EstudianteRepository estudianteRepository,
                        NotaAreaRepository notaAreaRepository,
                        ResumenFinalMapper resumenFinalMapper,
                        CursoRepository cursoRepository,
                        MatriculaRepository matriculaRepository) {
                this.resumenFinalRepository = resumenFinalRepository;
                this.estudianteRepository = estudianteRepository;
                this.notaAreaRepository = notaAreaRepository;
                this.resumenFinalMapper = resumenFinalMapper;
                this.cursoRepository = cursoRepository;
                this.matriculaRepository = matriculaRepository;
        }

        @Override
        public ResumenFinalResponseDto obtenerPorEstudianteYAnio(
                        UUID estudianteId, Integer anioLectivo) {
                ResumenFinalEstudiante resumen = resumenFinalRepository
                                .findByEstudianteIdAndAnioLectivo(estudianteId, anioLectivo)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Resumen final no encontrado para el estudiante "
                                                                + estudianteId + " en el año " + anioLectivo));

                return resumenFinalMapper.toDto(resumen);
        }

        @Override
        public ResumenFinalResponseDto calcularYGuardar(
                        UUID estudianteId, Integer anioLectivo) {
                return calcular(estudianteId, anioLectivo, false);
        }

        @Override
        public void recalcularSiCompleto(UUID estudianteId, Integer anioLectivo) {
                calcular(estudianteId, anioLectivo, true);
        }

        private ResumenFinalResponseDto calcular(
                        UUID estudianteId,
                        Integer anioLectivo,
                        boolean automatico) {
                Estudiante estudiante = estudianteRepository.findById(estudianteId)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Estudiante no encontrado con ID: " + estudianteId));

                Matricula matricula = matriculaRepository
                                .findByEstudianteIdAndAnioLectivo(estudianteId, anioLectivo)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "El estudiante no tiene matrícula para el año seleccionado"));

                List<Curso> cursosEsperados = cursoRepository
                                .findByAnioLectivoAndNivelAndGrado(
                                                anioLectivo,
                                                matricula.getNivel(),
                                                matricula.getGrado());

                if (cursosEsperados.isEmpty()) {
                        return manejarPendientes(
                                        estudianteId,
                                        anioLectivo,
                                        automatico,
                                        "No hay cursos configurados para la matrícula del estudiante");
                }

                var idsCursosEsperados = cursosEsperados.stream()
                                .map(Curso::getId)
                                .collect(Collectors.toSet());

                Map<UUID, List<NotaArea>> notasPorCurso = notaAreaRepository
                                .findByEstudianteId(estudianteId)
                                .stream()
                                .filter(nota -> idsCursosEsperados.contains(nota.getCurso().getId()))
                                .collect(Collectors.groupingBy(
                                                nota -> nota.getCurso().getId()));

                for (Curso curso : cursosEsperados) {
                        List<NotaArea> notas = notasPorCurso
                                        .getOrDefault(curso.getId(), List.of());

                        boolean completo = BIMESTRES_REQUERIDOS.stream()
                                        .allMatch(bimestre -> notas.stream()
                                                        .anyMatch(nota -> bimestre.equals(nota.getBimestre())
                                                                        && nota.getCalificativoArea() != null));

                        if (!completo) {
                                return manejarPendientes(
                                                estudianteId,
                                                anioLectivo,
                                                automatico,
                                                "No se puede calcular el resumen final: el área '"
                                                                + curso.getNombre()
                                                                + "' tiene bimestres o promedio final pendientes");
                        }
                }

                Map<UUID, NotaCualitativa> finalesPorCurso = cursosEsperados.stream()
                                .collect(Collectors.toMap(
                                                Curso::getId,
                                                curso -> notasPorCurso.get(curso.getId()).stream()
                                                                .filter(nota -> Integer.valueOf(BIMESTRE_FINAL)
                                                                                .equals(nota.getBimestre())
                                                                                && nota.getCalificativoArea() != null)
                                                                .findFirst()
                                                                .orElseThrow()
                                                                .getCalificativoArea()));

                boolean tieneDesaprobado = finalesPorCurso.values().stream()
                                .anyMatch(nota -> nota == NotaCualitativa.C);

                boolean tieneEnRecuperacion = finalesPorCurso.values().stream()
                                .anyMatch(nota -> nota == NotaCualitativa.B);

                SituacionFinal situacion;

                if (tieneDesaprobado) {
                        situacion = SituacionFinal.DESAPROBADO;
                } else if (tieneEnRecuperacion) {
                        situacion = SituacionFinal.RECUPERACION;
                } else {
                        situacion = SituacionFinal.APROBADO;
                }

                String areasTexto = cursosEsperados.stream()
                                .filter(curso -> {
                                        NotaCualitativa nota = finalesPorCurso.get(curso.getId());
                                        return nota == NotaCualitativa.B
                                                        || nota == NotaCualitativa.C;
                                })
                                .map(Curso::getNombre)
                                .sorted()
                                .collect(Collectors.joining(", "));

                ResumenFinalEstudiante resumen = resumenFinalRepository
                                .findByEstudianteIdAndAnioLectivo(estudianteId, anioLectivo)
                                .orElseGet(() -> {
                                        ResumenFinalEstudiante nuevo = new ResumenFinalEstudiante();
                                        nuevo.setEstudiante(estudiante);
                                        nuevo.setAnioLectivo(anioLectivo);
                                        return nuevo;
                                });

                resumen.setSituacionFinal(situacion);
                resumen.setAreaARecuperar(
                                areasTexto.isBlank() ? null : areasTexto);

                return resumenFinalMapper.toDto(
                                resumenFinalRepository.save(resumen));
        }

        private ResumenFinalResponseDto manejarPendientes(
                        UUID estudianteId,
                        Integer anioLectivo,
                        boolean automatico,
                        String mensaje) {
                if (!automatico) {
                        throw new IllegalStateException(mensaje);
                }

                resumenFinalRepository.deleteByEstudianteIdAndAnioLectivo(
                                estudianteId, anioLectivo);

                return null;
        }
}