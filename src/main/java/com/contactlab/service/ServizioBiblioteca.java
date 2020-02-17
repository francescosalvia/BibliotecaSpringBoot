package com.contactlab.service;

import com.contactlab.dao.ClienteDao;
import com.contactlab.dao.LibroDao;
import com.contactlab.dao.PrenotazioneDao;
import com.contactlab.data.Cliente;
import com.contactlab.data.Libro;
import com.contactlab.data.Prestito;
import com.contactlab.repository.ClienteRepository;
import com.contactlab.repository.LibroRepository;
import com.contactlab.repository.PrestitoRepository;
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
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class ServizioBiblioteca {

    private static final Logger logger = LoggerFactory.getLogger(ServizioBiblioteca.class);

    @Autowired
    private LibroDao libroDao;

    @Autowired
    private ClienteDao clienteDao;

    @Autowired
    private PrenotazioneDao prenotazioneDao;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private PrestitoRepository prestitoRepository;


    /**
     * METODI UTILI
     **/

    public boolean controlloEmail(String email) {
        logger.info("Chiamata nel metodo controlloEmail");
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }


    public List<Cliente> trovaClienti() {

        return clienteRepository.findAll();
    }

    public List<Libro> trovaLibri() {
        return libroRepository.findAll();
    }

    public List<Prestito> trovaPrestiti() {
        return prestitoRepository.findAll();
    }


    /**
     * LEGGI FILE CON I LIBRI  -> PUNTO 1
     **/

    public void caricaLibro(String file) {

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
                    int dataa = Integer.parseInt(data);
                    Optional<Libro> libroTrovato = libroRepository.findLibroByTitoloAndAutore(titolo, autore);

                    if (libroTrovato.isPresent()) {
                        Libro libro = libroTrovato.get();
                        libro.setAnno(dataa);
                        libro.setAutore(autore);
                        libro.setGenere(genere);
                        libro.setTitolo(titolo);
                        libroRepository.save(libro);// disponibilità non la modifico per non influire su eventuali prestiti
                    } else {
                        Libro libro = new Libro(titolo, autore, dataa, genere, disponibile);
                        libroRepository.save(libro);

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

    public void caricaCliente(String file) {
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
                            Optional<Cliente> cliente = clienteRepository.findClienteByEmail(email);

                            if (cliente.isPresent()) {
                                Cliente cliente1 = cliente.get();
                                cliente1.setNome(nome);
                                cliente1.setCognome(cognome);
                                cliente1.setDataNascita(sDate);
                                cliente1.setLuogoNascita(luogoNascita);
                                cliente1.setEmail(email);
                                cliente1.setResidenza(residenza);
                                cliente1.setNumeroTelefono(telefono);
                                clienteRepository.save(cliente1);
                            } else {
                                Cliente cliente1 = new Cliente(nome, cognome, sDate, luogoNascita, residenza, email, telefono);
                                clienteRepository.save(cliente1);
                            }

                        } // fine if controllo email
                    }
                } // fine if details.lenght
            } // fine while

        } catch (IOException e) {
            logger.error("IOException nel metodo caricaCliente", e);
        } catch (ParseException e) {
            logger.error("ParseException nel metodo caricaCliente", e);
        } catch (NumberParseException e) {
            logger.error("NumberParseException nel metodo caricaCliente", e);
        }
    }

    /**
     * MODIFICA RESIDENZA E TELEFONO  ---> PUNTO 3
     **/

    public void modificaTelefonoResidenza(String telefono, String residenza, int idCliente) throws SQLException {

        Optional<Cliente> cliente = clienteRepository.findClienteByIdCliente(idCliente);

        if (cliente.isPresent()) {

            Cliente cliente1 = cliente.get();
            try {
                if (StringUtils.isNotBlank(telefono)) {

                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();

                    Phonenumber.PhoneNumber number = phoneUtil.parse(telefono, "IT");

                    if (phoneUtil.isValidNumber(number)) {

                        telefono = phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.E164);

                        cliente1.setNumeroTelefono(telefono);
                        clienteRepository.save(cliente1);
                        logger.info("Telefono modificato");
                    }
                }
                if (StringUtils.isNotBlank(residenza)) {
                    cliente1.setResidenza(residenza);
                    clienteRepository.save(cliente1);
                    logger.info("residenza modificata");
                }

            } catch (NumberParseException e) {
                logger.error("NumberParseException nel metodo modificaTelefonoResidenza", e);

            }

        }
    }

    /**
     * PUNTO 6
     **/
    public void addPrestito(String file) {

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

                    logger.info("Chiamata al metodo getCliente in base all'email");
                    Optional<Cliente> cliente = clienteRepository.findClienteByEmail(email);
                    if (cliente.isPresent()) {
                        logger.info("il cliente è stato trovato");
                        String disponibile = "true";
                        logger.info("Chiamata al metodo getLibro per verificare la disponibilità");
                        Optional<Libro> libro = libroRepository.findLibroByTitoloAndAutoreAndDisponibile(titolo, autore, disponibile);
                        if (libro.isPresent()) {
                            logger.info("Il libro è disponibile");
                            String restituito = "false";
                            logger.info("Chiamata al metodo getConteggio per sapere quanti libri sono stati prenotati dal cliente");

                            long conteggioLibri = prestitoRepository.countByIdClienteAndRestituito(cliente.get().getIdCliente(), restituito);

                            if (conteggioLibri < 3) {
                                int idCliente2 = cliente.get().getIdCliente();
                                int idLibro2 = libro.get().getIdLibro();
                                restituito = "false";
                                logger.info("Chiamata al metodo save Prestito per prenotare il libro");
                                Prestito prestito = new Prestito(idCliente2, idLibro2, LocalDateTime.now(), restituito);
                                prestitoRepository.save(prestito);
                                logger.info("Libro prenotato");

                                String disponibilita = "false";

                                Libro libro1 = libro.get();
                                libro1.setDisponibile(disponibilita);

                                libroRepository.save(libro1);
                                logger.info("la disponibilità del libro è stata settata a false dopo essere stato prenotato");
                            } else {
                                logger.error("Non si possono prenotare più di 3 libri ");
                            }
                        } else {
                            logger.info("Libro non disponibile");
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
    public void checkPrestitiScaduti() {

        logger.info("Chiamata al metodo checkPrestitiScaduti ");

        List<Prestito> prestiti = prestitoRepository.findAll();
        LocalDate dataOggi = LocalDate.now();

        for (Prestito prestito : prestiti) {
            LocalDate dataPrestito = prestito.getDataPrestito().toLocalDate();

            if (dataPrestito != null) {
                long days = ChronoUnit.DAYS.between(dataPrestito, dataOggi);

                if (days > 30) {
                    logger.info("Il prestito per l'utente con id : {} supera i 30 giorni", prestito.getIdCliente());
                } else {
                    logger.info("Il prestito non supera i 30 giorni");
                }
            }
        }
    }


    /**
     * PUNTO 8
     **/
    public void checkPrenotazioniPerUtente(String file) {

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

                    Optional<Cliente> cliente = clienteRepository.findClienteByEmail(email);
                    if (cliente.isPresent()) {
                        String disponibile = "false";
                        Optional<Libro> libro = libroRepository.findLibroByTitoloAndAutoreAndDisponibile(titolo, autore, disponibile);
                        if (libro.isPresent()) {
                            int idCliente2 = cliente.get().getIdCliente();
                            int idLibro2 = libro.get().getIdLibro();

                            Optional<Prestito> prestito = prestitoRepository.findPrestitoByIdClienteAndIdLibro(idLibro2, idCliente2);

                            if (prestito.isPresent()) {

                                dataPrestito = prestito.get().getDataPrestito().toLocalDate();

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

            }

            logger.info(" Fine Chiamata al metodo checkPrenotazioniPerUtente ");

        } catch (IOException e) {
            logger.error("IOException nel metodo checkPrenotazioniPerUtente ", e);
        }

    }

    /**
     * PUNTO 9
     **/
    public List<Libro> getLibroPerAutore(String autore) {
        return libroRepository.findLibroByAutore(autore);
    }

    /**
     * PUNTO 10
     **/
    public List<Cliente> getClientiPerAutore(String autore) {

        List<Cliente> clienti = new ArrayList<>();
        List<Prestito> prestiti = new ArrayList<>();

        List<Libro> libri = getLibroPerAutore(autore);

        for (Libro libro : libri) {

            Optional<Prestito> prestito = prestitoRepository.findPrestitoByIdLibro(libro.getIdLibro());
            prestito.ifPresent(prestiti::add);
        }

        for (Prestito prestito : prestiti) {

            Optional<Cliente> cliente = clienteRepository.findClienteByIdCliente(prestito.getIdCliente());
            cliente.ifPresent(clienti::add);
        }

        return clienti;
    }

    /**
     * PUNTO 11
     **/
    public Map<Libro, Integer> classificaLibri() {

        List<Prestito> prestiti = prestitoRepository.findAll();

        Map<Libro, List<Prestito>> map = new HashMap<>();


        for (Prestito prestito : prestiti) {

            Libro libro;
            int idLibro = prestito.getIdLibro();

            Optional<Libro> libro2 = libroRepository.findLibroByIdLibro(idLibro);

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

        logger.info("Elenco Libri + Numero elementi che hanno quel libro, ordinati in ordine decrescente: ");

        return map2.entrySet()
                .stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                        collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }


    /**
     * PUNTO 12
     **/
    public Map<Cliente, Integer> classificaClienti() {

        List<Prestito> prestiti = prestitoRepository.findAll();

        Map<Cliente, List<Prestito>> map = new HashMap<>();

        for (Prestito prestito : prestiti) {

            Cliente cliente;
            int idCliente = prestito.getIdCliente();

            Optional<Cliente> cliente2 = clienteRepository.findClienteByIdCliente(idCliente);

            if (cliente2.isPresent()) {

                cliente = cliente2.get();

                if (map.containsKey(cliente)) {
                    List<Prestito> prestitoBis = map.get(cliente);
                    prestitoBis.add(prestito);
                } else {
                    List<Prestito> prestitoBis = new ArrayList<>();
                    prestitoBis.add(prestito);
                    map.put(cliente, prestitoBis);
                }
            }
        }

        Map<Cliente, Integer> map2 = new HashMap<>();
        map.forEach((k, v) -> map2.put(k, v.size()));

        logger.info("Elenco Clienti + Numero libri letti, ordinati in ordine decrescente: ");

        return map2.entrySet()
                .stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                        collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));

    }


    /**
     * PUNTO 13
     **/

    public Map<String, Integer> classificaGenereLibri() {

        List<Prestito> prestiti = prestitoRepository.findAll();

        Map<String, List<Prestito>> map = new HashMap<>();


        for (Prestito prestito : prestiti) {

            String genere;
            int idLibro = prestito.getIdLibro();

            Optional<Libro> libro = libroRepository.findLibroByIdLibro(idLibro);

            if (libro.isPresent()) {

                genere = libro.get().getGenere();

                if (StringUtils.isNotBlank(genere)) {
                    if (map.containsKey(genere)) {
                        List<Prestito> prestitoBis = map.get(genere);
                        prestitoBis.add(prestito);
                    } else {
                        List<Prestito> prestitoBis = new ArrayList<>();
                        prestitoBis.add(prestito);
                        map.put(genere, prestitoBis);
                    }
                }
            }
        }

        Map<String, Integer> map2 = new HashMap<>();
        map.forEach((k, v) -> map2.put(k, v.size()));

        logger.info("Elenco genere libro + Numero di libri con quel genere, ordinati in ordine decrescente: ");


        return map2.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
    }


    /**
     * PUNTO 14
     **/

    public Map<Cliente, Map<String, Integer>> classificaGenereLibriCliente() {

        List<Prestito> prestiti = prestitoRepository.findAll();
        List<Cliente> clienti = clienteRepository.findAll();

        Map<String, List<Prestito>> map;

        Map<Cliente, Map<String, Integer>> map3 = new HashMap<>();

        for (Cliente cliente : clienti) {

            int idCliente = cliente.getIdCliente();
            map = new HashMap<>();
            for (Prestito prestito : prestiti) {

                if (idCliente == prestito.getIdCliente()) {

                    String genere;
                    int idLibro = prestito.getIdLibro();

                    Optional<Libro> libro = libroRepository.findLibroByIdLibro(idLibro);

                    if (libro.isPresent()) {

                        genere = libro.get().getGenere();

                        if (StringUtils.isNotBlank(genere)) {
                            if (map.containsKey(genere)) {
                                List<Prestito> prestitoBis = map.get(genere);
                                prestitoBis.add(prestito);
                            } else {
                                List<Prestito> prestitoBis = new ArrayList<>();
                                prestitoBis.add(prestito);
                                map.put(genere, prestitoBis);
                            }
                        }
                    }
                }
            }


            Map<String, Integer> map2 = new HashMap<>();

            map.forEach((k, v) -> map2.put(k, v.size()));
            map2.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()));
            map3.put(cliente, map2);
        }

        Map<Cliente, Map<String, Integer>> map4 = new HashMap<>();

        map3.forEach((cliente1, stringIntegerMap) -> {
            Map<String, Integer> mapp = stringIntegerMap.entrySet()
                    .stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                            collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue, LinkedHashMap::new));
            map4.put(cliente1, mapp);
        });

        return map4;
    }


    /**
     * METODO STAMPA TUTTI I LIBRI
     **/

    public void stampaLibro() {
        List<Libro> libri = libroRepository.findAll();

        for (Libro libro : libri) {
            System.out.println(libro.toString());
        }
    }


    public void restituisciLibro(String file) {

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

                    Optional<Cliente> cliente = clienteRepository.findClienteByEmail(email);

                    if (cliente.isPresent()) {

                        int idCliente = cliente.get().getIdCliente();
                        String disponibile = "false";

                        Optional<Libro> libro = libroRepository.findLibroByTitoloAndAutoreAndDisponibile(titolo, autore, disponibile);

                        if (libro.isPresent()) {
                            int idLibro = libro.get().getIdLibro();
                            String restituito = "true";

                            Optional<Prestito> prestito = prestitoRepository.findPrestitoByIdClienteAndIdLibro(idCliente, idLibro);

                            if (prestito.isPresent()) {

                                Prestito prestito1 = prestito.get();
                                prestito1.setRestituito(restituito);
                                prestitoRepository.save(prestito1);

                                String disponibilita = "true";

                                Libro libro1 = libro.get();
                                libro1.setDisponibile(disponibilita);

                                libroRepository.save(libro1);
                            }
                        }

                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
