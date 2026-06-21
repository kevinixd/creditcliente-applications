package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.dto;

import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.PrestamoSolicitud;
import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

public class PrestamoSolicitudDTO {
    private Long id;

    @NotNull(message = "El cliente es obligatorio")
    private Long clienteId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal montoSolicitado;

    @NotNull(message = "El plazo es obligatorio")
    @Min(value = 365, message = "El plazo debe ser al menos de 365 días")
    private Integer plazoDias;

    @NotBlank(message = "El detalle es obligatorio")
    @Size(max = 500, message = "El detalle no puede exceder de los 500 caracteres")
    private String detalles;
    
    private PrestamoSolicitud.EstadoSolicitud estado;
    private LocalDate fechaSolicitud;

    public PrestamoSolicitudDTO() {}

    public PrestamoSolicitudDTO(Long id, Long clienteId, BigDecimal montoSolicitado, Integer plazoDias, String detalles, PrestamoSolicitud.EstadoSolicitud estado, LocalDate fechaSolicitud) {
        this.id = id;
        this.clienteId = clienteId;
        this.montoSolicitado = montoSolicitado;
        this.plazoDias = plazoDias;
        this.detalles = detalles;
        this.estado = estado;
        this.fechaSolicitud = fechaSolicitud;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getClienteId() { return clienteId; }
    public void setClienteId(Long clienteId) { this.clienteId = clienteId; }

    public BigDecimal getMontoSolicitado() { return montoSolicitado; }
    public void setMontoSolicitado(BigDecimal montoSolicitado) { this.montoSolicitado = montoSolicitado; }

    public Integer getPlazoDias() { return plazoDias; }
    public void setPlazoDias(Integer plazoDias) { this.plazoDias = plazoDias; }

    public String getDetalles() { return detalles; }
    public void setDetalles(String detalles) { this.detalles = detalles; }

    public PrestamoSolicitud.EstadoSolicitud getEstado() { return estado; }
    public void setEstado(PrestamoSolicitud.EstadoSolicitud estado) { this.estado = estado; }

    public LocalDate getFechaSolicitud() { return fechaSolicitud; }
    public void setFechaSolicitud(LocalDate fechaSolicitud) { this.fechaSolicitud = fechaSolicitud; }
}
