package com.contactlab.repository;

import com.contactlab.data.Prestito;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
    public interface PrestitoRepository extends CrudRepository<Prestito, Integer> {

        List<Prestito> findAll();
        long countByIdClienteAndRestituito(Integer idCliente, String restituito);
        Optional<Prestito> findPrestitoByIdClienteAndIdLibro(Integer idCliente, Integer idLibro);
        Optional<Prestito> findPrestitoByIdLibro(Integer idLibro);

    }



