package com.colegio.api.servicesimpl;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.colegio.api.models.Curso;
import com.colegio.api.models.Estudiante;
import com.colegio.api.models.NotaArea;
import com.colegio.api.models.NotaCompetencia;
import com.colegio.api.models.NotaCualitativa;
import com.colegio.api.repositories.CursoRepository;
import com.colegio.api.repositories.EstudianteRepository;
import com.colegio.api.repositories.NotaAreaRepository;
import com.colegio.api.repositories.NotaCompetenciaRepository;
import com.colegio.api.services.NotaAreaCalculoService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotaAreaCalculoServiceImpl implements NotaAreaCalculoService {

    private static final Map<NotaCualitativa, Integer> VALOR_NUMERICO = Map.of(
            NotaCualitativa.AD, 4,
            NotaCualitativa.A, 3,
            NotaCualitativa.B, 2,
            NotaCualitativa.C, 1);

    private final NotaCompetenciaRepository competenciaRepository;
    private final NotaAreaRepository areaRepository;
    private final EstudianteRepository estudianteRepository;
    private final CursoRepository cursoRepository;

    public NotaAreaCalculoServiceImpl(NotaCompetenciaRepository competenciaRepository,
            NotaAreaRepository areaRepository,
            EstudianteRepository estudianteRepository,
            CursoRepository cursoRepository) {
        this.competenciaRepository = competenciaRepository;
        this.areaRepository = areaRepository;
        this.estudianteRepository = estudianteRepository;
        this.cursoRepository = cursoRepository;
    }

    @Override
    public void recalcularPromedioArea(UUID estudianteId, UUID cursoId, Integer bimestre) {

        List<NotaCompetencia> notaCompetencias = competenciaRepository
                .findByEstudianteIdAndAreaIdAndBimestre(estudianteId, cursoId, bimestre);

        if (notaCompetencias.isEmpty()) {
            return;
        }

        double promedio = notaCompetencias.stream()
                .mapToInt(n -> VALOR_NUMERICO.getOrDefault(n.getCalificativo(), 0))
                .average()
                .orElse(0.0);

        NotaCualitativa calificativoArea = convertirANota(promedio);

        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado"));

        Curso curso = cursoRepository.findById(cursoId)
                .orElseThrow(() -> new EntityNotFoundException("Curso no encontrado"));

        NotaArea notaArea = areaRepository
                .findByEstudianteIdAndCursoIdAndBimestre(estudianteId, cursoId, bimestre)
                .orElseGet(() -> {
                    NotaArea nueva = new NotaArea();
                    nueva.setEstudiante(estudiante);
                    nueva.setCurso(curso);
                    nueva.setBimestre(bimestre);
                    return nueva;
                });

        notaArea.setCalificativoArea(calificativoArea);
        areaRepository.save(notaArea);
    }

    private NotaCualitativa convertirANota(double promedio) {
        if (promedio >= 3.5)
            return NotaCualitativa.AD;
        if (promedio >= 2.5)
            return NotaCualitativa.A;
        if (promedio >= 1.5)
            return NotaCualitativa.B;
        return NotaCualitativa.C;
    }
}
