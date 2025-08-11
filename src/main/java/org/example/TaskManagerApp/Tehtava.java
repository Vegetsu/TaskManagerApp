package org.example.TaskManagerApp;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Tehtava {
    private SimpleStringProperty Tehtavanimi, Tehtavakuvaus;

    private SimpleIntegerProperty TehtavaId, IdKayttaja;

    public Tehtava(Integer tehtavaid,String tehtavanimi, String tehtavakuvaus, Integer idkayttaja) {
        Tehtavanimi = new SimpleStringProperty(tehtavanimi);
        Tehtavakuvaus = new SimpleStringProperty(tehtavakuvaus);
        TehtavaId = new SimpleIntegerProperty(tehtavaid);
        IdKayttaja = new SimpleIntegerProperty(idkayttaja);
    }

    public Tehtava(String tehtavanimi, String tehtavakuvaus) {
        Tehtavanimi = new SimpleStringProperty(tehtavanimi);
        Tehtavakuvaus = new SimpleStringProperty(tehtavakuvaus);
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
