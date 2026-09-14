package com.colegio.api.servicesimpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.colegio.api.dtos.EstudianteRequestDto;
import com.colegio.api.dtos.EstudianteResponseDto;
import com.colegio.api.mappers.EstudianteMapper;
import com.colegio.api.models.Apoderado;
import com.colegio.api.models.Estudiante;
import com.colegio.api.repositories.ApoderadoRepository;
import com.colegio.api.repositories.EstudianteRepository;
import com.colegio.api.services.EstudianteService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final ApoderadoRepository apoderadoRepository;
    private final EstudianteMapper estudianteMapper;

    public EstudianteServiceImpl(EstudianteRepository estudianteRepository, ApoderadoRepository apoderadoRepository,
            EstudianteMapper estudianteMapper) {
        this.estudianteRepository = estudianteRepository;
        this.apoderadoRepository = apoderadoRepository;
        this.estudianteMapper = estudianteMapper;
    }

    @Override
    public List<EstudianteResponseDto> obtenerTodos() {
        return estudianteRepository.findAll()
                .stream()
                .map(estudianteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EstudianteResponseDto obtenerPorId(UUID id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado con ID: " + id));
        return estudianteMapper.toDto(estudiante);
    }

    @Override
    @Transactional
    public EstudianteResponseDto crear(EstudianteRequestDto requestDto) {

        Apoderado apoderado;

        if (requestDto.getIdApoderado() != null) {
            apoderado = apoderadoRepository.findById(requestDto.getIdApoderado())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Apoderado no encontrado con ID: " + requestDto.getIdApoderado()));
        } else if (requestDto.getApoderadoNuevo() != null) {
            apoderado = new Apoderado();
            apoderado.setDni(requestDto.getApoderadoNuevo().getDni());
            apoderado.setNombres(requestDto.getApoderadoNuevo().getNombre());
            apoderado.setApellidos(requestDto.getApoderadoNuevo().getApellidos());
            apoderado.setTelefono(requestDto.getApoderadoNuevo().getTelefono());
            apoderado.setEmail(requestDto.getApoderadoNuevo().getEmail());
            apoderado.setParentesco(requestDto.getApoderadoNuevo().getParentesco());

            apoderado = apoderadoRepository.save(apoderado);
        } else {
            throw new IllegalArgumentException("Debe proporcionar un apoderado existente o nuevo");
        }

        Estudiante estudiante = new Estudiante();
        estudiante.setDni(requestDto.getDni());
        estudiante.setNombres(requestDto.getNombres());
        estudiante.setApellidos(requestDto.getApellidos());
        estudiante.setFechaNacimiento(requestDto.getFechaNacimiento());
        estudiante.setDireccion(requestDto.getDireccion());
        estudiante.setApoderado(apoderado);

        Estudiante creado = estudianteRepository.save(estudiante);
        return estudianteMapper.toDto(creado);
    }

    @Override
    public EstudianteResponseDto actualizar(UUID id, EstudianteRequestDto requestDto) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estudiante no encontrado con ID: " + id));

        Apoderado apoderado = apoderadoRepository.findById(requestDto.getIdApoderado())
                .orElseThrow(
                        () -> new EntityNotFoundException(
                                "Apoderado no encontrado con ID: " + requestDto.getIdApoderado()));

        estudianteMapper.updateEntityFromDto(requestDto, estudiante, apoderado);
        Estudiante actualizado = estudianteRepository.save(estudiante);

        return estudianteMapper.toDto(actualizado);
    }

    @Override
    public void eliminar(UUID id) {
        if (!estudianteRepository.existsById(id)) {
            throw new EntityNotFoundException("Estudiante no encontrado con ID: " + id);
        }
        estudianteRepository.deleteById(id);
    }

}