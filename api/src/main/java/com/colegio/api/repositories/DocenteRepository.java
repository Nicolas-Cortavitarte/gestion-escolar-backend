package com.colegio.api.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.colegio.api.models.Docente;

import jakarta.persistence.LockModeType;

public interface DocenteRepository extends JpaRepository<Docente, UUID> {

    // Buscar Docentes por DNI
    Optional<Docente> findByDni(String dni);

    // Buscar Docente por ID
    Optional<Docente> findById(UUID usuarioUuid);

    // Verificar si existe un Docente por email
    boolean existsByUsuario_Email(String email);

    // Verificar si existe un Docente por DNI
    boolean existsByDni(String dni);

    // Buscar Docente por ID de Usuario
    Optional<Docente> findByUsuario_Id(UUID usuarioId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT d FROM Docente d WHERE d.id = :id")
    Optional<Docente> buscarPorIdParaActualizar(@Param("id") UUID id);
}
