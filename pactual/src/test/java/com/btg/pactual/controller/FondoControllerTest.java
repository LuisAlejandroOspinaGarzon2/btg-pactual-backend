package com.btg.pactual.controller;

import com.btg.pactual.model.Cliente;
import com.btg.pactual.model.Fondo;
import com.btg.pactual.model.Transaccion;
import com.btg.pactual.repository.ClienteRepository;
import com.btg.pactual.repository.FondoRepository;
import com.btg.pactual.repository.TransaccionRepository;
import com.btg.pactual.service.EmailService;
import com.btg.pactual.service.SmsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FondoController.class)
public class FondoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FondoRepository fondoRepository;

    @MockBean
    private ClienteRepository clienteRepository;

    @MockBean
    private TransaccionRepository transaccionRepository;

    @MockBean
    private EmailService emailService;

    @MockBean
    private SmsService smsService;

    @Test
    public void testGetAllFondos() throws Exception {
        Fondo fondo1 = new Fondo("1", "FPV_BTG_PACTUAL_RECAUDADORA", 75000.0);
        Fondo fondo2 = new Fondo("2", "FPV_BTG_PACTUAL_ECOPETROL", 125000.0);

        Mockito.when(fondoRepository.findAll()).thenReturn(Arrays.asList(fondo1, fondo2));

        mockMvc.perform(get("/api/fondos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", org.hamcrest.Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", org.hamcrest.Matchers.is("FPV_BTG_PACTUAL_RECAUDADORA")))
                .andExpect(jsonPath("$[1].nombre", org.hamcrest.Matchers.is("FPV_BTG_PACTUAL_ECOPETROL")));
    }

    @Test
    public void testSuscribirFondo() throws Exception {
        String fondoId = "1";
        String clienteId = "100";

        Fondo fondo = new Fondo(fondoId, "FPV_BTG_PACTUAL_RECAUDADORA", 75000.0);
        Cliente cliente = new Cliente(clienteId, "Juan Pérez", "juan.perez@example.com", "3001234567", 500000.0);

        Mockito.when(fondoRepository.findById(fondoId)).thenReturn(Optional.of(fondo));
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        Mockito.when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        Mockito.when(transaccionRepository.save(any(Transaccion.class))).thenReturn(new Transaccion());

        mockMvc.perform(post("/api/fondos/{id}/suscribir", fondoId)
                .param("clienteId", clienteId))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Suscripción exitosa al fondo")));
    }

    @Test
    public void testCancelarFondo() throws Exception {
        String fondoId = "1";
        String clienteId = "100";

        Fondo fondo = new Fondo(fondoId, "FPV_BTG_PACTUAL_RECAUDADORA", 75000.0);
        Cliente cliente = new Cliente(clienteId, "Juan Pérez", "juan.perez@example.com", "3001234567", 425000.0);

        Mockito.when(fondoRepository.findById(fondoId)).thenReturn(Optional.of(fondo));
        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));
        Mockito.when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);
        Mockito.when(transaccionRepository.save(any(Transaccion.class))).thenReturn(new Transaccion());

        mockMvc.perform(post("/api/fondos/{id}/cancelar", fondoId)
                .param("clienteId", clienteId))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Cancelación exitosa del fondo")));
    }
}
