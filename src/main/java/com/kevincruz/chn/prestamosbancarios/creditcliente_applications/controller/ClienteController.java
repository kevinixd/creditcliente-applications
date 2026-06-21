package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.controller;

import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.dto.ClienteDTO;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.Cliente;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.service.ClienteService;

import jakarta.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "*")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ClienteDTO> crearCliente(@Valid @RequestBody ClienteDTO clienteDTO) {
        Cliente cliente = modelMapper.map(clienteDTO, Cliente.class);
        Cliente clienteGuardado = clienteService.crearCliente(cliente);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(clienteGuardado, ClienteDTO.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteDTO> obtenerCliente(@PathVariable Long id) {
        return clienteService.obtenerClientePorId(id)
                .map(cliente -> ResponseEntity.ok(modelMapper.map(cliente, ClienteDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> obtenerTodosLosClientes() {
        List<ClienteDTO> clientes = clienteService.obtenerTodosLosClientes()
                .stream()
                .map(cliente -> modelMapper.map(cliente, ClienteDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(clientes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizarCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
        Cliente clienteActualizado = modelMapper.map(clienteDTO, Cliente.class);
        Cliente cliente = clienteService.actualizarCliente(id, clienteActualizado);
        return ResponseEntity.ok(modelMapper.map(cliente, ClienteDTO.class));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/numero/{numeroIdentificacion}")
    public ResponseEntity<ClienteDTO> obtenerClientePorNumeroIdentificacion(@PathVariable String numeroIdentificacion) {
        return clienteService.obtenerClientePorNumeroIdentificacion(numeroIdentificacion)
                .map(cliente -> ResponseEntity.ok(modelMapper.map(cliente, ClienteDTO.class)))
                .orElse(ResponseEntity.notFound().build());
    }

    
}
