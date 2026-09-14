package com.colegio.api.servicesimpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.colegio.api.dtos.NotaAreaResponseDto;
import com.colegio.api.mappers.NotaAreaMapper;
import com.colegio.api.repositories.NotaAreaRepository;
import com.colegio.api.services.NotaAreaService;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class NotaAreaServiceImpl implements NotaAreaService {

    private final NotaAreaRepository notaAreaRepository;
    private final NotaAreaMapper notaAreaMapper;

    public NotaAreaServiceImpl(NotaAreaRepository notaAreaRepository, NotaAreaMapper notaAreaMapper) {
        this.notaAreaRepository = notaAreaRepository;
        this.notaAreaMapper = notaAreaMapper;
    }

    @Override
    public List<NotaAreaResponseDto> obtenerPorEstudiante(UUID estudianteId) {
        return notaAreaRepository.findByEstudianteId(estudianteId)
                .stream()
                .map(notaAreaMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotaAreaResponseDto> obtenerPorEstudianteYBimestre(UUID estudianteId, Integer bimestre) {
        return notaAreaRepository.findByEstudianteIdAndBimestre(estudianteId, bimestre)
                .stream()
                .map(notaAreaMapper::toDto)
                .collect(Collectors.toList());
    }

}
