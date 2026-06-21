package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PagoDTO {
    private Long id;
    private Long prestamoAprobadoId;
    private BigDecimal monto;
    private LocalDate fechaPago;
    private String referencia;

    public PagoDTO() {}

    public PagoDTO(Long id, Long prestamoAprobadoId, BigDecimal monto, LocalDate fechaPago, String referencia) {
        this.id = id;
        this.prestamoAprobadoId = prestamoAprobadoId;
        this.monto = monto;
        this.fechaPago = fechaPago;
        this.referencia = referencia;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPrestamoAprobadoId() { return prestamoAprobadoId; }
    public void setPrestamoAprobadoId(Long prestamoAprobadoId) { this.prestamoAprobadoId = prestamoAprobadoId; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
}
