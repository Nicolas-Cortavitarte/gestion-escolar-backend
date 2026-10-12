package com.colegio.api.security;

import java.time.Duration;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

@Service
public class LimiteLoginService {

    private static final int MAX_INTENTOS = 10;
    private static final long VENTANA_NANOS = Duration.ofMinutes(1).toNanos();

    private final Cache<String, Ventana> intentos = Caffeine.newBuilder()
            .maximumSize(10_000)
            .expireAfterWrite(Duration.ofMinutes(2))
            .build();

    public boolean permitirIntento(String correo) {
        String clave = correo.trim().toLowerCase(Locale.ROOT);

        Ventana ventana = intentos.asMap().compute(clave, (email, anterior) -> {
            long ahora = System.nanoTime();

            if (anterior == null
                    || ahora - anterior.inicio() >= VENTANA_NANOS) {

                return new Ventana(ahora, 1);
            }

            return new Ventana(
                    anterior.inicio(),
                    Math.min(anterior.cantidad() + 1, MAX_INTENTOS + 1));
        });

        return ventana.cantidad() <= MAX_INTENTOS;
    }

    private record Ventana(long inicio, int cantidad) {
    }
}