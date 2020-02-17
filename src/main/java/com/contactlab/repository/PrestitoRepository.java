package com.contactlab.repository;

import com.contactlab.data.Prestito;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
    public interface PrestitoRepository extends CrudRepository<Prestito, Integer> {

        List<Prestito> findAll();

    }



