package com.colegio.api.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pensiones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pension {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matricula_id", nullable = false)
    private Matricula matricula;

    @Column(name = "mes", nullable = false)
    private Integer mes;

    @Column(name = "monto_base", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoBase;

    @Builder.Default
    @Column(name = "mora_acumulada", precision = 10, scale = 2)
    private BigDecimal moraAcumulada = BigDecimal.ZERO;

    @Column(name = "monto_total", insertable = false, updatable = false, precision = 10, scale = 2)
    private BigDecimal montoTotal;

    @Column(name = "fecha_vencimiento", nullable = false)
    private LocalDate fechaVencimiento;

    @Column(name = "fecha_pago")
    private OffsetDateTime fechaPago;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "estado", nullable = false)
    private EstadoPension estado = EstadoPension.PENDIENTE;
}
