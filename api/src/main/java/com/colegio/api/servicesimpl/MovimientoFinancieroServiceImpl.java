package com.colegio.api.servicesimpl;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.colegio.api.dtos.MovimientoFinancieroRequestDto;
import com.colegio.api.dtos.MovimientoFinancieroResponseDto;
import com.colegio.api.mappers.MovimientoFinancieroMapper;
import com.colegio.api.models.MovimientoFinanciero;
import com.colegio.api.models.Usuario;
import com.colegio.api.repositories.MovimientoFinancieroRepository;
import com.colegio.api.security.UsuarioPrincipal;
import com.colegio.api.services.MovimientoFinancieroService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class MovimientoFinancieroServiceImpl implements MovimientoFinancieroService {

    private final MovimientoFinancieroRepository movimientoRepository;
    private final MovimientoFinancieroMapper movimientoMapper;

    public MovimientoFinancieroServiceImpl(MovimientoFinancieroRepository movimientoRepository,
            MovimientoFinancieroMapper movimientoMapper) {
        this.movimientoRepository = movimientoRepository;
        this.movimientoMapper = movimientoMapper;
    }

    @Override
    public List<MovimientoFinancieroResponseDto> obtenerPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return movimientoRepository.findByFechaBetween(inicio, fin)
                .stream().map(movimientoMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public MovimientoFinancieroResponseDto crear(MovimientoFinancieroRequestDto requestDto) {
        MovimientoFinanciero movimiento = movimientoMapper.toEntity(requestDto);

        Usuario usuarioActual = obtenerUsuarioAutenticado();
        movimiento.setRegistradoPor(usuarioActual);

        return movimientoMapper.toDto(movimientoRepository.save(movimiento));
    }

    @Override
    public void eliminar(UUID id) {
        if (!movimientoRepository.existsById(id)) {
            throw new EntityNotFoundException("Movimiento no encontrado con ID: " + id);
        }
        movimientoRepository.deleteById(id);
    }

    private Usuario obtenerUsuarioAutenticado() {
        UsuarioPrincipal principal = (UsuarioPrincipal) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return principal.getUsuario();
    }
}
