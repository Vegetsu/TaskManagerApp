package org.example.TaskManagerApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/*
*Taskmanagerin SQL-tietokanta-luokka, jossa määritellään toiminnallisuudet MySql-tietokannan kanssa, ja jonka metodeja
* käytetään apuna muissa luokissa.
*  -Hakee TableView:llä näytettävät tiedot tietokannasta
*  -Määrittelee tietokantatoiminnallisuudet eri toiminnoille, joita TableView:iin on mahdollista tehdä, kuten
*   tehtävän kuvauksen ja nimen lisäyksen, muokkauksen ja poiston sekä käyttäjän lisäyksen ja poiston
*  -Määrittelee eri käyttäjän varmistuksia, joita käytetään apuna virheiden välttymiseksi, ettei tietokantaan esim. lisätä
*   duplikaattikäyttäjiä
 */

public class SQLTietokanta {

    // Jos haluaa käyttää MySQL, vaihda private static final String DB_TYPE = "mysql".
    private static final String DB_TYPE = "sqLite";


/*
* Metodi, joka valitsee, että yhdistetäänkö MySQL-tietokantaan vai SQLite-tietokantaan käyttämällä ylempänä annettua arvoa sqLite tai mysql.
 */
    public static Connection Openconnection() throws SQLException, IOException {
        if (DB_TYPE.equalsIgnoreCase("mysql")) {
            return openMySQLConnection();
        } else if (DB_TYPE.equalsIgnoreCase("sqlite")) {
            return openSQLiteConnection();
        } else {
            throw new IllegalArgumentException("Tuntematon tietokantatyyppi: " + DB_TYPE);
        }
    }


    /*
    * Metodi, joka avaa yhteyden MySql-tietokantaan. Hakee tiedot config.properties kansiosta, jotta tietokannan
    * arkaluontoiset tiedot säilyvät turvassa.
     */
    public static Connection openMySQLConnection() throws SQLException, IOException {
        Properties props = new Properties();

        // Määritetään config.properties käytettäväksi tietokannan kirjautumistietojen hakemiseen.
        try (InputStream input = new FileInputStream("src/main/resources/config.properties")) {
            props.load(input);
        }


        // Tietokannan osoite, tietokannan käyttäjän nimi sekä tietokannan salasana, jotka haetaan config.properties kansiosta
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

       // String SQLite_url = "jdbc:sqlite:tehtavat.db";

        // Muodostetaan yhteys
        Connection yhteys = DriverManager.getConnection(url, user, password);
        System.out.println("Yhteys muodostettu");

        return yhteys;
    }

    /*
    * Metodi, joka avaa yhteyden SQLite-tietokantaan.
     */
    private static Connection openSQLiteConnection() throws SQLException {
        String SQLite_url = "jdbc:sqlite:tehtavat.db";
        Connection yhteys = DriverManager.getConnection(SQLite_url);
        System.out.println("Yhteys SQLite:en muodostettu");

        initializeSQLiteDatabase(yhteys);
        return yhteys;
    }

    /*
    * Valmistelee SQLite-tietokannan valmiiksi, jos sitä ei ole vielä rakennettu.
     */
    private static void initializeSQLiteDatabase(Connection conn) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS kayttajat (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            kayttajatunnus TEXT UNIQUE NOT NULL,
                            salasana_hash TEXT NOT NULL
                        )
                    """);

            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS tehtavamanager (
                            IdTehtava INTEGER PRIMARY KEY AUTOINCREMENT,
                            NimiTehtava TEXT NOT NULL,
                            KuvausTehtava TEXT,
                            IdKayttaja INTEGER,
                            FOREIGN KEY (IdKayttaja) REFERENCES kayttajat(id)
                        )
                    """);
        }
    }


    /*
    * Metodi jolla haetaan tietokannasta kaikki tietyn henkilön tehtävätiedot ja asetetaan ne sitten listaan, jota
    * käytetään TableView:ssä.
     */
    public  static ObservableList<Tehtava> Tehtavatiedot(Integer kayttajaid) throws SQLException, IOException {
        Connection conn = Openconnection();
        ObservableList<Tehtava> lista = FXCollections.observableArrayList();

        try {
            // Valitaan kaikki tehtävätiedot tietokannasta valitun käyttäjän id:n mukaan.
            PreparedStatement ps = conn.prepareStatement("Select * from tehtavamanager WHERE idKayttaja = ?");

            ps.setInt(1, kayttajaid);
            ResultSet rs = ps.executeQuery();

            // Lisätään tiedot TableView:ssä käytettävään listaan.
            while (rs.next()){
                lista.add(new Tehtava(Integer.parseInt(rs.getString("idTehtava")), rs.getString("NimiTehtava"), rs.getString("KuvausTehtava"), Integer.parseInt(rs.getString("idKayttaja"))));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return lista;
    }


    /*
    * Metodi, jolla lisätään tehtävä tietokantaan. Metodi myös palauttaa nykyisen käyttäjän id:n, jotta se voidaan heti
    * lisätä TableViewiin jottei poiston kanssa tule ongelmia.
     */
    public static int lisaatehtava(String nimi, String kuvaus, Integer kayttajaid, Connection yhteys) throws SQLException {

        // Tehtävän lisäys tietokantaan. Lisätään tehtävän nimi, tehtävän kuvaus ja nykyisen käyttäjän id.
        String sql = """
        INSERT INTO tehtavamanager(NimiTehtava, KuvausTehtava, IdKayttaja)
        VALUES (?,?,?)
    """;

        try (PreparedStatement statement = yhteys.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, nimi);
            statement.setString(2, kuvaus);
            statement.setInt(3, kayttajaid);

            statement.executeUpdate();


            // Lisätyn käyttäjän id:n palautus
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        return -1;
    }

/*
* Metodi joka päivittää tehtävän kuvauksen tietokantaan tehtävän id:n avulla.
 */
    public static void PaivitaKuvaus(String kuvaus,Integer id, Connection yhteys) throws SQLException {

        try (PreparedStatement statement = yhteys.prepareStatement("UPDATE tehtavamanager SET KuvausTehtava=? WHERE IdTehtava=?")) {
            statement.setString(1, kuvaus);
            statement.setInt(2, id);
            statement.executeUpdate();

        }
    }

    /*
    * Metodi joka päivittää tehtävän nimen tietokantaan tehtävän id:n avulla.
     */
    public static void PaivitaNimi(String nimi,Integer id, Connection yhteys) throws SQLException {

        try (PreparedStatement statement = yhteys.prepareStatement("UPDATE tehtavamanager SET NimiTehtava=? WHERE IdTehtava=?")) {
            statement.setString(1, nimi);
            statement.setInt(2, id);
            statement.executeUpdate();

        }
    }


    /*
    * Metodi, joka poistaa tehtävän tietokannasta tehtävän id:n mukaan.
     */
    public static void poistaTehtava(Integer id,Connection yhteys) throws SQLException {

        try (PreparedStatement statement = yhteys.prepareStatement("DELETE FROM tehtavamanager WHERE IdTehtava=?")){
            statement.setInt(1,id);
            statement.executeUpdate();
        }
    }

    /*
    * Metodi, joka lisää käyttäjän tietokantaan. Tietokantaan lisätään käyttäjätunnus ja salasana, joka hashataan.
     */
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

    /*
    * Metodi, jota käytetään apuna oikean käyttäjän tunnistamisessa. Käyttäjätunnuksen avulla etsitään oikea käyttäjä
    * tietokannasta, jonka löydyttyä käytetään UserService-luokan verifyPassword-metodia hashatun salasanan varmistukseen.
     */
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

/*
* Metodi, jolla varmistetaan käyttäjän olemassaolo tietokannassa. Metodi käyttää käyttäjätunnusta apuna etsiäkseen tätä
* tietokannasta, ja jos tämän nimistä käyttäjää ei ole olemassa, se palauttaa falsen, jonka avulla toisessa metodissa
* heitetään virheviesti käyttäjälle.
 */
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

    /*
    * Metodi, jonka avulla talletetaan käyttäjän id kayttaja-muuttujaan kirjautumisvaiheessa
    * TaskManagerLoginController- metodissa, jonka jälkeen tätä muuttujaa käytetään etsimään oikean käyttäjän tiedot
    * tietokannasta TableViewiin.
     */
    public static int valitseKayttaja(String kayttajatunnus, Connection yhteys) throws SQLException {
        try (PreparedStatement statement = yhteys.prepareStatement("""
        SELECT id FROM kayttajat WHERE kayttajatunnus = ?
    """)) {
            statement.setString(1, kayttajatunnus);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                } else {
                    throw new SQLException("Käyttäjää ei löytynyt: " + kayttajatunnus);
                }
            }
        }
    }


}
