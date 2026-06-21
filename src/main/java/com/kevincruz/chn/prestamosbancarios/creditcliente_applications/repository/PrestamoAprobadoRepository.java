package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.repository;

import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.PrestamoAprobado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoAprobadoRepository extends JpaRepository<PrestamoAprobado, Long> {
    List<PrestamoAprobado> findByClienteId(Long clienteId);
    List<PrestamoAprobado> findByEstado(PrestamoAprobado.EstadoPago estado);
}
