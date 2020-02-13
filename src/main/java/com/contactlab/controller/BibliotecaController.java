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
import java.util.List;

@RestController
public class BibliotecaController {

    @Autowired
    private ServizioBiblioteca servizioBiblioteca;

    /***********************************************************************************************/

    /**  1. registrazione cliente  **/

    @GetMapping("/caricacliente")
    public void caricaCliente(@RequestParam(value = "fileCliente") String fileCliente) {

        try {
            servizioBiblioteca.caricaCliente(fileCliente);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /** 2. registrazione libro  **/

    @GetMapping("/caricalibro")
    public void caricaLibro(@RequestParam(value = "fileLibro") String fileLibro) {

        try {
            servizioBiblioteca.caricaLibro(fileLibro);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**  6. registrazione prestito  **/

    @GetMapping("/caricaprestiti")
    public void caricaPrestito(@RequestParam(value = "filePrestito") String filePrestito) {

        try {
            servizioBiblioteca.addPrestito(filePrestito);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**   restituizione prestito  **/

    @GetMapping("/restituisciprestiti")
    public void restituisciPrestito(@RequestParam(value = "fileRestituisci") String fileRestituisci) {

        try {
            servizioBiblioteca.restituisciLibro(fileRestituisci);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /**
     *      3. modifica numero di telefono cliente
     *      4. modifica email cliente
     *      5. modifica residenza cliente
     **/

    @GetMapping("/modificaCliente")
    public List<Cliente> modifica(@RequestParam(value = "id_cliente") int idCliente, @RequestParam(value = "telefono") String telefono, @RequestParam(value = "residenza") String residenza) throws SQLException {

        servizioBiblioteca.modificaTelefonoResidenza(telefono, residenza, idCliente);

        return servizioBiblioteca.trovaClienti();

    }



    /** 7. verifica sul registro se ci sono presiti scaduti **/


    @GetMapping("/prestitiscaduti")
    public void prestitiScaduti(@RequestParam(value = "fileCheckPrenotazioni") String fileCheckPrenotazioni) {

        try {
            servizioBiblioteca.checkPrenotazioniPerUtente(fileCheckPrenotazioni);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


    /** 9.  stampare tutti i libri prendendo in input l'autore **/

    @GetMapping("/libroautore")
    public List<Libro> getLibroPerAutore(@RequestParam(value = "autore") String autore) throws SQLException {

        return servizioBiblioteca.getLibroPerAutore(autore);
    }


    /** 10. stampare tutti i clienti che hanno preso in prestito i libri di un autore passato in input **/

    @GetMapping("/clientiautore")
    public List<Cliente> getClientiAutore(@RequestParam(value = "autore") String autore) throws SQLException {

        return servizioBiblioteca.getClientiPerAutore(autore);
    }






    /** Stampa libri **/

    @GetMapping("/stampalibri")
    public List<Libro> getLibri() throws SQLException {

        return servizioBiblioteca.trovaLibri();
    }

    /** Stampa clienti **/

    @GetMapping("/stampaclienti")
    public List<Cliente> getClienti() throws SQLException {

        return servizioBiblioteca.trovaClienti();
    }

    /** Stampa prestiti **/

    @GetMapping("/stampaprestiti")
    public List<Prestito> getPrestiti() throws SQLException {

         return servizioBiblioteca.trovaPrestiti();
    }




}
