package com.btg.pactual.controller;

import com.btg.pactual.model.Cliente;
import com.btg.pactual.model.Fondo;
import com.btg.pactual.model.Transaccion;
import com.btg.pactual.repository.ClienteRepository;
import com.btg.pactual.repository.FondoRepository;
import com.btg.pactual.repository.TransaccionRepository;
import com.btg.pactual.service.EmailService;
import com.btg.pactual.service.SmsService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/fondos")
public class FondoController {

    @Autowired
    private SmsService smsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private FondoRepository fondoRepository;

    @Autowired
    private TransaccionRepository transaccionRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public List<Fondo> getAllFondos() {
        return fondoRepository.findAll();
    }

    @PostMapping
    public Fondo createFondo(@Valid @RequestBody Fondo fondo) {
        return fondoRepository.save(fondo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Fondo> getFondoById(@PathVariable String id) {
        Optional<Fondo> fondo = fondoRepository.findById(id);
        return fondo.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Fondo> updateFondo(@PathVariable String id, @Valid @RequestBody Fondo fondoDetails) {
        Optional<Fondo> fondoOptional = fondoRepository.findById(id);
        if (!fondoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Fondo fondo = fondoOptional.get();
        fondo.setNombre(fondoDetails.getNombre());
        fondo.setMonto(fondoDetails.getMonto());
        Fondo updatedFondo = fondoRepository.save(fondo);
        return ResponseEntity.ok(updatedFondo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFondo(@PathVariable String id) {
        Optional<Fondo> fondoOptional = fondoRepository.findById(id);
        if (!fondoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        fondoRepository.delete(fondoOptional.get());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/suscribir")
    public ResponseEntity<String> suscribirFondo(@PathVariable String id, @RequestParam String clienteId) {
        Optional<Fondo> fondoOptional = fondoRepository.findById(id);
        if (!fondoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Fondo fondo = fondoOptional.get();

        Optional<Cliente> clienteOptional = clienteRepository.findById(clienteId);
        if (!clienteOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Cliente no encontrado");
        }
        Cliente cliente = clienteOptional.get();

        double montoMinimo = fondo.getMonto();

        if (cliente.getSaldo() < montoMinimo) {
            return ResponseEntity.badRequest().body("No tiene saldo disponible para vincularse al fondo " + fondo.getNombre());
        }

        cliente.setSaldo(cliente.getSaldo() - montoMinimo);
        clienteRepository.save(cliente);

        Transaccion transaccion = new Transaccion();
        transaccion.setFondoId(id);
        transaccion.setClienteId(clienteId);
        transaccion.setTipo("APERTURA");
        transaccion.setMonto(montoMinimo);
        transaccion.setFecha(LocalDateTime.now());

        transaccionRepository.save(transaccion);

        emailService.sendEmail(
            cliente.getEmail(),
            "Suscripción al fondo " + fondo.getNombre(),
            "Te has suscrito exitosamente al fondo " + fondo.getNombre()
        );

        smsService.sendSms(
            cliente.getTelefono(),
            "Te has suscrito al fondo " + fondo.getNombre()
        );

        return ResponseEntity.ok("Suscripción exitosa al fondo " + fondo.getNombre() + ". Nuevo saldo: " + cliente.getSaldo());

    }

    @PostMapping("/{id}/cancelar")
    public ResponseEntity<String> cancelarFondo(@PathVariable String id, @RequestParam String clienteId) {
        Optional<Fondo> fondoOptional = fondoRepository.findById(id);
        if (!fondoOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Fondo fondo = fondoOptional.get();

        Optional<Cliente> clienteOptional = clienteRepository.findById(clienteId);
        if (!clienteOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Cliente no encontrado");
        }
        Cliente cliente = clienteOptional.get();

        double montoMinimo = fondo.getMonto();
        cliente.setSaldo(cliente.getSaldo() + montoMinimo);
        clienteRepository.save(cliente);

        Transaccion transaccion = new Transaccion();
        transaccion.setFondoId(id);
        transaccion.setClienteId(clienteId);
        transaccion.setTipo("CANCELACION");
        transaccion.setMonto(montoMinimo);
        transaccion.setFecha(LocalDateTime.now());

        transaccionRepository.save(transaccion);

        emailService.sendEmail(
            cliente.getEmail(),
            "Cancelación del fondo " + fondo.getNombre(),
            "Has cancelado tu suscripción al fondo " + fondo.getNombre()
        );

        smsService.sendSms(
            cliente.getTelefono(),
            "Has cancelado tu suscripción al fondo " + fondo.getNombre()
        );

        return ResponseEntity.ok("Cancelación exitosa del fondo " + fondo.getNombre() + ". Nuevo saldo: " + cliente.getSaldo());
    }

    @GetMapping("/cliente/{clienteId}/transacciones")
    public List<Transaccion> getTransaccionesByCliente(@PathVariable String clienteId) {
        return transaccionRepository.findByClienteId(clienteId);
    }
}
