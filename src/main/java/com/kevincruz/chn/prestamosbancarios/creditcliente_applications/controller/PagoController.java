package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.controller;

import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.dto.PagoDTO;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.Pago;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.PrestamoAprobado;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.service.PagoService;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.service.PrestamoAprobadoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "*")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Autowired
    private PrestamoAprobadoService prestamoAprobadoService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<PagoDTO> registrarPago(@RequestBody PagoDTO pagoDTO) {
        PrestamoAprobado prestamo = prestamoAprobadoService.obtenerPrestamoPorId(pagoDTO.getPrestamoAprobadoId())
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));
        
        Pago pago = modelMapper.map(pagoDTO, Pago.class);
        pago.setPrestamoAprobado(prestamo);
        
        Pago pagoGuardado = pagoService.registrarPago(pago);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(pagoGuardado, PagoDTO.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoDTO> obtenerPago(@PathVariable Long id) {
        return pagoService.obtenerPagoPorId(id)
                .map(pago -> ResponseEntity.ok(modelMapper.map(pago, PagoDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/prestamo/{prestamoAprobadoId}")
    public ResponseEntity<List<PagoDTO>> obtenerPagosDelPrestamo(@PathVariable Long prestamoAprobadoId) {
        List<PagoDTO> pagos = pagoService.obtenerPagosDelPrestamo(prestamoAprobadoId)
                .stream()
                .map(pago -> modelMapper.map(pago, PagoDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(pagos);
    }

    @GetMapping
    public ResponseEntity<List<PagoDTO>> obtenerTodosLosPagos() {
        List<PagoDTO> pagos = pagoService.obtenerTodosLosPagos()
                .stream()
                .map(pago -> modelMapper.map(pago, PagoDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(pagos);
    }

    @GetMapping("/prestamo/{prestamoAprobadoId}/total-pagado")
    public ResponseEntity<BigDecimal> obtenerTotalPagado(@PathVariable Long prestamoAprobadoId) {
        BigDecimal totalPagado = pagoService.obtenerTotalPagado(prestamoAprobadoId);
        return ResponseEntity.ok(totalPagado);
    }
}
