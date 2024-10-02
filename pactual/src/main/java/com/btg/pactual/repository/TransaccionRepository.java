package com.btg.pactual.repository;

import com.btg.pactual.model.Transaccion;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface TransaccionRepository extends MongoRepository<Transaccion, String> {
    List<Transaccion> findByClienteId(String clienteId);
    List<Transaccion> findByFondoId(String fondoId);
}
