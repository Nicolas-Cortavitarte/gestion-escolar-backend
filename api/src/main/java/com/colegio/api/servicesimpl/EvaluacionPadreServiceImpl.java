package com.colegio.api.servicesimpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.colegio.api.dtos.EvaluacionPadreRequestDto;
import com.colegio.api.dtos.EvaluacionPadreResponseDto;
import com.colegio.api.mappers.EvaluacionPadreMapper;
import com.colegio.api.models.Estudiante;
import com.colegio.api.models.EvaluacionPadreFamilia;
import com.colegio.api.repositories.EstudianteRepository;
import com.colegio.api.repositories.EvaluacionPadreFamiliaRepository;
import com.colegio.api.services.EvaluacionPadreService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EvaluacionPadreServiceImpl implements EvaluacionPadreService {

    private final EvaluacionPadreFamiliaRepository evaluacionPadreRepository;
    private final EstudianteRepository estudianteRepository;
    private final EvaluacionPadreMapper evaluacionPadreMapper;

    public EvaluacionPadreServiceImpl(EvaluacionPadreFamiliaRepository evaluacionPadreRepository,
            EstudianteRepository estudianteRepository,
            EvaluacionPadreMapper evaluacionPadreMapper) {
        this.evaluacionPadreRepository = evaluacionPadreRepository;
        this.estudianteRepository = estudianteRepository;
        this.evaluacionPadreMapper = evaluacionPadreMapper;
    }

    @Override
    public List<EvaluacionPadreResponseDto> obtenerPorEstudiante(UUID estudianteId) {
        return evaluacionPadreRepository.findByEstudianteId(estudianteId)
                .stream().map(evaluacionPadreMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public EvaluacionPadreResponseDto registrarOActualizar(UUID estudianteId, EvaluacionPadreRequestDto requestDto) {
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado con ID: " + estudianteId));

        EvaluacionPadreFamilia evaluacion = evaluacionPadreRepository
                .findByEstudianteIdAndBimestre(estudianteId, requestDto.getBimestre())
                .orElseGet(() -> {
                    EvaluacionPadreFamilia nueva = new EvaluacionPadreFamilia();
                    nueva.setEstudiante(estudiante);
                    nueva.setBimestre(requestDto.getBimestre());
                    return nueva;
                });

        evaluacion.setEnviaPuntualmenteHijo(requestDto.getEnviaPuntualmenteHijo());
        evaluacion.setApoyaTareasCasa(requestDto.getApoyaTareasCasa());
        evaluacion.setEnviaHijoUniformado(requestDto.getEnviaHijoUniformado());
        evaluacion.setAsisteReunionesColegio(requestDto.getAsisteReunionesColegio());
        evaluacion.setCumplePagosInstitucion(requestDto.getCumplePagosInstitucion());

        return evaluacionPadreMapper.toDto(evaluacionPadreRepository.save(evaluacion));
    }
}
