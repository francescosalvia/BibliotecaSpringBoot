package com.contactlab.repository;

import com.contactlab.data.Libro;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
    public interface LibroRepository extends CrudRepository<Libro, Integer> {

        List<Libro> findAll();
        Optional<Libro> findLibroByTitoloAndAutore(String titolo, String autore);



    }

