package com.contactlab;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
























   /* @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {

   //     Scanner scanner = new Scanner(System.in);


        *//*   *//**//**  1. registrazione cliente  **//**//*

           String fileCliente = "C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\clienti.txt";
           servizioBiblioteca.caricaCliente(fileCliente);

           *//**//** 2. registrazione libro  **//**//*

           String fileLibro = "C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\libri.txt";
           servizioBiblioteca.caricaLibro(fileLibro);

           *//**//**  6. registrazione prestito  **//**//*

           String filePrestito = "C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\prestito.txt";
           servizioBiblioteca.addPrestito(filePrestito);*//*

        *//*            *//**//**   restituizione prestito  **//**//*

                    String fileRestituisci = "C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\restituisci.txt";
                    servizioBiblioteca.restituisciLibro(fileRestituisci);*//*

        *//*
                    *//*
*//**  registrazione prestito 2 volta **//**//*


            String filePrestito2 = "C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\prestito2.txt";
            servizioBiblioteca.addPrestito(filePrestito2);
*//*

        *//**
         *      3. modifica numero di telefono cliente
         *      4. modifica email cliente
         *      5. modifica residenza cliente
         **//*

*//*            System.out.println("Metodo modifica telefono e residenza");
            System.out.println("Inserire Telefono e Residenza. Se non si vuole modificare lasciare vuoto");

            System.out.println("IdCliente (Obbligatorio): ");
            int idCliente = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Telefono: ");
            String telefono = scanner.nextLine();

            System.out.println("Residenza: ");
            String residenza = scanner.nextLine();

            servizioBiblioteca.modificaTelefonoResidenza(telefono, residenza, idCliente);*//*

        *//*            *//**//** 7. verifica sul registro se ci sono presiti scaduti **//**//*

                    String fileCheckPrenotazioni = "C:\\Users\\francesco.salvia\\Desktop\\BIBLIOTECA\\checkPrestiti.txt";
                    servizioBiblioteca.checkPrenotazioniPerUtente(fileCheckPrenotazioni);

                    List<Cliente> clienti = servizioBiblioteca.trovaClienti();

                    *//**//**  stampa tutti i clienti **//**//*

                    for (Cliente c : clienti) {
                        System.out.println(c.toString());
                    }

                    *//**//** stampa tutti i libri **//**//*

                    List<Libro> libri = servizioBiblioteca.trovaLibri();

                    for (Libro l : libri) {
                        System.out.println(l.toString());
                    }

                    *//**//** stampa tutti i prestiti **//**//*

                    List<Prestito> prestiti = servizioBiblioteca.trovaPrestiti();

                    for (Prestito p : prestiti) {
                        System.out.println(p.toString());
                    }

                    servizioBiblioteca.checkPrestitiScaduti();*//*

        *//** 9.  stampare tutti i libri prendendo in input l'autore **//*

           *//* System.out.println("Metodo get Libro Per Autore: ");
            System.out.println("Inserisci L'autore");

            String autore = scanner.nextLine();
            List<Libro> libroPerAutore = servizioBiblioteca.getLibroPerAutore(autore);

            for (Libro l : libroPerAutore) {
                System.out.println(l.toString());
            }*//*

        *//*           System.out.println("----------------");*//*

        *//** 10. stampare tutti i clienti che hanno preso in prestito i libri di un autore passato in input **//*

            *//*System.out.println("Metodo get Clienti Per Autore: ");
            System.out.println("Inserisci L'autore");

            String autore2 = scanner.nextLine();
            List<Cliente> clienti1 = servizioBiblioteca.getClientiPerAutore(autore2);

            for (Cliente c : clienti1) {
                System.out.println(c.toString());
            }*//*


        *//*            *//**//** 11. stampare la classifica dei libri più chiesti in prestito (info libro + numero di prestiti) **//**//*

                    Map<Libro, Integer> mapLibro = servizioBiblioteca.classificaLibri();

                    mapLibro.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                            forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getKey() + " -> " + stringIntegerEntry.getValue()));


                    *//**//** 12. stampare la classifica dei clienti che hanno letto più libri (info clienti + numero libri letti) **//**//*

                    Map<Cliente, Integer> mapClienti = servizioBiblioteca.classificaClienti();

                    mapClienti.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                            forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getKey() + " -> " + stringIntegerEntry.getValue()));


                    *//**//** 13. stampare la classifica dei generi di libri più letti (genere + numero libri letti) **//**//*

                    Map<String, Integer> mapGenere = servizioBiblioteca.classificaGenereLibri();

                    mapGenere.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                            forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getKey() + " -> " + stringIntegerEntry.getValue()));

                    *//**//** 14. per ogni cliente, stampare la classifica dei generi più letti (genere + numero libri letti) **//**//*

                    Map<Cliente, Map<String, Integer>> map3 = servizioBiblioteca.classificaGenereLibriCliente();

                    map3.forEach((cliente, stringIntegerMap) -> {

                        System.out.println(cliente.toString());

                        stringIntegerMap.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).
                                forEach(stringIntegerEntry -> System.out.println(stringIntegerEntry.getKey() + " -> " + stringIntegerEntry.getValue()));

                    });*//*


    }*/
