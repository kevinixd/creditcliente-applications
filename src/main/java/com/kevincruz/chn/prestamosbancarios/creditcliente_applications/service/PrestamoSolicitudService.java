package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.service;

import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.PrestamoSolicitud;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.Cliente;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.PrestamoAprobado;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.repository.PrestamoSolicitudRepository;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.repository.ClienteRepository;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.repository.PrestamoAprobadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PrestamoSolicitudService {

    @Autowired
    private PrestamoSolicitudRepository prestamoSolicitudRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PrestamoAprobadoRepository prestamoAprobadoRepository;

    public PrestamoSolicitud crearSolicitud(PrestamoSolicitud solicitud) {
        solicitud.setEstado(PrestamoSolicitud.EstadoSolicitud.EN_PROCESO);
        return prestamoSolicitudRepository.save(solicitud);
    }

    public Optional<PrestamoSolicitud> obtenerSolicitudPorId(Long id) {
        return prestamoSolicitudRepository.findById(id);
    }

    public List<PrestamoSolicitud> obtenerSolicitudesDelCliente(Long clienteId) {
        return prestamoSolicitudRepository.findByClienteId(clienteId);
    }

    public List<PrestamoSolicitud> obtenerSolicitudesPendientes() {
        return prestamoSolicitudRepository.findByEstado(PrestamoSolicitud.EstadoSolicitud.EN_PROCESO);
    }

    public List<PrestamoSolicitud> obtenerTodasLasSolicitudes() {
        return prestamoSolicitudRepository.findAll();
    }

    @Transactional
    public PrestamoSolicitud aprobarSolicitud(Long solicitudId, BigDecimal tasaInteres) {
        PrestamoSolicitud solicitud = prestamoSolicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        // Crear préstamo aprobado
        PrestamoAprobado prestamoAprobado = new PrestamoAprobado();
        prestamoAprobado.setCliente(solicitud.getCliente());
        prestamoAprobado.setMontoAprobado(solicitud.getMontoSolicitado());
        prestamoAprobado.setPlazoDias(solicitud.getPlazoDias());
        prestamoAprobado.setTasaInteres(tasaInteres);
        prestamoAprobado.setFechaVencimiento(LocalDate.now().plusDays(solicitud.getPlazoDias()));

        prestamoAprobadoRepository.save(prestamoAprobado);

        // Actualizar solicitud
        solicitud.setEstado(PrestamoSolicitud.EstadoSolicitud.APROBADO);
        solicitud.setFechaRevision(LocalDate.now());

        return prestamoSolicitudRepository.save(solicitud);
    }

    @Transactional
    public PrestamoSolicitud rechazarSolicitud(Long solicitudId, String motivo) {
        PrestamoSolicitud solicitud = prestamoSolicitudRepository.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        solicitud.setEstado(PrestamoSolicitud.EstadoSolicitud.RECHAZADO);
        solicitud.setFechaRevision(LocalDate.now());
        solicitud.setMotivoRechazo(motivo);

        return prestamoSolicitudRepository.save(solicitud);
    }
}
