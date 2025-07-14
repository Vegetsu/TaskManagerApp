package org.example.TaskManagerApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class SQLTietokanta {

    public static Connection Openconnection() throws SQLException {
        Connection yhteys = DriverManager.getConnection(
                "jdbc:mysql://localhost:" + 3307 + "/" + "taskmanager_schema",
                "root", ""
        );
        System.out.println("yhteys muodostettu");
        return yhteys;

    }

    public  static ObservableList<Tehtava> Tehtavatiedot() throws SQLException {
        Connection conn = Openconnection();
        ObservableList<Tehtava> lista = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = conn.prepareStatement("Select * from tehtavamanager");
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                lista.add(new Tehtava(Integer.parseInt(rs.getString("idTehtava")), rs.getString("NimiTehtava"), rs.getString("KuvausTehtava")));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public static void lisaatehtava(Integer id, String nimi, String kuvaus, Connection yhteys) throws SQLException {

        try (PreparedStatement statement = yhteys.prepareStatement("""
        INSERT INTO tehtavamanager(IdTehtava, NimiTehtava, KuvausTehtava)
        VALUES (?,?,?)
      """)) {
            statement.setInt(1, id);
            statement.setString(2, nimi);
            statement.setString(3, kuvaus);
            statement.executeUpdate();

        }


    }

    public static void PaivitaKuvaus(String kuvaus,Integer id, Connection yhteys) throws SQLException {

        try (PreparedStatement statement = yhteys.prepareStatement("UPDATE tehtavamanager SET KuvausTehtava=? WHERE IdTehtava=?")) {
            statement.setString(1, kuvaus);
            statement.setInt(2, id);
            statement.executeUpdate();

        }
    }

    public static void PaivitaNimi(String nimi,Integer id, Connection yhteys) throws SQLException {

        try (PreparedStatement statement = yhteys.prepareStatement("UPDATE tehtavamanager SET NimiTehtava=? WHERE IdTehtava=?")) {
            statement.setString(1, nimi);
            statement.setInt(2, id);
            statement.executeUpdate();

        }
    }

    public static void PaivitaId(Integer uusiId, Integer vanhaId, Connection yhteys) throws SQLException {

        try (PreparedStatement statement = yhteys.prepareStatement("UPDATE tehtavamanager SET IdTehtava=? WHERE IdTehtava=?")) {
            statement.setInt(1, uusiId);
            statement.setInt(2, vanhaId);
            statement.executeUpdate();

        }
    }

    public static void poistaTehtava(Integer id,Connection yhteys) throws SQLException {

        try (PreparedStatement statement = yhteys.prepareStatement("DELETE FROM tehtavamanager WHERE IdTehtava=?")){
            statement.setInt(1,id);
            statement.executeUpdate();
        }
    }

}
