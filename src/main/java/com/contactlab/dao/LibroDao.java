package com.contactlab.dao;

import com.contactlab.data.Libro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//import static com.contactlab.dao.DatabaseDao.getConnection;


@Repository
public class LibroDao {

    @Autowired
    private Connection connection;

    /*************************************************************************/

    public void insertLibro(String titolo, String autore, String anno, String genere, String disponibile) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("INSERT INTO libro(titolo,  autore,  anno,  genere,  disponibile) VALUES (?,?,?,?,?)");

        ps.setString(1, titolo);
        ps.setString(2, autore);
        ps.setString(3, anno);
        ps.setString(4, genere);
        ps.setString(5, disponibile);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();

    }

    /**************************************************************************/

    public void updateLibro(String titolo, String autore, String anno, String genere, int idLibro) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("UPDATE libro SET  titolo = ?,  autore = ?,  anno = ?,  genere = ? where id_libro = ?");

        ps.setString(1, titolo);
        ps.setString(2, autore);
        ps.setString(3, anno);
        ps.setString(4, genere);
        ps.setInt(5, idLibro);

        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();
    }

    /************************************************************************/

    public Optional<Integer> getIdLibro(String titolo, String autore) throws SQLException {

        Optional<Integer> idCliente = Optional.empty();
        PreparedStatement ps = connection.prepareStatement("SELECT id_libro FROM libro where titolo = ? and autore = ?");

        ps.setString(1, titolo);
        ps.setString(2, autore);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            idCliente = Optional.of(rs.getInt("id_libro"));
        }

        return idCliente;
    }

    /*************************************************************************/

    public List<Libro> getLibro() throws SQLException {

        List<Libro> libri = new ArrayList<>();

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM libro");


        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            int idLibro = rs.getInt("id_libro");
            String titolo = rs.getString("titolo");
            String autore = rs.getString("autore");
            int anno = rs.getInt("anno");
            String genere = rs.getString("genere");
            String disponibile = rs.getString("disponibile");


            Libro libro = new Libro(titolo, autore, anno, genere, disponibile);
            libro.setIdLibro(idLibro);
            libri.add(libro);
        }
        return libri;
    }

    /*************************************************************************/

    public List<Libro> getLibroPerAutore(String autore) throws SQLException {

        List<Libro> libri = new ArrayList<>();

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM libro where autore = ?");
        ps.setString(1, autore);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            int idLibro = rs.getInt("id_libro");
            String titolo = rs.getString("titolo");
            int anno = rs.getInt("anno");
            String genere = rs.getString("genere");
            String disponibile = rs.getString("disponibile");


            Libro libro = new Libro(titolo, autore, anno, genere, disponibile);
            libro.setIdLibro(idLibro);
            libri.add(libro);
        }
        return libri;
    }

    /*************************************************************************/


    public Optional<Libro> getLibroPerID(int idLibro) throws SQLException {

        Libro libro;
        Optional<Libro> libro1 = null;

        PreparedStatement ps = connection.prepareStatement("SELECT * FROM libro where id_libro = ?");
        ps.setInt(1, idLibro);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            String autore = rs.getString("autore");
            String titolo = rs.getString("titolo");
            int anno = rs.getInt("anno");
            String genere = rs.getString("genere");
            String disponibile = rs.getString("disponibile");


            libro = new Libro(titolo, autore, anno, genere, disponibile);
            libro.setIdLibro(idLibro);
            libro1 = Optional.of(libro);

        }
        return libro1;
    }

    /*************************************************************************/

    public Optional<String> getGenerePerId(int idLibro) throws SQLException {

        Optional<String> genere1 = null;

        PreparedStatement ps = connection.prepareStatement("SELECT genere FROM libro where id_libro = ?");
        ps.setInt(1, idLibro);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {

            String genere = rs.getString("genere");
            genere1 = Optional.of(genere);

        }
        return genere1;
    }

    /*************************************************************************/

    public Optional<Integer> getIdLibroDisponibile(String titolo,String autore,String disponibile) throws SQLException {

        Optional<Integer> idCliente = Optional.empty();
        PreparedStatement ps = connection.prepareStatement("SELECT id_libro FROM libro where titolo = ? and autore = ? and disponibile = ?");

        ps.setString(1,titolo);
        ps.setString(2,autore);
        ps.setString(3,disponibile);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            idCliente = Optional.of(rs.getInt("id_libro"));
        }

        return idCliente;
    }


    /*************************************************************************/


    public void updateDisponibilitàLibro(String autore, String titolo, String disponibilità) throws SQLException {

        PreparedStatement ps = connection.prepareStatement("UPDATE libro SET disponibile = ? where  autore = ? and titolo = ? ");

        ps.setString(1,disponibilità);
        ps.setString(2,autore);
        ps.setString(3,titolo);


        ps.executeUpdate();

        ResultSet rs = ps.getGeneratedKeys();
        rs.next();


    }


}
