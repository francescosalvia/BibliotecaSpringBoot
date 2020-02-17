package com.contactlab.data;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name="prestito")
public class Prestito {

    @Id
    @Column(name = "id_cliente")
    private Integer idCliente;
    @Column(name = "id_libro")
    private Integer idLibro;
    @Column(name = "data_insert")
    private LocalDate dataPrestito;
    private String restituito;

    public Prestito() {
    }

    public Integer getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(Integer idUtente) {
        this.idCliente = idUtente;
    }

    public Integer getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Integer idLibro) {
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
                "idUtente='" + idCliente + '\'' +
                ", idLibro='" + idLibro + '\'' +
                ", dataPrestito=" + dataPrestito +
                ", restituito='" + restituito + '\'' +
                " } ";
    }
}
