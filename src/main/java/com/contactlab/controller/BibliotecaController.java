package com.contactlab.controller;


import com.contactlab.data.Cliente;
import com.contactlab.data.Libro;
import com.contactlab.data.Prestito;
import com.contactlab.service.ServizioBiblioteca;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class BibliotecaController {

    private static final Logger logger = LoggerFactory.getLogger(BibliotecaController.class);


    @Autowired
    private ServizioBiblioteca servizioBiblioteca;

    /***********************************************************************************************/

    /**
     * 1. registrazione cliente
     **/

    @GetMapping("/caricacliente")  // C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\clienti.txt
    public ResponseEntity<Void> caricaCliente(@RequestParam(value = "fileCliente") String fileCliente) {

        logger.info("Chiamata nel metodo caricaCliente");

        servizioBiblioteca.caricaCliente(fileCliente);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 2. registrazione libro
     **/

    @GetMapping("/caricalibro") // C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\libri.txt
    public void caricaLibro(@RequestParam(value = "fileLibro") String fileLibro) {

        logger.info("Chiamata nel metodo caricaLibro");

        servizioBiblioteca.caricaLibro(fileLibro);

    }

    /**
     * 6. registrazione prestito
     **/

    @GetMapping("/caricaprestiti")    // C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\prestito.txt
    public void caricaPrestito(@RequestParam(value = "filePrestito") String filePrestito) {

        logger.info("Chiamata nel metodo caricaPrestito");

        servizioBiblioteca.addPrestito(filePrestito);

    }

    /**
     * restituizione prestito
     **/

    @GetMapping("/restituisciprestiti")
    public void restituisciPrestito(@RequestParam(value = "fileRestituisci") String fileRestituisci) {
        logger.info("Chiamata nel metodo restituisciPrestito");
        servizioBiblioteca.restituisciLibro(fileRestituisci);

    }


    /**
     * 3. modifica numero di telefono cliente
     * 4. modifica email cliente
     * 5. modifica residenza cliente
     **/

    @GetMapping("/modificaCliente")
    public List<Cliente> modifica(@RequestParam(value = "id_cliente") int idCliente, @RequestParam(value = "telefono") String telefono, @RequestParam(value = "residenza") String residenza) throws SQLException {

        logger.info("Chiamata nel metodo modifica per modificare telefono o residenza");
        servizioBiblioteca.modificaTelefonoResidenza(telefono, residenza, idCliente);

        logger.info("Chiamata nel metodo trovaClienti per verificare le modifiche");
        return servizioBiblioteca.trovaClienti();

    }


    /**
     * 7. verifica sul registro se ci sono presiti scaduti
     **/

    @GetMapping("/prestitiscaduti")
    public void prestitiScaduti() {

        logger.info("Chiamata nel metodo prestitiScaduti");

        servizioBiblioteca.checkPrestitiScaduti();

    }

    /**
     * 8. verifica sul registro se ci sono presiti scaduti per utente
     **/

    @GetMapping("/prestitiscadutiutente")
    public void prestitiScaduti(@RequestParam(value = "fileCheckPrenotazioni") String fileCheckPrenotazioni) {

        logger.info("Chiamata nel metodo prestitiScaduti");
        servizioBiblioteca.checkPrenotazioniPerUtente(fileCheckPrenotazioni);
    }


    /**
     * 9.  stampare tutti i libri prendendo in input l'autore
     **/

    @GetMapping("/libroautore")
    public List<Libro> getLibroPerAutore(@RequestParam(value = "autore") String autore) {

        logger.info("Chiamata nel metodo getLibroPerAutore");
        return servizioBiblioteca.getLibroPerAutore(autore);
    }


    /**
     * 10. stampare tutti i clienti che hanno preso in prestito i libri di un autore passato in input
     **/

    @GetMapping("/clientiautore")
    public List<Cliente> getClientiAutore(@RequestParam(value = "autore") String autore) {

        logger.info("Chiamata nel metodo getClientiAutore");
        return servizioBiblioteca.getClientiPerAutore(autore);
    }

    /**
     * 11. stampare la classifica dei libri più chiesti in prestito (info libro + numero di prestiti)
     **/


    @GetMapping("/classificalibri")
    public Map<Libro, Integer> getClassificaLibri() {

        logger.info("Chiamata nel metodo getClassificaLibri");

        return servizioBiblioteca.classificaLibri();

    }


    /**
     * 12. stampare la classifica dei clienti che hanno letto più libri (info clienti + numero libri letti)
     **/

    @GetMapping("/classificaclientilibri")
    public Map<Cliente, Integer> getClassificaClientiLibri() {

        logger.info("Chiamata nel metodo getClassificaClientiLibri");

        return servizioBiblioteca.classificaClienti();
    }


    /**
     * 13. stampare la classifica dei generi di libri più letti (genere + numero libri letti)
     **/

    @GetMapping("/classificagenerelibro")
    public Map<String, Integer> getgenereLibro() {

        logger.info("Chiamata nel metodo getgenereLibro");

        return servizioBiblioteca.classificaGenereLibri();
    }


    /**
     * 14. per ogni cliente, stampare la classifica dei generi più letti (genere + numero libri letti)
     **/

    @GetMapping("/classificaclientegenere")
    public Map<Cliente, Map<String, Integer>> getClientegenereLibro() {

        logger.info("Chiamata nel metodo getClientegenereLibro");
        return servizioBiblioteca.classificaGenereLibriCliente();
    }

    /**
     * Stampa libri
     **/

    @GetMapping("/stampalibri")
    public List<Libro> getLibri() {
        logger.info("Chiamata nel metodo getLibri");
        return servizioBiblioteca.trovaLibri();
    }

    /**
     * Stampa clienti
     **/

    @GetMapping("/stampaclienti")
    public ResponseEntity<List<Cliente>> getClienti() {
        logger.info("Chiamata nel metodo getClienti");
        return new ResponseEntity<>(servizioBiblioteca.trovaClienti(), HttpStatus.OK);
    }

    /**
     * Stampa prestiti
     **/

    @GetMapping("/stampaprestiti")
    public List<Prestito> getPrestiti() {
        logger.info("Chiamata nel metodo getPrestiti");
        return servizioBiblioteca.trovaPrestiti();
    }


}
