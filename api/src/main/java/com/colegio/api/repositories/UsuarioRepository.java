package com.colegio.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.api.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    // Buscar usuario por correo electrónico
    Optional<Usuario> findByEmail(String email);

    // Validar que existe email
    boolean existsByEmail(String email);
}
