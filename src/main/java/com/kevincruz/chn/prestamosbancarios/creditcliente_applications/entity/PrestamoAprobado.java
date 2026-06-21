package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prestamos_aprobados")
public class PrestamoAprobado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private BigDecimal montoAprobado;

    @Column(nullable = false)
    private Integer plazoDias;

    @Column(nullable = false)
    private BigDecimal tasaInteres;

    @Column(nullable = false, updatable = false)
    private LocalDate fechaAprobacion;

    @Column(nullable = false)
    private LocalDate fechaVencimiento;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoPago estado;

    @OneToMany(mappedBy = "prestamoAprobado", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos = new ArrayList<>();

    public PrestamoAprobado() {}

    @PrePersist
    protected void onCreate() {
        fechaAprobacion = LocalDate.now();
        if (estado == null) {
            estado = EstadoPago.ACTIVO;
        }
    }

    public BigDecimal getSaldoPendiente() {
        BigDecimal totalPagado = pagos.stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return montoAprobado.add(calcularInteres()).subtract(totalPagado);
    }

    public BigDecimal calcularInteres() {
        return montoAprobado.multiply(tasaInteres).divide(new BigDecimal(100));
    }

    public enum EstadoPago {
        ACTIVO, PAGADO, CANCELADO
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }

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

    public EstadoPago getEstado() { return estado; }
    public void setEstado(EstadoPago estado) { this.estado = estado; }

    public List<Pago> getPagos() { return pagos; }
    public void setPagos(List<Pago> pagos) { this.pagos = pagos; }
}
