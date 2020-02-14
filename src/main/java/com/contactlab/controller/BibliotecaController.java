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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class BibliotecaController {

    private static final Logger logger = LoggerFactory.getLogger(BibliotecaController.class);


    @Autowired
    private ServizioBiblioteca servizioBiblioteca;

    /***********************************************************************************************/

    /**
     * 1. registrazione cliente
     **/

    @GetMapping("/caricacliente")
    public ResponseEntity<Void> caricaCliente(@RequestParam(value = "fileCliente") String fileCliente) {

        logger.info("Chiamata nel metodo caricaCliente");

        try {
            servizioBiblioteca.caricaCliente(fileCliente);

        } catch (SQLException e) {
            logger.error("SQLException nel metodo caricaCliente ", e);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 2. registrazione libro
     **/

    @GetMapping("/caricalibro")
    public void caricaLibro(@RequestParam(value = "fileLibro") String fileLibro) {

        logger.info("Chiamata nel metodo caricaLibro");

        try {
            servizioBiblioteca.caricaLibro(fileLibro);
        } catch (SQLException e) {
            logger.error("SQLException nel metodo caricaLibro ", e);
        }

    }

    /**
     * 6. registrazione prestito
     **/

    @GetMapping("/caricaprestiti")
    public void caricaPrestito(@RequestParam(value = "filePrestito") String filePrestito) {

        logger.info("Chiamata nel metodo caricaPrestito");

        try {
            servizioBiblioteca.addPrestito(filePrestito);
        } catch (SQLException e) {
            logger.error("SQLException nel metodo caricaPrestito ", e);
        }

    }

    /**
     * restituizione prestito
     **/

    @GetMapping("/restituisciprestiti")
    public void restituisciPrestito(@RequestParam(value = "fileRestituisci") String fileRestituisci) {
        logger.info("Chiamata nel metodo restituisciPrestito");
        try {
            servizioBiblioteca.restituisciLibro(fileRestituisci);
        } catch (SQLException e) {
            logger.error("SQLException nel metodo restituisciPrestito ", e);
        }

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
    public void prestitiScaduti(@RequestParam(value = "fileCheckPrenotazioni") String fileCheckPrenotazioni) {

        logger.info("Chiamata nel metodo prestitiScaduti");
        try {
            servizioBiblioteca.checkPrenotazioniPerUtente(fileCheckPrenotazioni);
        } catch (SQLException e) {
            logger.error("SQLException nel metodo prestitiScaduti ", e);
        }

    }


    /**
     * 9.  stampare tutti i libri prendendo in input l'autore
     **/

    @GetMapping("/libroautore")
    public List<Libro> getLibroPerAutore(@RequestParam(value = "autore") String autore) throws SQLException {

        logger.info("Chiamata nel metodo getLibroPerAutore");
        return servizioBiblioteca.getLibroPerAutore(autore);
    }


    /**
     * 10. stampare tutti i clienti che hanno preso in prestito i libri di un autore passato in input
     **/

    @GetMapping("/clientiautore")
    public List<Cliente> getClientiAutore(@RequestParam(value = "autore") String autore) throws SQLException {

        logger.info("Chiamata nel metodo getClientiAutore");
        return servizioBiblioteca.getClientiPerAutore(autore);
    }

    /**
     * 11. stampare la classifica dei libri più chiesti in prestito (info libro + numero di prestiti)
     **/


    @GetMapping("/classificalibri")
    public Map<Libro, Integer> getClassificaLibri() throws SQLException {

        logger.info("Chiamata nel metodo getClassificaLibri");
        Map<Libro, Integer> mapLibro = servizioBiblioteca.classificaLibri();
        Map<Libro, Integer> mapLibro2 = new HashMap<>();
        mapLibro.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                forEach(stringIntegerEntry -> mapLibro2.put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue()));

        return mapLibro2;

    }


    /**
     * 12. stampare la classifica dei clienti che hanno letto più libri (info clienti + numero libri letti)
     **/

    @GetMapping("/classificaclientilibri")
    public Map<Cliente, Integer> getClassificaClientiLibri() throws SQLException {

        logger.info("Chiamata nel metodo getClassificaClientiLibri");
        Map<Cliente, Integer> mapClienti = servizioBiblioteca.classificaClienti();
        Map<Cliente, Integer> mapClienti2 = new HashMap<>();

        mapClienti.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                forEach(stringIntegerEntry -> mapClienti2.put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue()));

        return mapClienti2;

    }


    /**
     * 13. stampare la classifica dei generi di libri più letti (genere + numero libri letti)
     **/

    @GetMapping("/classificagenerelibro")
    public Map<String, Integer> getgenereLibro() throws SQLException {

        logger.info("Chiamata nel metodo getgenereLibro");
        Map<String, Integer> mapGenere = servizioBiblioteca.classificaGenereLibri();
        Map<String, Integer> mapGenere2 = new HashMap<>();

        mapGenere.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                forEach(stringIntegerEntry -> mapGenere2.put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue()));

        return mapGenere2;
    }


    /**
     * 14. per ogni cliente, stampare la classifica dei generi più letti (genere + numero libri letti)
     **/

    @GetMapping("/classificaclientegenere")
    public  Map<Cliente, Map<String, Integer>> getClientegenereLibro() throws SQLException {

        logger.info("Chiamata nel metodo getClientegenereLibro");
        Map<Cliente, Map<String, Integer>> map3 = servizioBiblioteca.classificaGenereLibriCliente();
        Map<Cliente, Map<String, Integer>> map4 = new HashMap<>();

        map3.forEach((cliente, stringIntegerMap) -> {

            Map<String, Integer> map = new HashMap<>();
            stringIntegerMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                    forEach(stringIntegerEntry -> map.put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue()));
            map4.put(cliente,map);
        });

        return map4;

    }

    /**
     * Stampa libri
     **/

    @GetMapping("/stampalibri")
    public List<Libro> getLibri() throws SQLException {
        logger.info("Chiamata nel metodo getLibri");
        return servizioBiblioteca.trovaLibri();
    }

    /**
     * Stampa clienti
     **/

    @GetMapping("/stampaclienti")
    public ResponseEntity<List<Cliente>> getClienti() throws SQLException {
        logger.info("Chiamata nel metodo getClienti");
        return new ResponseEntity<>(servizioBiblioteca.trovaClienti(),HttpStatus.OK) ;
    }

    /**
     * Stampa prestiti
     **/

    @GetMapping("/stampaprestiti")
    public List<Prestito> getPrestiti() throws SQLException {
        logger.info("Chiamata nel metodo getPrestiti");
        return servizioBiblioteca.trovaPrestiti();
    }


}
