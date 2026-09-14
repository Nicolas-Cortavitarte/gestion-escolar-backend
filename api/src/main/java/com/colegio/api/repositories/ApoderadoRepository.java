package com.colegio.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.colegio.api.models.Apoderado;

public interface ApoderadoRepository extends JpaRepository<Apoderado, UUID> {

    // Buscar apoderado por DNI
    Optional<Apoderado> findByDni(String dni);
}
