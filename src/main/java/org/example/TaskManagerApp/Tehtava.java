package org.example.TaskManagerApp;

import javafx.beans.property.SimpleStringProperty;

public class Tehtava {
    private SimpleStringProperty Tehtavanimi, Tehtavakuvaus;

    public Tehtava(String tehtavanimi, String tehtavakuvaus) {
        Tehtavanimi = new SimpleStringProperty(tehtavanimi);
        Tehtavakuvaus = new SimpleStringProperty(tehtavakuvaus);
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
}
