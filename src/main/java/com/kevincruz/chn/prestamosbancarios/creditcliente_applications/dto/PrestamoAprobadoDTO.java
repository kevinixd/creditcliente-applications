package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.dto;

import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.PrestamoAprobado;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PrestamoAprobadoDTO {
    private Long id;
    private Long clienteId;
    private BigDecimal montoAprobado;
    private Integer plazoDias;
    private BigDecimal tasaInteres;
    private LocalDate fechaAprobacion;
    private LocalDate fechaVencimiento;
    private PrestamoAprobado.EstadoPago estado;
    private BigDecimal saldoPendiente;

    public PrestamoAprobadoDTO() {}

    public PrestamoAprobadoDTO(Long id, Long clienteId, BigDecimal montoAprobado, Integer plazoDias, BigDecimal tasaInteres, LocalDate fechaAprobacion, LocalDate fechaVencimiento, PrestamoAprobado.EstadoPago estado, BigDecimal saldoPendiente) {
        this.id = id;
        this.clienteId = clienteId;
        this.montoAprobado = montoAprobado;
        this.plazoDias = plazoDias;
        this.tasaInteres = tasaInteres;
        this.fechaAprobacion = fechaAprobacion;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.saldoPendiente = saldoPendiente;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public BigDecimal getMontoAprobado() { return montoAprobado; }
    public void setMontoAprobado(BigDecimal montoAprobado) { this.montoAprobado = montoAprobado; }

    public Integer getPlazoDias() { return plazoDias; }
    public void setPlazoDias(Integer plazoDias) { this.plazoDias = plazoDias; }

    public BigDecimal getTasaInteres() { return tasaInteres; }
    public void setTasaInteres(BigDecimal tasaInteres) { this.tasaInteres = tasaInteres; }

    public LocalDate getFechaAprobacion() { return fechaAprobacion; }
    public void setFechaAprobacion(LocalDate fechaAprobacion) { this.fechaAprobacion = fechaAprobacion; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public PrestamoAprobado.EstadoPago getEstado() { return estado; }
    public void setEstado(PrestamoAprobado.EstadoPago estado) { this.estado = estado; }

    public BigDecimal getSaldoPendiente() { return saldoPendiente; }
    public void setSaldoPendiente(BigDecimal saldoPendiente) { this.saldoPendiente = saldoPendiente; }
}
