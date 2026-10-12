package com.colegio.api.exceptions;

public class LimiteLoginException extends RuntimeException {

    public LimiteLoginException() {
        super("Demasiados intentos de inicio de sesión. "
                + "Espera un minuto antes de volver a intentarlo.");
    }
}