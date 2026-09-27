package com.colegio.api.services;

import com.colegio.api.dtos.InscripcionRequestDto;
import com.colegio.api.dtos.InscripcionResponseDto;

public interface InscripcionService {

    InscripcionResponseDto crear(InscripcionRequestDto requestDto);
}
