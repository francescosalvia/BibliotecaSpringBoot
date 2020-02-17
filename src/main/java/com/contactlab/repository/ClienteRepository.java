package com.contactlab.repository;

import com.contactlab.data.Cliente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends CrudRepository<Cliente, Integer> {

    List<Cliente> findAll();
    Optional<Cliente> findClienteByEmail(String email);
    Optional<Cliente> findClienteByIdCliente(int idCliente);


}
