package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.service;

import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.Pago;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.PrestamoAprobado;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.repository.PagoRepository;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.repository.PrestamoAprobadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PagoService {

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private PrestamoAprobadoRepository prestamoAprobadoRepository;

    @Transactional
    public Pago registrarPago(Pago pago) {
        PrestamoAprobado prestamo = pago.getPrestamoAprobado();
        
        // Guardar el pago
        Pago pagGuardado = pagoRepository.save(pago);

        // Verificar si el prestamo esta totalmente pagado
        BigDecimal saldoPendiente = prestamo.getSaldoPendiente();
        if (saldoPendiente.compareTo(BigDecimal.ZERO) <= 0) {
            prestamo.setEstado(PrestamoAprobado.EstadoPago.PAGADO);
            prestamoAprobadoRepository.save(prestamo);
        }

        return pagGuardado;
    }

    public Optional<Pago> obtenerPagoPorId(Long id) {
        return pagoRepository.findById(id);
    }

    public List<Pago> obtenerPagosDelPrestamo(Long prestamoAprobadoId) {
        return pagoRepository.findByPrestamoAprobadoId(prestamoAprobadoId);
    }

    public List<Pago> obtenerTodosLosPagos() {
        return pagoRepository.findAll();
    }

    public BigDecimal obtenerTotalPagado(Long prestamoAprobadoId) {
        List<Pago> pagos = pagoRepository.findByPrestamoAprobadoId(prestamoAprobadoId);
        return pagos.stream()
                .map(Pago::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
