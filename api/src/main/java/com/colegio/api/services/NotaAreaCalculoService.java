package com.colegio.api.services;

import java.util.UUID;

public interface NotaAreaCalculoService {

    void recalcularPromedioArea(UUID estudianteId, UUID areaId, Integer bimestre);

}
