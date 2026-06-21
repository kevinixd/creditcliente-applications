package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.repository;

import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.Pago;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {
    List<Pago> findByPrestamoAprobadoId(Long prestamoAprobadoId);
}
