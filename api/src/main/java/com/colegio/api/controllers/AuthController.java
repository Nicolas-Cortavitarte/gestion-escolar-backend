package com.colegio.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.colegio.api.dtos.LoginRequestDto;
import com.colegio.api.dtos.LoginResponseDto;
import com.colegio.api.security.JwtService;
import com.colegio.api.security.UsuarioPrincipal;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getCorreo(), requestDto.getContrasena()));

        UsuarioPrincipal principal = (UsuarioPrincipal) authentication.getPrincipal();
        String rol = principal.getUsuario().getRol().name();
        String token = jwtService.generarToken(principal.getUsername(), rol);

        LoginResponseDto response = new LoginResponseDto();
        response.setToken(token);
        response.setCorreo(principal.getUsername());
        response.setRol(rol);

        return ResponseEntity.ok(response);
    }
}
