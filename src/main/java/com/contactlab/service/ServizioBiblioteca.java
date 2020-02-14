package com.contactlab.service;

import com.contactlab.controller.BibliotecaController;
import com.contactlab.dao.ClienteDao;
import com.contactlab.dao.LibroDao;
import com.contactlab.dao.PrenotazioneDao;
import com.contactlab.data.Cliente;
import com.contactlab.data.Libro;
import com.contactlab.data.Prestito;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;


@Service
public class ServizioBiblioteca {

    private static final Logger logger = LoggerFactory.getLogger(ServizioBiblioteca.class);

    @Autowired
    private LibroDao libroDao;

    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private PrenotazioneDao prenotazioneDao;


    /**
     * METODI UTILI
     **/

    public boolean controlloEmail(String email) {
        logger.info("Chiamata nel metodo controlloEmail");
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }


    public List<Cliente> trovaClienti() throws SQLException {

        return clienteDao.getClienti();
    }

    public List<Libro> trovaLibri() throws SQLException {
        return libroDao.getLibro();
    }

    public List<Prestito> trovaPrestiti() throws SQLException {
        return prenotazioneDao.getPrestito();
    }


    /**
     * LEGGI FILE CON I LIBRI  -> PUNTO 1
     **/

    public void caricaLibro(String file) throws SQLException {

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {
                String[] details = line.split(";");

                if (details.length == 5) {

                    String titolo = details[0];
                    String autore = details[1];
                    String data = details[2];
                    String genere = details[3];
                    String disponibile = details[4];

                    Optional<Integer> idLibro = libroDao.getIdLibro(titolo, autore);

                    if (idLibro.isPresent()) {
                        libroDao.updateLibro(titolo, autore, data, genere, idLibro.get());  // disponibilità non la modifico per non influire su eventuali prestiti
                    } else {
                        libroDao.insertLibro(titolo, autore, data, genere, disponibile);
                    }
                }
            }
            logger.info("I libri nel file sono stati caricati");
        } catch (IOException e) {
            logger.error("IOException nel metodo caricaLibro", e);
        }

    }


    /**
     * LEGGI FILE CON I CLIENTI --> PUNTO 2
     **/

    public void caricaCliente(String file) throws SQLException {
        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;


            while ((line = br.readLine()) != null) {
                String[] details = line.split(";");

                if (details.length == 7) {

                    String nome = details[0];
                    String cognome = details[1];
                    String dataNascita = details[2];
                    Date data = new SimpleDateFormat("dd/MM/yyyy").parse(dataNascita);
                    java.sql.Date sDate = new java.sql.Date(data.getTime());
                    String luogoNascita = details[3];
                    String residenza = details[4];
                    String email = details[5];
                    String telefono = details[6];

                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

                    Phonenumber.PhoneNumber number = phoneUtil.parse(telefono, "IT");

                    if (phoneUtil.isValidNumber(number)) {

                        telefono = phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);

                        if (controlloEmail(email)) {
                            Optional<Integer> idCliente = clienteDao.getIdCliente(email);

                            if (idCliente.isPresent()) {
                                clienteDao.updateCliente(nome, cognome, sDate, luogoNascita, residenza, email, telefono, idCliente.get());
                            } else {
                                clienteDao.insertCliente(nome, cognome, sDate, luogoNascita, residenza, email, telefono);
                            }

                        } // fine if controllo email
                    }
                } // fine if details.lenght
            } // fine while

        } catch (IOException e) {
            logger.error("IOException nel metodo caricaCliente", e);
        } catch (ParseException e) {
            logger.error("ParseException nel metodo caricaCliente", e);
        } catch (NumberParseException e ) {
            logger.error("NumberParseException nel metodo caricaCliente", e);
        }
    }

    /**
     * MODIFICA RESIDENZA E TELEFONO  ---> PUNTO 3
     **/

    public void modificaTelefonoResidenza(String telefono, String residenza, int idCliente) throws SQLException {

        try {
            if (StringUtils.isNotBlank(telefono)) {

                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

                Phonenumber.PhoneNumber number = phoneUtil.parse(telefono, "IT");

                if (phoneUtil.isValidNumber(number)) {

                    telefono = phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);

                    clienteDao.updateTelefonoResidenza(telefono, "telefono",idCliente);
                    logger.info("Telefono modificato");
                }
            }
            if (StringUtils.isNotBlank(residenza)) {
                clienteDao.updateTelefonoResidenza(residenza, "residenza",idCliente);
                logger.info("residenza modificata");
            }

        } catch (NumberParseException e) {
            logger.error("NumberParseException nel metodo modificaTelefonoResidenza", e);

        }

    }


    /**
     * PUNTO 6
     **/
    public void addPrestito(String file) throws SQLException {

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {

                String[] details = line.split(";");

                if (details.length == 3) {
                    String titolo = details[0];
                    String autore = details[1];
                    String email = details[2];

                    logger.info("Chiamata al metodo getIdCliente in base all'email");
                    Optional<Integer> idCliente = clienteDao.getIdCliente(email);
                    if (idCliente.isPresent()) {
                        logger.info("L'id del cliente è stato trovato");
                        String disponibile = "true";
                        logger.info("Chiamata al metodo getIdLibroDisponibile per verificare la disponibilità");
                        Optional<Integer> idLibro = libroDao.getIdLibroDisponibile(titolo, autore, disponibile);
                        if (idLibro.isPresent()) {
                            logger.info("Il libro è disponibile");
                            String restituito = "false";
                            logger.info("Chiamata al metodo getConteggio per sapere quanti libri sono stati prenotati dal cliente");
                            Optional<Integer> conteggioLibri = prenotazioneDao.getConteggio(idCliente.get(), restituito);

                            int conteggio = conteggioLibri.get();

                            if (conteggio < 3) {
                                int idCliente2 = idCliente.get();
                                int idLibro2 = idLibro.get();
                                restituito = "false";
                                logger.info("Chiamata al metodo insertPrestito per prenotare il libro");
                                prenotazioneDao.insertPrestito(idLibro2, idCliente2, restituito);
                                logger.info("Libro prenotato");

                                String disponibilità = "false";
                                libroDao.updateDisponibilitàLibro(autore, titolo, disponibilità);
                                logger.info("la disponibilità del libro è stata settata a false dopo essere stato prenotato");
                            } else {
                                logger.error("Non si possono prenotare più di 3 libri ");
                            }
                        }
                    }
                }
            }

        } catch (IOException e) {
            logger.error("IOException nel metodo addPrestito", e);
        }

    }


    /**
     * PUNTO 7
     **/
    public void checkPrestitiScaduti() throws SQLException {

        logger.info("Chiamata al metodo checkPrestitiScaduti ");
        List<Prestito> prestiti = prenotazioneDao.getPrestito();
        LocalDate dataOggi = LocalDate.now();

        for (Prestito prestito : prestiti) {
            LocalDate dataPrestito = prestito.getDataPrestito();

            if (dataPrestito != null) {
                long days = ChronoUnit.DAYS.between(dataPrestito, dataOggi);

                if (days > 30) {
                    logger.info("Il prestito per l'utente con id : {} supera i 30 giorni", prestito.getIdUtente());
                } else {
                    logger.info("Il prestito non supera i 30 giorni");
                }
            }
        }
    }


    /**
     * PUNTO 8
     **/
    public void checkPrenotazioniPerUtente(String file) throws SQLException {

        logger.info("Chiamata al metodo checkPrenotazioniPerUtente ");
        LocalDate dataOggi = LocalDate.now();
        LocalDate dataPrestito = null;

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;

            while ((line = br.readLine()) != null) {

                String[] details = line.split(";");

                if (details.length == 3) {
                    String titolo = details[0];
                    String autore = details[1];
                    String email = details[2];

                    Optional<Integer> idCliente = clienteDao.getIdCliente(email);
                    if (idCliente.isPresent()) {
                        String disponibile = "false";
                        Optional<Integer> idLibro = libroDao.getIdLibroDisponibile(titolo, autore, disponibile);
                        if (idLibro.isPresent()) {
                            int idCliente2 = idCliente.get();
                            int idLibro2 = idLibro.get();

                            Optional<Timestamp> dataFromGet = prenotazioneDao.getDataPrestito(idLibro2, idCliente2);

                            if (dataFromGet.isPresent()) {
                                dataPrestito = dataFromGet.get().toLocalDateTime().toLocalDate();
                            }

                            if (dataPrestito != null) {
                                long days = ChronoUnit.DAYS.between(dataPrestito, dataOggi);

                                if (days > 30) {
                                    logger.info("Il prestito per l'utente con id : {} supera i 30 giorni", idCliente2);
                                } else {
                                    logger.info("Il prestito non supera i 30 giorni");
                                }

                            }
                        }
                    }

                }

            }

            logger.info(" Fine Chiamata al metodo checkPrenotazioniPerUtente ");

        } catch (IOException e) {
            logger.error("IOException nel metodo checkPrenotazioniPerUtente ", e);
        }

    }

    /**
     * PUNTO 9
     **/
    public List<Libro> getLibroPerAutore(String autore) throws SQLException {

        return libroDao.getLibroPerAutore(autore);
    }

    /**
     * PUNTO 10
     **/
    public List<Cliente> getClientiPerAutore(String autore) throws SQLException {

        List<Cliente> clienti = new ArrayList<>();
        List<Prestito> prestiti = new ArrayList<>();


        List<Libro> libri = getLibroPerAutore(autore);
        for (int i = 0; i < libri.size(); i++) {

            Optional<Prestito> prestito = prenotazioneDao.getPrestitoPerIdLibro(libri.get(i).getIdLibro());
            prestito.ifPresent(prestiti::add);
        }

        for (int i = 0; i < prestiti.size(); i++) {

            Optional<Cliente> cliente = clienteDao.getClientiPerId(prestiti.get(i).getIdUtente());
            cliente.ifPresent(clienti::add);
        }

        return clienti;
    }

    /**
     * PUNTO 11
     **/
    public Map<Libro, Integer> classificaLibri() throws SQLException {

        List<Prestito> prestiti = prenotazioneDao.getPrestito();

        Map<Libro, List<Prestito>> map = new HashMap<>();


        for (Prestito prestito : prestiti) {

            Libro libro;
            int idLibro = prestito.getIdLibro();

            Optional<Libro> libro2 = libroDao.getLibroPerID(idLibro);

            if (libro2.isPresent()) {

                libro = libro2.get();

                if (map.containsKey(libro)) {
                    List<Prestito> prestitoBis = map.get(libro);
                    prestitoBis.add(prestito);
                } else {
                    List<Prestito> prestitoBis = new ArrayList<>();
                    prestitoBis.add(prestito);
                    map.put(libro, prestitoBis);
                }
            }
        }

        Map<Libro, Integer> map2 = new HashMap<>();
        map.forEach((k, v) -> map2.put(k, v.size()));

        System.out.println("Elenco Libri + Numero elementi che hanno quel libro, ordinati in ordine decrescente: ");

        map2.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getKey() + " -> " + stringIntegerEntry.getValue()));

        return map2;
    }


    /**
     * PUNTO 12
     **/
    public Map<Cliente, Integer> classificaClienti() throws SQLException {

        List<Prestito> prestiti = prenotazioneDao.getPrestito();

        Map<Cliente, List<Prestito>> map = new HashMap<>();


        for (int i = 0; i < prestiti.size(); i++) {

            Cliente cliente;
            int idCliente = prestiti.get(i).getIdUtente();

            Optional<Cliente> cliente2 = clienteDao.getClientiPerId(idCliente);

            if (cliente2.isPresent()) {

                cliente = cliente2.get();

                if (map.containsKey(cliente)) {
                    List<Prestito> prestitoBis = map.get(cliente);
                    prestitoBis.add(prestiti.get(i));
                } else {
                    List<Prestito> prestitoBis = new ArrayList<>();
                    prestitoBis.add(prestiti.get(i));
                    map.put(cliente, prestitoBis);
                }
            }
        }

        Map<Cliente, Integer> map2 = new HashMap<>();
        map.forEach((k, v) -> map2.put(k, v.size()));

        System.out.println("Elenco Clienti + Numero libri letti, ordinati in ordine decrescente: ");

        map2.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        return map2;
    }


    /**
     * PUNTO 13
     **/

    public Map<String, Integer> classificaGenereLibri() throws SQLException {

        List<Prestito> prestiti = prenotazioneDao.getPrestito();

        Map<String, List<Prestito>> map = new HashMap<>();


        for (int i = 0; i < prestiti.size(); i++) {

            String genere;
            int idLibro = prestiti.get(i).getIdLibro();

            Optional<String> genere2 = libroDao.getGenerePerId(idLibro);

            if (genere2.isPresent()) {

                genere = genere2.get();

                if (map.containsKey(genere)) {
                    List<Prestito> prestitoBis = map.get(genere);
                    prestitoBis.add(prestiti.get(i));
                } else {
                    List<Prestito> prestitoBis = new ArrayList<>();
                    prestitoBis.add(prestiti.get(i));
                    map.put(genere, prestitoBis);
                }
            }
        }

        Map<String, Integer> map2 = new HashMap<>();
        map.forEach((k, v) -> map2.put(k, v.size()));

        System.out.println("Elenco genere libro + Numero di libri con quel genere, ordinati in ordine decrescente: ");

        return map2;
    }


    /**
     * PUNTO 14
     **/

    public Map<Cliente, Map<String, Integer>> classificaGenereLibriCliente() throws SQLException {

        List<Prestito> prestiti = prenotazioneDao.getPrestito();
        List<Cliente> clienti = clienteDao.getClienti();

        Map<String, List<Prestito>> map;

        Map<Cliente, Map<String, Integer>> map3 = new HashMap<>();

        for (int j = 0; j < clienti.size(); j++) {

            Cliente cliente = clienti.get(j);

            int idCliente = cliente.getIdCliente();
            map = new HashMap<>();
            for (int i = 0; i < prestiti.size(); i++) {


                if (idCliente == prestiti.get(i).getIdUtente()) {


                    String genere;
                    int idLibro = prestiti.get(i).getIdLibro();

                    Optional<String> genere2 = libroDao.getGenerePerId(idLibro);

                    if (genere2.isPresent()) {

                        genere = genere2.get();

                        if (map.containsKey(genere)) {
                            List<Prestito> prestitoBis = map.get(genere);
                            prestitoBis.add(prestiti.get(i));
                        } else {
                            List<Prestito> prestitoBis = new ArrayList<>();
                            prestitoBis.add(prestiti.get(i));
                            map.put(genere, prestitoBis);
                        }
                    }
                }
            }


            Map<String, Integer> map2 = new HashMap<>();

            map.forEach((k, v) -> map2.put(k, v.size()));
            map2.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()));


            map3.put(cliente, map2);

        }

        return map3;
    }


    /**
     * METODO STAMPA TUTTI I LIBRI
     **/

    public void stampaLibro() throws SQLException {
        List<Libro> libri = libroDao.getLibro();

        for (Libro libro : libri) {
            System.out.println(libro.toString());
        }
    }


    public void restituisciLibro(String file) throws SQLException {

        try {
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line;


            while ((line = br.readLine()) != null) {

                String[] details = line.split(";");

                if (details.length == 3) {
                    String titolo = details[0];
                    String autore = details[1];
                    String email = details[2];

                    Optional<Integer> idCliente = clienteDao.getIdCliente(email);
                    if (idCliente.isPresent()) {
                        String disponibile = "false";
                        Optional<Integer> idLibro = libroDao.getIdLibroDisponibile(titolo, autore, disponibile);
                        if (idLibro.isPresent()) {
                            int idCliente2 = idCliente.get();
                            int idLibro2 = idLibro.get();
                            String restituito = "true";
                            prenotazioneDao.updatePrestito(idLibro2, idCliente2, restituito);
                            String disponibilità = "true";
                            libroDao.updateDisponibilitàLibro(autore, titolo, disponibilità);
                        }
                    }

                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
