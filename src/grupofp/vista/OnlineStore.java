package grupofp.vista;

import grupofp.modelo.MySQLConnector;
import java.sql.Connection;

public class OnlineStore {
    public static void main(String[] args) {
        GestionOs gestion = new GestionOs();
        gestion.inicio();

        MySQLConnector connector = new MySQLConnector();
        Connection connection = connector.getConnection();
    }
}