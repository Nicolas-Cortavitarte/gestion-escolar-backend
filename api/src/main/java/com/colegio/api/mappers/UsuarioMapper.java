package com.colegio.api.mappers;

import org.springframework.stereotype.Component;

import com.colegio.api.dtos.UsuarioRequestDto;
import com.colegio.api.dtos.UsuarioResponseDto;
import com.colegio.api.dtos.UsuarioUpdateDto;
import com.colegio.api.models.Usuario;

@Component
public class UsuarioMapper {

    public UsuarioResponseDto toDto(Usuario usuario) {
        if (usuario == null) {
            return null;
        }

        UsuarioResponseDto responseDto = new UsuarioResponseDto();
        responseDto.setId(usuario.getId());
        responseDto.setCorreo(usuario.getEmail());
        responseDto.setRol(usuario.getRol());
        responseDto.setActivo(usuario.getActivo());
        responseDto.setCreadoEn(usuario.getCreadoEn());

        return responseDto;
    }

    public Usuario toEntity(UsuarioRequestDto usuarioRequestDto) {
        if (usuarioRequestDto == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setEmail(usuarioRequestDto.getCorreo());
        usuario.setRol(usuarioRequestDto.getRol());
        usuario.setActivo(true);

        return usuario;
    }

    public void updateEntityFromDto(UsuarioUpdateDto requestDto, Usuario usuario) {
        if (requestDto == null || usuario == null) {
            return;
        }

        usuario.setEmail(requestDto.getCorreo());
        usuario.setRol(requestDto.getRol());
    }
}
