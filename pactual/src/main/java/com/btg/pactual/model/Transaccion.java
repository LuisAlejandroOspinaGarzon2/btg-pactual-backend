package com.btg.pactual.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "transacciones")
public class Transaccion {
    
    @Id
    private String id;
    
    private String fondoId;
    private String clienteId;
    private String tipo; // "APERTURA" o "CANCELACION"
    private Double monto;
    private LocalDateTime fecha;
}
