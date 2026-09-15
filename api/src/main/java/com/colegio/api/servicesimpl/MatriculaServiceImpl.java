package com.colegio.api.servicesimpl;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.colegio.api.dtos.MatriculaRequestDto;
import com.colegio.api.dtos.MatriculaResponseDto;
import com.colegio.api.mappers.MatriculaMapper;
import com.colegio.api.models.Estudiante;
import com.colegio.api.models.Matricula;
import com.colegio.api.repositories.EstudianteRepository;
import com.colegio.api.repositories.MatriculaRepository;
import com.colegio.api.services.MatriculaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MatriculaServiceImpl implements MatriculaService {

    private final MatriculaRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final MatriculaMapper matriculaMapper;
    private final PensionServiceImpl pensionServiceImpl;

    public MatriculaServiceImpl(MatriculaRepository matriculaRepository, EstudianteRepository estudianteRepository,
            MatriculaMapper matriculaMapper, PensionServiceImpl pensionServiceImpl) {
        this.matriculaRepository = matriculaRepository;
        this.estudianteRepository = estudianteRepository;
        this.matriculaMapper = matriculaMapper;
        this.pensionServiceImpl = pensionServiceImpl;
    }

    @Override
    @Transactional
    public List<MatriculaResponseDto> obtenerTodos() {
        return matriculaRepository.findAll()
                .stream()
                .map(matriculaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<MatriculaResponseDto> obtenerPorEstudiante(UUID estudianteId) {
        return matriculaRepository.findByEstudianteId(estudianteId)
                .stream()
                .map(matriculaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public MatriculaResponseDto crear(MatriculaRequestDto responseDto) {
        Estudiante estudiante = estudianteRepository.findById(responseDto.getEstudianteId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Estudiante no encontrado con el ID: " + responseDto.getEstudianteId()));

        Matricula matricula = matriculaMapper.toEntity(responseDto);
        matricula.setEstudiante(estudiante);

        Matricula guardada = matriculaRepository.save(matricula);

        pensionServiceImpl.generarPensionesDelAnio(guardada);

        return matriculaMapper.toDto(guardada);
    }

    @Override
    public MatriculaResponseDto actualizar(UUID id, MatriculaRequestDto requestDto) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matricula no encontrada con el ID: " + id));

        matriculaMapper.updateEntityFromDto(requestDto, matricula);

        if (!matricula.getEstudiante().getId().equals(requestDto.getEstudianteId())) {
            Estudiante estudiante = estudianteRepository.findById(requestDto.getEstudianteId())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Estudiante no encontrado con el ID: " + requestDto.getEstudianteId()));
            matricula.setEstudiante(estudiante);
        }

        Matricula guardada = matriculaRepository.save(matricula);

        return matriculaMapper.toDto(guardada);
    }

    @Override
    public MatriculaResponseDto marcarMatriculaComoPagada(UUID id, OffsetDateTime fechaPago) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matricula no encontrada con el ID: " + id));

        if (Boolean.TRUE.equals(matricula.getMatriculaPagada())) {
            throw new IllegalStateException("La matricula con ID " + id + " ya se encuentra pagada.");
        }

        matricula.setMatriculaPagada(true);
        matricula.setFechaPagoMatricula(fechaPago != null ? fechaPago : OffsetDateTime.now());

        Matricula actualizada = matriculaRepository.save(matricula);

        return matriculaMapper.toDto(actualizada);
    }

    @Override
    public void eliminar(UUID id) {
        Matricula matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Matricula no encontrada con el ID: " + id));

        matriculaRepository.delete(matricula);
    }
}
