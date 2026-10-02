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
import com.colegio.api.models.NotaArea;
import com.colegio.api.models.NotaCualitativa;
import com.colegio.api.models.ResumenFinalEstudiante;
import com.colegio.api.models.SituacionFinal;
import com.colegio.api.repositories.EstudianteRepository;
import com.colegio.api.repositories.NotaAreaRepository;
import com.colegio.api.repositories.ResumenFinalEstudianteRepository;
import com.colegio.api.services.ResumenFinalService;
import com.colegio.api.models.Matricula;
import com.colegio.api.repositories.CursoRepository;
import com.colegio.api.repositories.MatriculaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class ResumenFinalServiceImpl implements ResumenFinalService {

        private static final int BIMESTRE_FINAL = 5;

        private final ResumenFinalEstudianteRepository resumenFinalRepository;
        private final EstudianteRepository estudianteRepository;
        private final NotaAreaRepository notaAreaRepository;
        private final ResumenFinalMapper resumenFinalMapper;
        private final CursoRepository cursoRepository;
        private final MatriculaRepository matriculaRepository;

        public ResumenFinalServiceImpl(ResumenFinalEstudianteRepository resumenFinalRepository,
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
        public ResumenFinalResponseDto obtenerPorEstudianteYAnio(UUID estudianteId, Integer anioLectivo) {
                ResumenFinalEstudiante resumen = resumenFinalRepository
                                .findByEstudianteIdAndAnioLectivo(estudianteId, anioLectivo)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Resumen final no encontrado para el estudiante " + estudianteId
                                                                + " en el año " + anioLectivo));
                return resumenFinalMapper.toDto(resumen);
        }

        @Override
        public ResumenFinalResponseDto calcularYGuardar(UUID estudianteId, Integer anioLectivo) {
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
                        throw new IllegalStateException(
                                        "No hay cursos configurados para la matrícula del estudiante");
                }

                var idsCursosEsperados = cursosEsperados.stream()
                                .map(Curso::getId)
                                .collect(Collectors.toSet());

                List<NotaArea> todasLasNotas = notaAreaRepository.findByEstudianteId(estudianteId)
                                .stream()
                                .filter(n -> idsCursosEsperados.contains(n.getCurso().getId()))
                                .collect(Collectors.toList());

                if (todasLasNotas.isEmpty()) {
                        throw new IllegalStateException(
                                        "El estudiante no tiene notas registradas para el año lectivo " + anioLectivo);
                }

                Map<Curso, List<NotaArea>> notasPorCurso = todasLasNotas.stream()
                                .collect(Collectors.groupingBy(NotaArea::getCurso));

                for (Curso curso : cursosEsperados) {
                        if (!notasPorCurso.containsKey(curso)) {
                                throw new IllegalStateException(
                                                "No se puede calcular el resumen final: el área '"
                                                                + curso.getNombre()
                                                                + "' no tiene notas registradas");
                        }
                }

                for (Map.Entry<Curso, List<NotaArea>> entry : notasPorCurso.entrySet()) {
                        long bimestresRegulares = entry.getValue().stream()
                                        .filter(n -> n.getBimestre() >= 1 && n.getBimestre() <= 4)
                                        .count();
                        boolean tieneFinal = entry.getValue().stream()
                                        .anyMatch(n -> n.getBimestre() == BIMESTRE_FINAL);

                        if (bimestresRegulares < 4 || !tieneFinal) {
                                throw new IllegalStateException(
                                                "No se puede calcular el resumen final: el área '"
                                                                + entry.getKey().getNombre()
                                                                + "' tiene bimestres o promedio final pendientes de registrar");
                        }
                }

                Map<Curso, NotaCualitativa> promedioFinalPorCurso = notasPorCurso.entrySet().stream()
                                .collect(Collectors.toMap(
                                                Map.Entry::getKey,
                                                entry -> entry.getValue().stream()
                                                                .filter(n -> n.getBimestre() == BIMESTRE_FINAL)
                                                                .findFirst()
                                                                .map(NotaArea::getCalificativoArea)
                                                                .orElseThrow()));

                boolean tieneDesaprobado = promedioFinalPorCurso.values().stream()
                                .anyMatch(nota -> nota == NotaCualitativa.C);
                boolean tieneEnRecuperacion = promedioFinalPorCurso.values().stream()
                                .anyMatch(nota -> nota == NotaCualitativa.B);

                SituacionFinal situacion;
                if (tieneDesaprobado) {
                        situacion = SituacionFinal.DESAPROBADO;
                } else if (tieneEnRecuperacion) {
                        situacion = SituacionFinal.RECUPERACION;
                } else {
                        situacion = SituacionFinal.APROBADO;
                }

                String areasTexto = promedioFinalPorCurso.entrySet().stream()
                                .filter(entry -> entry.getValue() == NotaCualitativa.B
                                                || entry.getValue() == NotaCualitativa.C)
                                .map(entry -> entry.getKey().getNombre())
                                .collect(Collectors.joining(", "));

                if (areasTexto.isBlank()) {
                        areasTexto = null;
                }

                ResumenFinalEstudiante resumen = resumenFinalRepository
                                .findByEstudianteIdAndAnioLectivo(estudianteId, anioLectivo)
                                .orElseGet(() -> {
                                        ResumenFinalEstudiante nuevo = new ResumenFinalEstudiante();
                                        nuevo.setEstudiante(estudiante);
                                        nuevo.setAnioLectivo(anioLectivo);
                                        return nuevo;
                                });

                resumen.setSituacionFinal(situacion);
                resumen.setAreaARecuperar(areasTexto);

                return resumenFinalMapper.toDto(resumenFinalRepository.save(resumen));
        }
}
