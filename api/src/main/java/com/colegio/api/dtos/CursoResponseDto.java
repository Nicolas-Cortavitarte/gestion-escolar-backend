package com.colegio.api.dtos;

import java.util.UUID;

import lombok.Data;

@Data
public class CursoResponseDto {

    private UUID id;
    private String nombre;
    private String nivel;
    private String grado;
    private Integer anioLectivo;
    private UUID docenteId;
    private String nombresDocente;

}
