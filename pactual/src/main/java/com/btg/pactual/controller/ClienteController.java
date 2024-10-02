package com.btg.pactual.controller;

import com.btg.pactual.model.Cliente;
import com.btg.pactual.repository.ClienteRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public List<Cliente> getAllClientes() {
        return clienteRepository.findAll();
    }

    @PostMapping
    public Cliente createCliente(@Valid @RequestBody Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> getClienteById(@PathVariable String id) {
        Optional<Cliente> cliente = clienteRepository.findById(id);
        return cliente.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cliente> updateCliente(@PathVariable String id, @Valid @RequestBody Cliente clienteDetails) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (!clienteOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Cliente cliente = clienteOptional.get();
        cliente.setNombre(clienteDetails.getNombre());
        cliente.setEmail(clienteDetails.getEmail());
        cliente.setTelefono(clienteDetails.getTelefono());
        Cliente updatedCliente = clienteRepository.save(cliente);
        return ResponseEntity.ok(updatedCliente);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCliente(@PathVariable String id) {
        Optional<Cliente> clienteOptional = clienteRepository.findById(id);
        if (!clienteOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        clienteRepository.delete(clienteOptional.get());
        return ResponseEntity.noContent().build();
    }
}
