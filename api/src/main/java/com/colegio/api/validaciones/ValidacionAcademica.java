package com.colegio.api.validaciones;

import java.util.Map;
import java.util.Set;

public final class ValidacionAcademica {

    private static final Map<String, Set<String>> GRADOS_POR_NIVEL = Map.of(
            "INICIAL", Set.of(
                    "3 AÑOS", "4 AÑOS", "5 AÑOS"),
            "PRIMARIA", Set.of(
                    "1RO", "2DO", "3RO",
                    "4TO", "5TO", "6TO"));

    private ValidacionAcademica() {
    }

    public static void validarNivelYGrado(String nivel, String grado) {
        if (nivel == null || grado == null) {
            throw new IllegalArgumentException(
                    "El nivel y el grado son obligatorios.");
        }

        Set<String> gradosPermitidos = GRADOS_POR_NIVEL.get(nivel);

        if (gradosPermitidos == null) {
            throw new IllegalArgumentException(
                    "El nivel debe ser INICIAL o PRIMARIA.");
        }

        if (!gradosPermitidos.contains(grado)) {
            throw new IllegalArgumentException(
                    "El grado seleccionado no corresponde al nivel "
                            + nivel + ".");
        }
    }
}
