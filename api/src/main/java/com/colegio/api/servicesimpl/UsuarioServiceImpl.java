package com.colegio.api.servicesimpl;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.colegio.api.dtos.CambiarPasswordDto;
import com.colegio.api.dtos.UsuarioRequestDto;
import com.colegio.api.dtos.UsuarioResponseDto;
import com.colegio.api.dtos.UsuarioUpdateDto;
import com.colegio.api.mappers.UsuarioMapper;
import com.colegio.api.models.Usuario;
import com.colegio.api.repositories.UsuarioRepository;
import com.colegio.api.services.UsuarioService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final PasswordEncoder passwordEncoder;
    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<UsuarioResponseDto> obtenerTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(usuarioMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioResponseDto obtenerPorId(UUID id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));
        return usuarioMapper.toDto(usuario);
    }

    @Override
    public UsuarioResponseDto crear(UsuarioRequestDto requestDto) {
        if (usuarioRepository.existsByEmail(requestDto.getCorreo())) {
            throw new IllegalArgumentException("El correo ya existe");
        }

        Usuario usuario = usuarioMapper.toEntity(requestDto);
        usuario.setPassword(passwordEncoder.encode(requestDto.getContrasena()));

        Usuario guardado = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(guardado);
    }

    @Override
    public UsuarioResponseDto actualizar(UUID id, UsuarioUpdateDto requestDto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        usuarioMapper.updateEntityFromDto(requestDto, usuario);
        Usuario actualizado = usuarioRepository.save(usuario);
        return usuarioMapper.toDto(actualizado);
    }

    @Override
    public void cambiarPassword(UUID id, CambiarPasswordDto dto) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con ID: " + id));

        if (!passwordEncoder.matches(dto.getPasswordActual(), usuario.getPassword())) {
            throw new IllegalArgumentException("Contraseña actual incorrecta");
        }

        usuario.setPassword(passwordEncoder.encode(dto.getPasswordNueva()));
        usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(UUID id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }
}
