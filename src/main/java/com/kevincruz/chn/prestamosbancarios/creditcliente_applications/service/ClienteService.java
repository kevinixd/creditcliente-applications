package com.kevincruz.chn.prestamosbancarios.creditcliente_applications.service;

import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.entity.Cliente;
import com.kevincruz.chn.prestamosbancarios.creditcliente_applications.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente crearCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> obtenerClientePorId(Long id) {
        return clienteRepository.findById(id);
    }

    public List<Cliente> obtenerTodosLosClientes() {
        return clienteRepository.findAll();
    }

    public Cliente actualizarCliente(Long id, Cliente clienteActualizado) {
        return clienteRepository.findById(id).map(cliente -> {
            cliente.setNombre(clienteActualizado.getNombre());
            cliente.setApellido(clienteActualizado.getApellido());
            cliente.setDireccion(clienteActualizado.getDireccion());
            cliente.setCorreoElectronico(clienteActualizado.getCorreoElectronico());
            cliente.setTelefono(clienteActualizado.getTelefono());
            return clienteRepository.save(cliente);
        }).orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }

    public void eliminarCliente(Long id) {
        clienteRepository.deleteById(id);
    }

    public Optional<Cliente> obtenerClientePorNumeroIdentificacion(String numeroIdentificacion) {
        return clienteRepository.findByNumeroIdentificacion(numeroIdentificacion);
    }

    public Optional<Cliente> obtenerClientePorCorreoElectronico(String correoElectronico) {
        return clienteRepository.findByCorreoElectronico(correoElectronico);
    }
}
