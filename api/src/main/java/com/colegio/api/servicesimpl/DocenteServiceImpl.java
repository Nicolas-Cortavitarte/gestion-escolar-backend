package com.colegio.api.servicesimpl;

import com.colegio.api.repositories.UsuarioRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.colegio.api.dtos.DocenteRequestDto;
import com.colegio.api.dtos.DocenteResponseDto;
import com.colegio.api.dtos.DocenteUpdateDto;
import com.colegio.api.mappers.DocenteMapper;
import com.colegio.api.models.Docente;
import com.colegio.api.models.RolUsuario;
import com.colegio.api.models.Usuario;
import com.colegio.api.repositories.CursoRepository;
import com.colegio.api.repositories.DocenteRepository;
import com.colegio.api.services.DocenteService;
import com.colegio.api.services.PagoDocenteService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class DocenteServiceImpl implements DocenteService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final CursoRepository cursoRepository;
    private final DocenteRepository docenteRepository;
    private final DocenteMapper docenteMapper;
    private final PagoDocenteService pagoDocenteService;

    public DocenteServiceImpl(DocenteRepository docenteRepository, DocenteMapper docenteMapper,
            UsuarioRepository usuarioRepository, CursoRepository cursoRepository,
            PasswordEncoder passwordEncoder, PagoDocenteService pagoDocenteService) {
        this.docenteRepository = docenteRepository;
        this.docenteMapper = docenteMapper;
        this.cursoRepository = cursoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.pagoDocenteService = pagoDocenteService;
    }

    @Override
    public DocenteResponseDto ontenerPorId(UUID id) {
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado con ID: " + id));

        return docenteMapper.toDto(docente);
    }

    @Override
    public DocenteResponseDto buscarPorDni(String dni) {
        Docente docente = docenteRepository.findByDni(dni)
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado con DNI: " + dni));

        return docenteMapper.toDto(docente);
    }

    @Override
    public List<DocenteResponseDto> obtenerTodos() {
        return docenteRepository.findAll()
                .stream()
                .map(docenteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DocenteResponseDto crear(DocenteRequestDto requestDto) {
        if (docenteRepository.existsByDni(requestDto.getDni())) {
            throw new IllegalArgumentException("El DNI ya existe");
        }
        if (usuarioRepository.existsByEmail(requestDto.getCorreo())) {
            throw new IllegalArgumentException("El correo ya existe");
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(requestDto.getCorreo());
        usuario.setPassword(passwordEncoder.encode(requestDto.getContrasena()));
        usuario.setRol(RolUsuario.DOCENTE);
        usuario.setActivo(true);
        Usuario usuarioGuardado = usuarioRepository.save(usuario);

        Docente docente = docenteMapper.toEntity(requestDto);
        docente.setUsuario(usuarioGuardado);
        Docente creado = docenteRepository.save(docente);

        pagoDocenteService.genererPagoDelAnio(docente, LocalDate.now().getYear());

        return docenteMapper.toDto(creado);
    }

    @Override
    public DocenteResponseDto actualizar(UUID id, DocenteUpdateDto requestDto) {
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado con ID: " + id));

        docenteMapper.updateEntityFromDto(requestDto, docente);
        Docente actualizado = docenteRepository.save(docente);
        return docenteMapper.toDto(actualizado);
    }

    @Override
    public void desactivar(UUID id) {
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado con ID: " + id));

        if (cursoRepository.existsByDocenteId(id)) {
            throw new IllegalStateException("El docente no se puede desactivar porque tiene cursos asignados");
        }

        Usuario usuario = docente.getUsuario();
        usuario.setActivo(false);
        usuarioRepository.save(usuario);
    }

    @Override
    public void reactivar(UUID id) {
        Docente docente = docenteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Docente no encontrado con ID: " + id));

        Usuario usuario = docente.getUsuario();
        usuario.setActivo(true);
        usuarioRepository.save(usuario);
    }
}
