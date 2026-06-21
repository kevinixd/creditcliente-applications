package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.repository;

import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByNumeroIdentificacion(String numeroIdentificacion);
    Optional<Cliente> findByCorreoElectronico(String correoElectronico);
}
