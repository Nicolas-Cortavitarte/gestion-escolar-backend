package com.colegio.api.servicesimpl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.colegio.api.dtos.AreaBoletaDto;
import com.colegio.api.dtos.BoletaResponseDto;
import com.colegio.api.dtos.CompetenciaBoletaDto;
import com.colegio.api.dtos.ConductaBimestreDto;
import com.colegio.api.dtos.EvaluacionPadreBimestreDto;
import com.colegio.api.dtos.ResumenFinalResponseDto;
import com.colegio.api.mappers.ResumenFinalMapper;
import com.colegio.api.models.Competencia;
import com.colegio.api.models.Curso;
import com.colegio.api.models.Estudiante;
import com.colegio.api.models.EvaluacionPadreFamilia;
import com.colegio.api.models.NotaArea;
import com.colegio.api.models.NotaCompetencia;
import com.colegio.api.models.NotaCualitativa;
import com.colegio.api.models.ReporteConductaAsistencia;
import com.colegio.api.repositories.CompetenciaRepository;
import com.colegio.api.repositories.EstudianteRepository;
import com.colegio.api.repositories.EvaluacionPadreFamiliaRepository;
import com.colegio.api.repositories.NotaAreaRepository;
import com.colegio.api.repositories.NotaCompetenciaRepository;
import com.colegio.api.repositories.ReporteConductaAsistenciaRepository;
import com.colegio.api.repositories.ResumenFinalEstudianteRepository;
import com.colegio.api.services.BoletaService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class BoletaServiceImpl implements BoletaService {

    private static final int BIMESTRE_FINAL = 5;

    private final EstudianteRepository estudianteRepository;
    private final NotaAreaRepository notaAreaRepository;
    private final NotaCompetenciaRepository notaCompetenciaRepository;
    private final CompetenciaRepository competenciaRepository;
    private final ReporteConductaAsistenciaRepository reporteConductaAsistenciaRepository;
    private final EvaluacionPadreFamiliaRepository evaluacionPadreRepository;
    private final ResumenFinalEstudianteRepository resumenFinalRepository;
    private final ResumenFinalMapper resumenFinalMapper;

    public BoletaServiceImpl(
            EstudianteRepository estudianteRepository,
            NotaAreaRepository notaAreaRepository,
            NotaCompetenciaRepository notaCompetenciaRepository,
            CompetenciaRepository competenciaRepository,
            ReporteConductaAsistenciaRepository reporteConductaAsistenciaRepository,
            EvaluacionPadreFamiliaRepository evaluacionPadreRepository,
            ResumenFinalEstudianteRepository resumenFinalRepository,
            ResumenFinalMapper resumenFinalMapper) {
        this.estudianteRepository = estudianteRepository;
        this.notaAreaRepository = notaAreaRepository;
        this.notaCompetenciaRepository = notaCompetenciaRepository;
        this.competenciaRepository = competenciaRepository;
        this.reporteConductaAsistenciaRepository = reporteConductaAsistenciaRepository;
        this.evaluacionPadreRepository = evaluacionPadreRepository;
        this.resumenFinalRepository = resumenFinalRepository;
        this.resumenFinalMapper = resumenFinalMapper;
    }

    @Override
    public BoletaResponseDto generarBoleta(UUID estudianteId, Integer anioLectivo) {

        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado con ID: " + estudianteId));

        BoletaResponseDto boleta = new BoletaResponseDto();
        boleta.setEstudianteId(estudianteId);
        boleta.setNombreEstudiante(estudiante.getNombres() + " " + estudiante.getApellidos());
        boleta.setAnioLectivo(anioLectivo);

        boleta.setAreas(construirAreas(estudianteId, anioLectivo));
        boleta.setConducta(construirConducta(estudianteId, anioLectivo));
        boleta.setEvaluacionPadre(construirEvaluacionPadre(estudianteId, anioLectivo));
        boleta.setResumenFinal(construirResumenFinal(estudianteId, anioLectivo));

        return boleta;
    }

    private List<AreaBoletaDto> construirAreas(UUID estudianteId, Integer anioLectivo) {

        List<NotaArea> notasDelAnio = notaAreaRepository.findByEstudianteId(estudianteId)
                .stream()
                .filter(n -> n.getCurso().getAnioLectivo().equals(anioLectivo))
                .collect(Collectors.toList());

        Map<Curso, List<NotaArea>> notasPorCurso = notasDelAnio.stream()
                .collect(Collectors.groupingBy(NotaArea::getCurso));

        return notasPorCurso.entrySet().stream()
                .map(entry -> construirArea(estudianteId, entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(AreaBoletaDto::getNombreArea))
                .collect(Collectors.toList());
    }

    private AreaBoletaDto construirArea(UUID estudianteId, Curso curso, List<NotaArea> notasDelCurso) {
        AreaBoletaDto area = new AreaBoletaDto();
        area.setNombreArea(curso.getNombre());

        Map<Integer, NotaCualitativa> calificativosPorBimestre = notasDelCurso.stream()
                .filter(n -> n.getBimestre() >= 1 && n.getBimestre() <= 4)
                .collect(Collectors.toMap(NotaArea::getBimestre, NotaArea::getCalificativoArea));
        area.setCalificativoAreaPorBimestre(calificativosPorBimestre);

        notasDelCurso.stream()
                .filter(n -> n.getBimestre() == BIMESTRE_FINAL)
                .findFirst()
                .ifPresent(n -> area.setPromedioFinalArea(n.getCalificativoArea()));

        List<Competencia> competencias = competenciaRepository.findByCursoId(curso.getId());
        List<CompetenciaBoletaDto> competenciasDto = competencias.stream()
                .map(comp -> construirCompetencia(estudianteId, comp))
                .collect(Collectors.toList());
        area.setCompetencias(competenciasDto);

        return area;
    }

    private CompetenciaBoletaDto construirCompetencia(UUID estudianteId, Competencia competencia) {
        List<NotaCompetencia> notas = notaCompetenciaRepository
                .findByEstudianteIdAndCompetenciaId(estudianteId, competencia.getId());

        CompetenciaBoletaDto dto = new CompetenciaBoletaDto();
        dto.setNombreCompetencia(competencia.getNombreCompetencia());

        Map<Integer, NotaCualitativa> notasPorBimestre = notas.stream()
                .filter(n -> n.getBimestre() >= 1 && n.getBimestre() <= 4)
                .collect(Collectors.toMap(NotaCompetencia::getBimestre, NotaCompetencia::getCalificativo));
        dto.setNotasPorBimestre(notasPorBimestre);

        notas.stream()
                .filter(n -> n.getBimestre() == BIMESTRE_FINAL)
                .findFirst()
                .ifPresent(n -> dto.setPromedioFinal(n.getCalificativo()));

        return dto;
    }

    private List<ConductaBimestreDto> construirConducta(UUID estudianteId, Integer anioLectivo) {
        return reporteConductaAsistenciaRepository.findByEstudianteIdAndAnioLectivo(estudianteId, anioLectivo)
                .stream()
                .sorted(Comparator.comparing(ReporteConductaAsistencia::getBimestre))
                .map(r -> {
                    ConductaBimestreDto dto = new ConductaBimestreDto();
                    dto.setBimestre(r.getBimestre());
                    dto.setPuntualidadRespeto(r.getConductaPuntualidadRespeto());
                    dto.setActitudAula(r.getConductaActitudAula());
                    dto.setPresentacionAseo(r.getConductaPresentacionAseo());
                    dto.setInasistenciasJustificadas(r.getInasistenciasJustificadas());
                    dto.setInasistenciasInjustificadas(r.getInasistenciasInjustificadas());
                    dto.setTardanzasJustificadas(r.getTardanzasJustificadas());
                    dto.setTardanzasInjustificadas(r.getTardanzasInjustificadas());
                    dto.setApreciacionTutor(r.getApreciacionTutor());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private List<EvaluacionPadreBimestreDto> construirEvaluacionPadre(UUID estudianteId, Integer anioLectivo) {
        return evaluacionPadreRepository.findByEstudianteIdAndAnioLectivo(estudianteId, anioLectivo)
                .stream()
                .sorted(Comparator.comparing(EvaluacionPadreFamilia::getBimestre))
                .map(e -> {
                    EvaluacionPadreBimestreDto dto = new EvaluacionPadreBimestreDto();
                    dto.setBimestre(e.getBimestre());
                    dto.setEnviaPuntualmenteHijo(e.getEnviaPuntualmenteHijo());
                    dto.setApoyaTareasCasa(e.getApoyaTareasCasa());
                    dto.setEnviaHijoUniformado(e.getEnviaHijoUniformado());
                    dto.setAsisteReunionesColegio(e.getAsisteReunionesColegio());
                    dto.setCumplePagosInstitucion(e.getCumplePagosInstitucion());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    private ResumenFinalResponseDto construirResumenFinal(UUID estudianteId, Integer anioLectivo) {
        return resumenFinalRepository.findByEstudianteIdAndAnioLectivo(estudianteId, anioLectivo)
                .map(resumenFinalMapper::toDto)
                .orElse(null);
    }

}
