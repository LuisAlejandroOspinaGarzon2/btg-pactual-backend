package com.btg.pactual.repository;

import com.btg.pactual.model.Cliente;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface ClienteRepository extends MongoRepository<Cliente, String> {
    Optional<Cliente> findByEmail(String email);
}
