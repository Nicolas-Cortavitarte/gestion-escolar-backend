package com.colegio.api.servicesimpl;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.colegio.api.dtos.MatriculaRequestDto;
import com.colegio.api.dtos.MatriculaResponseDto;
import com.colegio.api.mappers.MatriculaMapper;
import com.colegio.api.models.Estudiante;
import com.colegio.api.models.Matricula;
import com.colegio.api.repositories.EstudianteRepository;
import com.colegio.api.repositories.MatriculaRepository;
import com.colegio.api.services.MatriculaService;
import com.colegio.api.validaciones.ValidacionAcademica;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MatriculaServiceImpl implements MatriculaService {

        private final MatriculaRepository matriculaRepository;
        private final EstudianteRepository estudianteRepository;
        private final MatriculaMapper matriculaMapper;
        private final PensionServiceImpl pensionServiceImpl;

        public MatriculaServiceImpl(MatriculaRepository matriculaRepository, EstudianteRepository estudianteRepository,
                        MatriculaMapper matriculaMapper, PensionServiceImpl pensionServiceImpl) {
                this.matriculaRepository = matriculaRepository;
                this.estudianteRepository = estudianteRepository;
                this.matriculaMapper = matriculaMapper;
                this.pensionServiceImpl = pensionServiceImpl;
        }

        @Override
        @Transactional
        @PreAuthorize("hasRole('ADMIN')")
        public List<MatriculaResponseDto> obtenerTodos() {
                return matriculaRepository.findAll()
                                .stream()
                                .map(matriculaMapper::toDto)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        @PreAuthorize("hasRole('ADMIN')")
        public List<MatriculaResponseDto> obtenerPorEstudiante(UUID estudianteId) {
                return matriculaRepository.findByEstudianteId(estudianteId)
                                .stream()
                                .map(matriculaMapper::toDto)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public MatriculaResponseDto crear(MatriculaRequestDto requestDto) {

                ValidacionAcademica.validarNivelYGrado(
                                requestDto.getNivel(),
                                requestDto.getGrado());

                int anioActual = LocalDate.now(ZoneId.of("America/Lima")).getYear();

                if (!Integer.valueOf(anioActual).equals(requestDto.getAnioLectivo())) {
                        throw new IllegalArgumentException(
                                        "El año lectivo debe ser el año actual: " + anioActual);
                }

                Estudiante estudiante = estudianteRepository.findById(requestDto.getEstudianteId())
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Estudiante no encontrado con el ID: " + requestDto.getEstudianteId()));

                boolean yaMatriculado = matriculaRepository
                                .existsByEstudianteIdAndAnioLectivo(
                                                requestDto.getEstudianteId(),
                                                requestDto.getAnioLectivo());

                if (yaMatriculado) {
                        throw new IllegalArgumentException(
                                        "El estudiante ya tiene una matrícula para este año lectivo.");
                }

                Matricula matricula = matriculaMapper.toEntity(requestDto);
                matricula.setEstudiante(estudiante);

                Matricula guardada = matriculaRepository.save(matricula);
                pensionServiceImpl.generarPensionesDelAnio(guardada);

                return matriculaMapper.toDto(guardada);
        }

        @Override
        public MatriculaResponseDto actualizar(
                        UUID id,
                        MatriculaRequestDto requestDto) {

                ValidacionAcademica.validarNivelYGrado(
                                requestDto.getNivel(),
                                requestDto.getGrado());

                Matricula matricula = matriculaRepository
                                .buscarPorIdParaActualizar(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Matrícula no encontrada con el ID: " + id));

                boolean cambiaDatosAcademicos = !Objects.equals(
                                matricula.getEstudiante().getId(),
                                requestDto.getEstudianteId())
                                || !Objects.equals(
                                                matricula.getAnioLectivo(),
                                                requestDto.getAnioLectivo())
                                || !Objects.equals(
                                                matricula.getNivel(),
                                                requestDto.getNivel())
                                || !Objects.equals(
                                                matricula.getGrado(),
                                                requestDto.getGrado());

                if (cambiaDatosAcademicos) {
                        throw new IllegalStateException(
                                        "No se puede cambiar el estudiante, año, nivel o grado "
                                                        + "desde la edición de una matrícula registrada.");
                }

                boolean cambiaPensiones = matricula.getMontoPensionMensual()
                                .compareTo(requestDto.getMontoPensionMensual()) != 0
                                || !Objects.equals(
                                                matricula.getDiaVencimientoPension(),
                                                requestDto.getFechaVencimiento());

                if (cambiaPensiones) {
                        throw new IllegalStateException(
                                        "No se puede cambiar la pensión mensual ni el vencimiento "
                                                        + "desde esta edición: las pensiones ya fueron generadas.");
                }

                boolean cambiaMontoMatricula = matricula.getMontoMatricula()
                                .compareTo(requestDto.getMontoMatricula()) != 0;

                if (Boolean.TRUE.equals(matricula.getMatriculaPagada())
                                && cambiaMontoMatricula) {

                        throw new IllegalStateException(
                                        "No se puede modificar el monto de una matrícula pagada.");
                }

                matricula.setMontoMatricula(requestDto.getMontoMatricula());

                return matriculaMapper.toDto(
                                matriculaRepository.save(matricula));
        }

        @Override
        public MatriculaResponseDto marcarMatriculaComoPagada(UUID id, OffsetDateTime fechaPago) {
                Matricula matricula = matriculaRepository.buscarPorIdParaActualizar(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Matricula no encontrada con el ID: " + id));

                if (Boolean.TRUE.equals(matricula.getMatriculaPagada())) {
                        throw new IllegalStateException("La matricula con ID " + id + " ya se encuentra pagada.");
                }

                matricula.setMatriculaPagada(true);
                matricula.setFechaPagoMatricula(fechaPago != null ? fechaPago : OffsetDateTime.now());

                Matricula actualizada = matriculaRepository.save(matricula);

                return matriculaMapper.toDto(actualizada);
        }

        @Override
        public void eliminar(UUID id) {
                Matricula matricula = matriculaRepository.findById(id)
                                .orElseThrow(() -> new EntityNotFoundException(
                                                "Matricula no encontrada con el ID: " + id));

                matriculaRepository.delete(matricula);
        }
}
