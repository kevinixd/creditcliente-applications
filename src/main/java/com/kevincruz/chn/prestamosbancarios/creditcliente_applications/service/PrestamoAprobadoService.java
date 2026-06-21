package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.service;

import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.PrestamoAprobado;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.repository.PrestamoAprobadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PrestamoAprobadoService {

    @Autowired
    private PrestamoAprobadoRepository prestamoAprobadoRepository;

    public Optional<PrestamoAprobado> obtenerPrestamoPorId(Long id) {
        return prestamoAprobadoRepository.findById(id);
    }

    public List<PrestamoAprobado> obtenerPrestamosDelCliente(Long clienteId) {
        return prestamoAprobadoRepository.findByClienteId(clienteId);
    }

    public List<PrestamoAprobado> obtenerPrestamosActivos() {
        return prestamoAprobadoRepository.findByEstado(PrestamoAprobado.EstadoPago.ACTIVO);
    }

    public List<PrestamoAprobado> obtenerTodosLosPrestamos() {
        return prestamoAprobadoRepository.findAll();
    }

    public BigDecimal obtenerSaldoPendiente(Long prestamoId) {
        PrestamoAprobado prestamo = prestamoAprobadoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));
        return prestamo.getSaldoPendiente();
    }

    public BigDecimal calcularInteres(Long prestamoId) {
        PrestamoAprobado prestamo = prestamoAprobadoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));
        return prestamo.calcularInteres();
    }
}
