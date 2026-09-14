package com.colegio.api.services;

import java.util.UUID;

public interface NotaFinalCalculoService {

    void recalcularPromedioFinalCompetencia(UUID estudianteId, UUID competenciaId);

    void recalcularPromedioFinalArea(UUID estudianteId, UUID cursoId);
}
