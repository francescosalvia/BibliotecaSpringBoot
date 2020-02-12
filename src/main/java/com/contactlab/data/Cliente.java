package com.contactlab.data;

import java.util.Date;
import java.util.Objects;

public class Cliente {

    private String nome;
    private String cognome;
    private Date dataNascita;
    private String luogoNascita;
    private String residenza;
    private String email;
    private String numeroTelefono;
    private int idCliente;

    public Cliente(String nome, String cognome, Date dataNascita, String luogoNascita, String residenza, String email, String numeroTelefono) {
        this.nome = nome;
        this.cognome = cognome;
        this.dataNascita = dataNascita;
        this.luogoNascita = luogoNascita;
        this.residenza = residenza;
        this.email = email;
        this.numeroTelefono = numeroTelefono;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Date getDataNascita() {
        return dataNascita;
    }

    public void setDataNascita(Date dataNascita) {
        this.dataNascita = dataNascita;
    }

    public String getLuogoNascita() {
        return luogoNascita;
    }

    public void setLuogoNascita(String luogoNascita) {
        this.luogoNascita = luogoNascita;
    }

    public String getResidenza() {
        return residenza;
    }

    public void setResidenza(String residenza) {
        this.residenza = residenza;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNumeroTelefono() {
        return numeroTelefono;
    }

    public void setNumeroTelefono(String numeroTelefono) {
        this.numeroTelefono = numeroTelefono;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    @Override
    public String toString() {
        return "Cliente { " +
                "nome='" + nome + '\'' +
                ", cognome='" + cognome + '\'' +
                ", dataNascita=" + dataNascita +
                ", luogoNascita='" + luogoNascita + '\'' +
                ", residenza='" + residenza + '\'' +
                ", email='" + email + '\'' +
                ", numeroTelefono='" + numeroTelefono + '\'' +
                ", idCliente='" + idCliente + '\'' +
                " } ";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return idCliente == cliente.idCliente &&
                nome.equals(cliente.nome) &&
                cognome.equals(cliente.cognome) &&
                dataNascita.equals(cliente.dataNascita) &&
                luogoNascita.equals(cliente.luogoNascita) &&
                residenza.equals(cliente.residenza) &&
                email.equals(cliente.email) &&
                numeroTelefono.equals(cliente.numeroTelefono);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, cognome, dataNascita, luogoNascita, residenza, email, numeroTelefono, idCliente);
    }
}

