package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.repository;

import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.PrestamoSolicitud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoSolicitudRepository extends JpaRepository<PrestamoSolicitud, Long> {
    List<PrestamoSolicitud> findByClienteId(Long clienteId);
    List<PrestamoSolicitud> findByEstado(PrestamoSolicitud.EstadoSolicitud estado);
}
