package com.colegio.api.servicesimpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.colegio.api.models.Competencia;
import com.colegio.api.models.Curso;
import com.colegio.api.models.Estudiante;
import com.colegio.api.models.NotaArea;
import com.colegio.api.models.NotaCompetencia;
import com.colegio.api.models.NotaCualitativa;
import com.colegio.api.repositories.CompetenciaRepository;
import com.colegio.api.repositories.CursoRepository;
import com.colegio.api.repositories.EstudianteRepository;
import com.colegio.api.repositories.NotaAreaRepository;
import com.colegio.api.repositories.NotaCompetenciaRepository;
import com.colegio.api.services.NotaFinalCalculoService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotaFinalCalculoServiceImpl implements NotaFinalCalculoService {

    private static final int BIMESTRE_FINAL = 5;
    private static final List<Integer> BIMESTRE_REGULARES = List.of(1, 2, 3, 4);

    private static final Map<NotaCualitativa, Integer> VALOR_NUMERICO = Map.of(
            NotaCualitativa.AD, 4,
            NotaCualitativa.A, 3,
            NotaCualitativa.B, 2,
            NotaCualitativa.C, 1);

    private final NotaCompetenciaRepository notaCompetenciaRepository;
    private final NotaAreaRepository notaAreaRepository;
    private final CompetenciaRepository competenciaRepository;
    private final EstudianteRepository estudianteRepository;
    private final CursoRepository cursoRepository;

    public NotaFinalCalculoServiceImpl(NotaCompetenciaRepository notaCompetenciaRepository,
            NotaAreaRepository notaAreaRepository,
            CompetenciaRepository competenciaRepository,
            EstudianteRepository estudianteRepository,
            CursoRepository cursoRepository) {
        this.notaCompetenciaRepository = notaCompetenciaRepository;
        this.notaAreaRepository = notaAreaRepository;
        this.competenciaRepository = competenciaRepository;
        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
    }

    @Override
    public void recalcularPromedioFinalCompetencia(UUID estudianteId, UUID competenciaId) {
        List<NotaCompetencia> notasBimestrales = notaCompetenciaRepository
                .findByEstudianteIdAndCompetenciaIdAndBimestreIn(estudianteId, competenciaId, BIMESTRE_REGULARES);

        if (notasBimestrales.isEmpty()) {
            return;
        }

        NotaCualitativa promedioFinal = promediar(
                notasBimestrales.stream().map(NotaCompetencia::getCalificativo).collect(Collectors.toList()));

        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado"));

        Competencia competencia = competenciaRepository.findById(competenciaId)
                .orElseThrow(() -> new EntityNotFoundException("Competencia no encontrada"));

        NotaCompetencia notaFinal = notaCompetenciaRepository
                .findByEstudianteIdAndCompetenciaIdAndBimestre(estudianteId, competenciaId, BIMESTRE_FINAL)
                .orElseGet(() -> {
                    NotaCompetencia nueva = new NotaCompetencia();
                    nueva.setEstudiante(estudiante);
                    nueva.setCompetencia(competencia);
                    nueva.setBimestre(BIMESTRE_FINAL);
                    return nueva;
                });

        notaFinal.setCalificativo(promedioFinal);
        notaCompetenciaRepository.save(notaFinal);
    }

    @Override
    public void recalcularPromedioFinalArea(UUID estudianteId, UUID cursoId) {
        List<Competencia> competencias = competenciaRepository.findByCursoId(cursoId);

        List<NotaCualitativa> finalesDeCompetencias = competencias.stream()
                .map(comp -> notaCompetenciaRepository
                        .findByEstudianteIdAndCompetenciaIdAndBimestre(estudianteId, comp.getId(), BIMESTRE_FINAL))
                .filter(Optional::isPresent)
                .map(opt -> opt.get().getCalificativo())
                .collect(Collectors.toList());

        if (finalesDeCompetencias.isEmpty()) {
            return;
        }

        NotaCualitativa promedioFinalArea = promediar(finalesDeCompetencias);

        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado"));
        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

        NotaArea notaAreaFinal = notaAreaRepository
                .findByEstudianteIdAndCursoIdAndBimestre(estudianteId, cursoId, BIMESTRE_FINAL)
                .orElseGet(() -> {
                    NotaArea nueva = new NotaArea();
                    nueva.setEstudiante(estudiante);
                    nueva.setCurso(curso);
                    nueva.setBimestre(BIMESTRE_FINAL);
                    return nueva;
                });

        notaAreaFinal.setCalificativoArea(promedioFinalArea);
        notaAreaRepository.save(notaAreaFinal);
    }

    private NotaCualitativa promediar(List<NotaCualitativa> notas) {
        double promedio = notas.stream()
                .mapToInt(VALOR_NUMERICO::get)
                .average()
                .orElse(0.0);

        if (promedio >= 3.5)
            return NotaCualitativa.AD;
        if (promedio >= 2.5)
            return NotaCualitativa.A;
        if (promedio >= 1.5)
            return NotaCualitativa.B;
        return NotaCualitativa.C;
    }
}
