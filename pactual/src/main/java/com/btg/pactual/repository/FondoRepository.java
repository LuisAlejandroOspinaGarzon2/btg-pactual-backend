package com.btg.pactual.repository;

import com.btg.pactual.model.Fondo;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.Optional;

public interface FondoRepository extends MongoRepository<Fondo, String> {
    Optional<Fondo> findByNombre(String nombre);
}
