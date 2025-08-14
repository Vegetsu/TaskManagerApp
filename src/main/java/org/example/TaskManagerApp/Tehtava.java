package org.example.TaskManagerApp;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


/*
* TaskManagerin Tehtava-luokka, jossa määritellään getterit ja setterit eri muuttujien arvojen keräämiseen ja asettamiseen
* tietokannan ja TableViewin päivittämistä varten.
 */
public class Tehtava {

    /* SimpleStringProperty ja SimpleIntegerProperty tekevät sen, että näiden arvojen päivittyminen tapahtuisi helposti
    * TableView:ssä.
     */
    private SimpleStringProperty Tehtavanimi, Tehtavakuvaus;

    private SimpleIntegerProperty TehtavaId, IdKayttaja;


    /*
    * Metodi, jota käytetään apuna TableView:n arvojen päivittämiseen. Tietokannasta haetut tiedot pistätään listaan tämän
    * metodin avulla, jolloin TableView:n arvojen päivittyminen onnistuu, jos näitä arvoja muokataan missä vain, esim.
    * tietokannassa. TableView:iin myöhemmin lisätyt tiedot on myös lisätty tämän metodin avulla, jotta niiden arvojen
    * päivittyminen näkyisi jatkossakin TableView:ssä.
     */
    public Tehtava(Integer tehtavaid,String tehtavanimi, String tehtavakuvaus, Integer idkayttaja) {
        Tehtavanimi = new SimpleStringProperty(tehtavanimi);
        Tehtavakuvaus = new SimpleStringProperty(tehtavakuvaus);
        TehtavaId = new SimpleIntegerProperty(tehtavaid);
        IdKayttaja = new SimpleIntegerProperty(idkayttaja);
    }

    public Integer getTehtavaId() {
        return TehtavaId.get();
    }

    public SimpleIntegerProperty tehtavaIdProperty() {
        return TehtavaId;
    }

    public void setTehtavaId(Integer tehtavaId) {
        this.TehtavaId.set(tehtavaId);
    }

    public String getTehtavanimi() {
        return Tehtavanimi.get();
    }

    public String tehtavanimiProperty() {
        return Tehtavanimi.get();
    }

    public void setTehtavanimi(String tehtavanimi) {
        this.Tehtavanimi.set(tehtavanimi);
    }

    public String getTehtavakuvaus() {
        return Tehtavakuvaus.get();
    }

    public String tehtavakuvausProperty() {
        return Tehtavakuvaus.get();
    }

    public void setTehtavakuvaus(String tehtavakuvaus) {
        this.Tehtavakuvaus.set(tehtavakuvaus);
    }

    public Integer getIdKayttaja() {
        return IdKayttaja.get();
    }

    public SimpleIntegerProperty idKayttajaProperty() {
        return IdKayttaja;
    }

    public void setIdKayttaja(Integer idKayttaja) {
        this.IdKayttaja.set(idKayttaja);
    }
}
