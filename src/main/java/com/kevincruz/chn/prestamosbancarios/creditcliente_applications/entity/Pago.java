package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "pagos")
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prestamo_aprobado_id", nullable = false)
    private PrestamoAprobado prestamoAprobado;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false, updatable = false)
    private LocalDate fechaPago;

    @Column(nullable = true)
    private String referencia;

    public Pago() {}

    @PrePersist
    protected void onCreate() {
        fechaPago = LocalDate.now();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public PrestamoAprobado getPrestamoAprobado() { return prestamoAprobado; }
    public void setPrestamoAprobado(PrestamoAprobado prestamoAprobado) { this.prestamoAprobado = prestamoAprobado; }

    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }

    public LocalDate getFechaPago() { return fechaPago; }
    public void setFechaPago(LocalDate fechaPago) { this.fechaPago = fechaPago; }

    public String getReferencia() { return referencia; }
    public void setReferencia(String referencia) { this.referencia = referencia; }
}
