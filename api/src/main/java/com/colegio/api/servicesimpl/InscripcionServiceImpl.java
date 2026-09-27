package com.colegio.api.servicesimpl;

import com.colegio.api.dtos.EstudianteResponseDto;
import com.colegio.api.dtos.InscripcionRequestDto;
import com.colegio.api.dtos.InscripcionResponseDto;
import com.colegio.api.dtos.MatriculaRequestDto;
import com.colegio.api.dtos.MatriculaResponseDto;
import com.colegio.api.services.EstudianteService;
import com.colegio.api.services.InscripcionService;
import com.colegio.api.services.MatriculaService;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InscripcionServiceImpl implements InscripcionService {
    private final EstudianteService estudianteService;
    private final MatriculaService matriculaService;

    @Override
    @Transactional
    public InscripcionResponseDto crear(InscripcionRequestDto requestDto) {
        EstudianteResponseDto estudianteCreado = estudianteService.crear(requestDto.getEstudiante());

        MatriculaRequestDto datosMatricula = new MatriculaRequestDto();
        datosMatricula.setEstudianteId(estudianteCreado.getId());
        datosMatricula.setAnioLectivo(requestDto.getAnioLectivo());
        datosMatricula.setNivel(requestDto.getNivel());
        datosMatricula.setGrado(requestDto.getGrado());
        datosMatricula.setMontoMatricula(requestDto.getMontoMatricula());
        datosMatricula.setMontoPensionMensual(requestDto.getMontoPensionMensual());
        datosMatricula.setFechaVencimiento(requestDto.getFechaVencimiento());

        MatriculaResponseDto matriculaCreada = matriculaService.crear(datosMatricula);

        return new InscripcionResponseDto(estudianteCreado, matriculaCreada);
    }
}
