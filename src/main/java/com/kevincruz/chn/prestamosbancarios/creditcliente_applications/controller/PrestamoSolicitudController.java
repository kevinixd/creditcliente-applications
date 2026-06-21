package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.controller;

import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.dto.PrestamoSolicitudDTO;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.PrestamoSolicitud;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.Cliente;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.service.PrestamoSolicitudService;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.service.ClienteService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.net.URI;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/prestamo-solicitudes")
@CrossOrigin(origins = "*")
public class PrestamoSolicitudController {

    @Autowired
    private PrestamoSolicitudService prestamoSolicitudService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<PrestamoSolicitudDTO> crearSolicitud(@Valid @RequestBody PrestamoSolicitudDTO solicitudDTO) {
        Cliente cliente = clienteService.obtenerClientePorId(solicitudDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        PrestamoSolicitud solicitud = modelMapper.map(solicitudDTO, PrestamoSolicitud.class);
        solicitud.setCliente(cliente);
        
        PrestamoSolicitud solicitudGuardada = prestamoSolicitudService.crearSolicitud(solicitud);
        PrestamoSolicitudDTO dto = modelMapper.map(solicitudGuardada, PrestamoSolicitudDTO.class);
        if (solicitudGuardada.getCliente() != null) {
            dto.setClienteId(solicitudGuardada.getCliente().getId());
        }
        URI location = URI.create(String.format("/api/prestamo-solicitudes/%d", solicitudGuardada.getId()));
        return ResponseEntity.created(location).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoSolicitudDTO> obtenerSolicitud(@PathVariable Long id) {
        return prestamoSolicitudService.obtenerSolicitudPorId(id)
                .map(solicitud -> ResponseEntity.ok(modelMapper.map(solicitud, PrestamoSolicitudDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PrestamoSolicitudDTO>> obtenerSolicitudesDelCliente(@PathVariable Long clienteId) {
        List<PrestamoSolicitudDTO> solicitudes = prestamoSolicitudService.obtenerSolicitudesDelCliente(clienteId)
                .stream()
                .map(solicitud -> {
                    PrestamoSolicitudDTO dto = modelMapper.map(solicitud, PrestamoSolicitudDTO.class);
                    dto.setClienteId(solicitud.getCliente().getId());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<PrestamoSolicitudDTO>> obtenerSolicitudesPendientes() {
        List<PrestamoSolicitudDTO> solicitudes = prestamoSolicitudService.obtenerSolicitudesPendientes()
                .stream()
                .map(solicitud -> {
                    PrestamoSolicitudDTO dto = modelMapper.map(solicitud, PrestamoSolicitudDTO.class);
                    dto.setClienteId(solicitud.getCliente().getId());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(solicitudes);
    }

    @GetMapping
    public ResponseEntity<List<PrestamoSolicitudDTO>> obtenerTodasLasSolicitudes() {
        List<PrestamoSolicitudDTO> solicitudes = prestamoSolicitudService.obtenerTodasLasSolicitudes()
                .stream()
                .map(solicitud -> {
                    PrestamoSolicitudDTO dto = modelMapper.map(solicitud, PrestamoSolicitudDTO.class);
                    dto.setClienteId(solicitud.getCliente().getId());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(solicitudes);
    }

    @PostMapping("/{id}/aprobar")
    public ResponseEntity<PrestamoSolicitudDTO> aprobarSolicitud(
            @PathVariable Long id,
            @RequestParam BigDecimal tasaInteres) {
        PrestamoSolicitud solicitud = prestamoSolicitudService.aprobarSolicitud(id, tasaInteres);
        PrestamoSolicitudDTO dto = modelMapper.map(solicitud, PrestamoSolicitudDTO.class);
        dto.setClienteId(solicitud.getCliente().getId());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/{id}/rechazar")
    public ResponseEntity<PrestamoSolicitudDTO> rechazarSolicitud(
            @PathVariable Long id,
            @RequestParam String motivo) {
        PrestamoSolicitud solicitud = prestamoSolicitudService.rechazarSolicitud(id, motivo);
        PrestamoSolicitudDTO dto = modelMapper.map(solicitud, PrestamoSolicitudDTO.class);
        dto.setClienteId(solicitud.getCliente().getId());
        return ResponseEntity.ok(dto);
    }
}
