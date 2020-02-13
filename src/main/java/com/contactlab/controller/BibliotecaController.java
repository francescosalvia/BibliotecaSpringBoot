package com.contactlab.controller;


import com.contactlab.data.Cliente;
import com.contactlab.data.Libro;
import com.contactlab.data.Prestito;
import com.contactlab.service.ServizioBiblioteca;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ServizioBiblioteca servizioBiblioteca;

    /***********************************************************************************************/

    /**
     * 1. registrazione cliente
     **/

    @GetMapping("/caricacliente")
    public void caricaCliente(@RequestParam(value = "fileCliente") String fileCliente) {

        try {
            servizioBiblioteca.caricaCliente(fileCliente);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 2. registrazione libro
     **/

    @GetMapping("/caricalibro")
    public void caricaLibro(@RequestParam(value = "fileLibro") String fileLibro) {

        try {
            servizioBiblioteca.caricaLibro(fileLibro);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * 6. registrazione prestito
     **/

    @GetMapping("/caricaprestiti")
    public void caricaPrestito(@RequestParam(value = "filePrestito") String filePrestito) {

        try {
            servizioBiblioteca.addPrestito(filePrestito);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * restituizione prestito
     **/

    @GetMapping("/restituisciprestiti")
    public void restituisciPrestito(@RequestParam(value = "fileRestituisci") String fileRestituisci) {

        try {
            servizioBiblioteca.restituisciLibro(fileRestituisci);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * 3. modifica numero di telefono cliente
     * 4. modifica email cliente
     * 5. modifica residenza cliente
     **/

    @GetMapping("/modificaCliente")
    public List<Cliente> modifica(@RequestParam(value = "id_cliente") int idCliente, @RequestParam(value = "telefono") String telefono, @RequestParam(value = "residenza") String residenza) throws SQLException {

        servizioBiblioteca.modificaTelefonoResidenza(telefono, residenza, idCliente);

        return servizioBiblioteca.trovaClienti();

    }


    /**
     * 7. verifica sul registro se ci sono presiti scaduti
     **/


    @GetMapping("/prestitiscaduti")
    public void prestitiScaduti(@RequestParam(value = "fileCheckPrenotazioni") String fileCheckPrenotazioni) {

        try {
            servizioBiblioteca.checkPrenotazioniPerUtente(fileCheckPrenotazioni);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     * 9.  stampare tutti i libri prendendo in input l'autore
     **/

    @GetMapping("/libroautore")
    public List<Libro> getLibroPerAutore(@RequestParam(value = "autore") String autore) throws SQLException {

        return servizioBiblioteca.getLibroPerAutore(autore);
    }


    /**
     * 10. stampare tutti i clienti che hanno preso in prestito i libri di un autore passato in input
     **/

    @GetMapping("/clientiautore")
    public List<Cliente> getClientiAutore(@RequestParam(value = "autore") String autore) throws SQLException {

        return servizioBiblioteca.getClientiPerAutore(autore);
    }

    /**
     * 11. stampare la classifica dei libri più chiesti in prestito (info libro + numero di prestiti)
     **/


    @GetMapping("/classificalibri")
    public Map<Libro, Integer> getClassificaLibri() throws SQLException {

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

        return servizioBiblioteca.trovaLibri();
    }

    /**
     * Stampa clienti
     **/

    @GetMapping("/stampaclienti")
    public List<Cliente> getClienti() throws SQLException {

        return servizioBiblioteca.trovaClienti();
    }

    /**
     * Stampa prestiti
     **/

    @GetMapping("/stampaprestiti")
    public List<Prestito> getPrestiti() throws SQLException {

        return servizioBiblioteca.trovaPrestiti();
    }


}
