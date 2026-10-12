package com.colegio.api.security;

import java.io.IOException;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UsuarioDetailsService usuarioDetailsService;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UsuarioDetailsService usuarioDetailsService,
            JwtAuthenticationEntryPoint authenticationEntryPoint) {

        this.jwtService = jwtService;
        this.usuarioDetailsService = usuarioDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                String token = authHeader.substring(7).trim();

                if (token.isEmpty()) {
                    throw new BadCredentialsException("Token ausente");
                }

                String email = jwtService.extraerEmail(token);

                if (email == null || email.isBlank()) {
                    throw new BadCredentialsException("Token inválido");
                }

                UserDetails usuario = usuarioDetailsService
                        .loadUserByUsername(email);

                if (!usuario.isEnabled()) {
                    throw new DisabledException("La cuenta está inactiva");
                }

                if (!usuario.isAccountNonLocked()
                        || !usuario.isAccountNonExpired()
                        || !usuario.isCredentialsNonExpired()) {

                    throw new BadCredentialsException(
                            "La cuenta no está habilitada para acceder");
                }

                if (!(usuario instanceof UsuarioPrincipal principal)
                        || !jwtService.esTokenValido(token, principal.getUsuario())) {

                    throw new BadCredentialsException(
                            "Token inválido o sesión revocada");
                }

                UsernamePasswordAuthenticationToken autenticacion = new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        usuario.getAuthorities());

                autenticacion.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request));

                SecurityContextHolder.getContext()
                        .setAuthentication(autenticacion);
            }
        } catch (JwtException | IllegalArgumentException excepcion) {
            rechazar(
                    request,
                    response,
                    new BadCredentialsException(
                            "Token inválido o vencido", excepcion));
            return;
        } catch (AuthenticationException excepcion) {
            rechazar(request, response, excepcion);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void rechazar(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException excepcion) throws IOException {

        SecurityContextHolder.clearContext();
        authenticationEntryPoint.commence(request, response, excepcion);
    }
}