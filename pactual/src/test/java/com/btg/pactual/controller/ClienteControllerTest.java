package com.btg.pactual.controller;

import com.btg.pactual.model.Cliente;
import com.btg.pactual.repository.ClienteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
public class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteRepository clienteRepository;

    @Test
    public void testGetAllClientes() throws Exception {
        Cliente cliente1 = new Cliente("1", "Juan Pérez", "juan.perez@example.com", "3001234567", 500000.0);
        Cliente cliente2 = new Cliente("2", "María Gómez", "maria.gomez@example.com", "3007654321", 500000.0);

        Mockito.when(clienteRepository.findAll()).thenReturn(Arrays.asList(cliente1, cliente2));

        mockMvc.perform(get("/api/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$[1].nombre", is("María Gómez")));
    }

    @Test
    public void testCreateCliente() throws Exception {
        Cliente cliente = new Cliente("1", "Juan Pérez", "juan.perez@example.com", "3001234567", 500000.0);

        Mockito.when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        String clienteJson = "{ \"nombre\": \"Juan Pérez\", \"email\": \"juan.perez@example.com\", \"telefono\": \"3001234567\" }";

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$.email", is("juan.perez@example.com")))
                .andExpect(jsonPath("$.telefono", is("3001234567")));
    }

    @Test
    public void testGetClienteById() throws Exception {
        String clienteId = "1";
        Cliente cliente = new Cliente(clienteId, "Juan Pérez", "juan.perez@example.com", "3001234567", 500000.0);

        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/clientes/{id}", clienteId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan Pérez")))
                .andExpect(jsonPath("$.email", is("juan.perez@example.com")))
                .andExpect(jsonPath("$.telefono", is("3001234567")));
    }

    @Test
    public void testUpdateCliente() throws Exception {
        String clienteId = "1";
        Cliente existingCliente = new Cliente(clienteId, "Juan Pérez", "juan.perez@example.com", "3001234567", 500000.0);
        Cliente updatedCliente = new Cliente(clienteId, "Juan Pérez Actualizado", "juan.perez@example.com", "3001234567", 500000.0);

        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(existingCliente));
        Mockito.when(clienteRepository.save(any(Cliente.class))).thenReturn(updatedCliente);

        String clienteJson = "{ \"nombre\": \"Juan Pérez Actualizado\", \"email\": \"juan.perez@example.com\", \"telefono\": \"3001234567\" }";

        mockMvc.perform(put("/api/clientes/{id}", clienteId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(clienteJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan Pérez Actualizado")));
    }

    @Test
    public void testDeleteCliente() throws Exception {
        String clienteId = "1";
        Cliente cliente = new Cliente(clienteId, "Juan Pérez", "juan.perez@example.com", "3001234567", 500000.0);

        Mockito.when(clienteRepository.findById(clienteId)).thenReturn(Optional.of(cliente));

        mockMvc.perform(delete("/api/clientes/{id}", clienteId))
                .andExpect(status().isNoContent());
    }
}
