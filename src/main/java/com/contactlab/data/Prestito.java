package com.contactlab.data;


import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name="prestito")
public class Prestito {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Column(name = "id_prestito")
    private Integer idPrestito;
    @Column(name = "id_cliente")
    private Integer idCliente;
    @Column(name = "id_libro")
    private Integer idLibro;
    @Column(name = "data_prestito")
    private LocalDateTime dataPrestito;
    private String restituito;

    public Prestito() {
    }

    public Prestito(Integer idCliente, Integer idLibro, LocalDateTime dataPrestito, String restituito) {
        this.idCliente = idCliente;
        this.idLibro = idLibro;
        this.dataPrestito = dataPrestito;
        this.restituito = restituito;
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

    public LocalDateTime getDataPrestito() {
        return dataPrestito;
    }

    public void setDataPrestito(LocalDateTime dataPrestito) {
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
