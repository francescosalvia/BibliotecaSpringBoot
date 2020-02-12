package com.contactlab.data;

import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class Prestito {

    private int idUtente;
    private int idLibro;
    private LocalDate dataPrestito;
    private String restituito;

    public int getIdUtente() {
        return idUtente;
    }

    public void setIdUtente(int idUtente) {
        this.idUtente = idUtente;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public LocalDate getDataPrestito() {
        return dataPrestito;
    }

    public void setDataPrestito(LocalDate dataPrestito) {
        this.dataPrestito = dataPrestito;
    }

    public String getRestituito() {
        return restituito;
    }

    public void setRestituito(String restituito) {
        this.restituito = restituito;
    }

    @Override
    public String toString() {
        return "Prestito { " +
                "idUtente='" + idUtente + '\'' +
                ", idLibro='" + idLibro + '\'' +
                ", dataPrestito=" + dataPrestito +
                ", restituito='" + restituito + '\'' +
                " } ";
    }
}
