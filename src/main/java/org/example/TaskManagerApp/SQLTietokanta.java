package org.example.TaskManagerApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class SQLTietokanta {

    public static Connection Openconnection() throws SQLException, IOException {
        Properties props = new Properties();

        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            props.load(input);
        }

        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        Connection yhteys = DriverManager.getConnection(url, user, password);
        System.out.println("Yhteys muodostettu");

        return yhteys;
    }

    /*
    public static Connection Openconnection() throws SQLException {
        Connection yhteys = DriverManager.getConnection(
                "jdbc:mysql://localhost:" + 3307 + "/" + "taskmanager_schema",
                "root", ""
        );
        System.out.println("yhteys muodostettu");
        return yhteys;

    }*/

    public  static ObservableList<Tehtava> Tehtavatiedot() throws SQLException, IOException {
        Connection conn = Openconnection();
        ObservableList<Tehtava> lista = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = conn.prepareStatement("Select * from tehtavamanager");
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                lista.add(new Tehtava(Integer.parseInt(rs.getString("idTehtava")), rs.getString("NimiTehtava"), rs.getString("KuvausTehtava"), Integer.parseInt(rs.getString("idKayttaja"))));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return lista;
    }

    public static int lisaatehtava(String nimi, String kuvaus, Integer kayttajaid, Connection yhteys) throws SQLException {
        String sql = """
        INSERT INTO tehtavamanager(NimiTehtava, KuvausTehtava, IdKayttaja)
        VALUES (?,?,?)
    """;

        try (PreparedStatement statement = yhteys.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, nimi);
            statement.setString(2, kuvaus);
            statement.setInt(3, kayttajaid);

            statement.executeUpdate();


            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return -1;
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

    /*public static void PaivitaId(Integer uusiId, Integer vanhaId, Connection yhteys) throws SQLException {

        try (PreparedStatement statement = yhteys.prepareStatement("UPDATE tehtavamanager SET IdTehtava=? WHERE IdTehtava=?")) {
            statement.setInt(1, uusiId);
            statement.setInt(2, vanhaId);
            statement.executeUpdate();

        }
    }*/

    public static void poistaTehtava(Integer id,Connection yhteys) throws SQLException {

        try (PreparedStatement statement = yhteys.prepareStatement("DELETE FROM tehtavamanager WHERE IdTehtava=?")){
            statement.setInt(1,id);
            statement.executeUpdate();
        }
    }

    public static void lisaaKayttaja(String kayttajatunnus, String salasana,Connection yhteys) throws SQLException {

        try (PreparedStatement statement = yhteys.prepareStatement("""
        INSERT INTO kayttajat(kayttajatunnus, salasana_hash)
        VALUES (?,?)
      """)) {
            statement.setString(1, kayttajatunnus);
            statement.setString(2, salasana);
            statement.executeUpdate();

        }
    }

    public static boolean verifyKayttaja(String kayttajatunnus, String salasana, Connection yhteys) throws SQLException {

        try (PreparedStatement statement = yhteys.prepareStatement("""
        SELECT salasana_hash FROM kayttajat WHERE kayttajatunnus = ?
      """)) {
            statement.setString(1,kayttajatunnus);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String hashFromDb = rs.getString("salasana_hash");
                return UserService.verifyPassword(salasana, hashFromDb);
            } else {
                return false;
            }

        }
    }


    public static boolean Onkokayttajaolemassa(String kayttajatunnus, Connection yhteys) throws SQLException {

        try (PreparedStatement statement = yhteys.prepareStatement("""
        SELECT kayttajatunnus FROM kayttajat WHERE kayttajatunnus = ?
      """)) {
            statement.setString(1,kayttajatunnus);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
               return false;
            } else {
                return true;
            }

        }
    }

}
