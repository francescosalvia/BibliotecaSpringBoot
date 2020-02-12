package com.contactlab.data;

import java.util.Objects;

public class Libro {

    private String titolo;
    private String autore;
    private int anno;
    private String genere;
    private String disponibile;
    private int idLibro;


    public Libro(String titolo, String autore, int anno, String genere, String disponibile) {
        this.titolo = titolo;
        this.autore = autore;
        this.anno = anno;
        this.genere = genere;
        this.disponibile = disponibile;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getAutore() {
        return autore;
    }

    public void setAutore(String autore) {
        this.autore = autore;
    }


    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public String getGenere() {
        return genere;
    }

    public void setGenere(String genere) {
        this.genere = genere;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getDisponibile() {
        return disponibile;
    }

    public void setDisponibile(String disponibile) {
        this.disponibile = disponibile;
    }

    @Override
    public String toString() {
        return "Libro { " +
                "titolo='" + titolo + '\'' +
                ", autore='" + autore + '\'' +
                ", anno=" + anno +
                ", genere='" + genere + '\'' +
                ", disponibile='" + disponibile + '\'' +
                ", idLibro='" + idLibro + '\'' +
                " } ";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Libro libro = (Libro) o;
        return anno == libro.anno &&
                idLibro == libro.idLibro &&
                Objects.equals(titolo, libro.titolo) &&
                Objects.equals(autore, libro.autore) &&
                Objects.equals(genere, libro.genere) &&
                Objects.equals(disponibile, libro.disponibile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titolo, autore, anno, genere, disponibile, idLibro);
    }

}
