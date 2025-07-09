package org.example.TaskManagerApp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class SQLTietokanta {

    public static Connection Openconnection() throws SQLException {
        Connection yhteys = DriverManager.getConnection(
                "jdbc:mysql://localhost:" + 3307 + "/" + "TaskManager",
                "root", "root"
        );
        System.out.println("yhteys muodostettu");
        return yhteys;

    }

    public  static ObservableList<Tehtava> Tehtavatiedot() throws SQLException {
        Connection conn = Openconnection();
        ObservableList<Tehtava> lista = FXCollections.observableArrayList();

        try {
            PreparedStatement ps = conn.prepareStatement("Select * from Tehtävä");
            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                //lista.add(new Tehtava())
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return lista;
    }
}
