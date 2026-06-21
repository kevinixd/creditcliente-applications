package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.controller;

import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.dto.PrestamoAprobadoDTO;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.PrestamoAprobado;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.service.PrestamoAprobadoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/prestamos-aprobados")
@CrossOrigin(origins = "*")
public class PrestamoAprobadoController {

    @Autowired
    private PrestamoAprobadoService prestamoAprobadoService;

    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("/{id}")
    public ResponseEntity<PrestamoAprobadoDTO> obtenerPrestamo(@PathVariable Long id) {
        return prestamoAprobadoService.obtenerPrestamoPorId(id)
                .map(prestamo -> {
                    PrestamoAprobadoDTO dto = modelMapper.map(prestamo, PrestamoAprobadoDTO.class);
                    dto.setClienteId(prestamo.getCliente().getId());
                    dto.setSaldoPendiente(prestamo.getSaldoPendiente());
                    return ResponseEntity.ok(dto);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<PrestamoAprobadoDTO>> obtenerPrestamosDelCliente(@PathVariable Long clienteId) {
        List<PrestamoAprobadoDTO> prestamos = prestamoAprobadoService.obtenerPrestamosDelCliente(clienteId)
                .stream()
                .map(prestamo -> {
                    PrestamoAprobadoDTO dto = modelMapper.map(prestamo, PrestamoAprobadoDTO.class);
                    dto.setClienteId(prestamo.getCliente().getId());
                    dto.setSaldoPendiente(prestamo.getSaldoPendiente());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/activos")
    public ResponseEntity<List<PrestamoAprobadoDTO>> obtenerPrestamosActivos() {
        List<PrestamoAprobadoDTO> prestamos = prestamoAprobadoService.obtenerPrestamosActivos()
                .stream()
                .map(prestamo -> {
                    PrestamoAprobadoDTO dto = modelMapper.map(prestamo, PrestamoAprobadoDTO.class);
                    dto.setClienteId(prestamo.getCliente().getId());
                    dto.setSaldoPendiente(prestamo.getSaldoPendiente());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping
    public ResponseEntity<List<PrestamoAprobadoDTO>> obtenerTodosLosPrestamos() {
        List<PrestamoAprobadoDTO> prestamos = prestamoAprobadoService.obtenerTodosLosPrestamos()
                .stream()
                .map(prestamo -> {
                    PrestamoAprobadoDTO dto = modelMapper.map(prestamo, PrestamoAprobadoDTO.class);
                    dto.setClienteId(prestamo.getCliente().getId());
                    dto.setSaldoPendiente(prestamo.getSaldoPendiente());
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(prestamos);
    }

    @GetMapping("/{id}/saldo")
    public ResponseEntity<BigDecimal> obtenerSaldoPendiente(@PathVariable Long id) {
        BigDecimal saldo = prestamoAprobadoService.obtenerSaldoPendiente(id);
        return ResponseEntity.ok(saldo);
    }

    @GetMapping("/{id}/interes")
    public ResponseEntity<BigDecimal> calcularInteres(@PathVariable Long id) {
        BigDecimal interes = prestamoAprobadoService.calcularInteres(id);
        return ResponseEntity.ok(interes);
    }
}
