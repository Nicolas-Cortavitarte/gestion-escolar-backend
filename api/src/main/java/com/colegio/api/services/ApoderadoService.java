package com.colegio.api.services;

import com.colegio.api.dtos.ApoderadoResponseDto;

public interface ApoderadoService {

    ApoderadoResponseDto buscarPorDni(String dni);

}
