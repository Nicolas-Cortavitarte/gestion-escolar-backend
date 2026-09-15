package com.colegio.api.servicesimpl;

import org.springframework.stereotype.Service;

import com.colegio.api.dtos.ApoderadoResponseDto;
import com.colegio.api.mappers.ApoderadoMapper;
import com.colegio.api.models.Apoderado;
import com.colegio.api.repositories.ApoderadoRepository;
import com.colegio.api.services.ApoderadoService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ApoderadoServiceImpl implements ApoderadoService {

    private final ApoderadoRepository apoderadoRepository;
    private final ApoderadoMapper apoderadoMapper;

    public ApoderadoServiceImpl(ApoderadoRepository apoderadoRepository, ApoderadoMapper apoderadoMapper) {
        this.apoderadoRepository = apoderadoRepository;
        this.apoderadoMapper = apoderadoMapper;
    }

    @Override
    public ApoderadoResponseDto buscarPorDni(String dni) {
        Apoderado apoderado = apoderadoRepository.findByDni(dni)
                .orElseThrow(() -> new EntityNotFoundException("Apoderado no encontrado con DNI: " + dni));

        return apoderadoMapper.toDto(apoderado);
    }

}
