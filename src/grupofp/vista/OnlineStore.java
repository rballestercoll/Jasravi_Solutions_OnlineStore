package grupofp.vista;

import grupofp.dao.DAOException;
import grupofp.modelo.MySQLConnector;
import grupofp.mysql.MySQLDAOManager;
import java.sql.Connection;
import java.sql.SQLException;

public class OnlineStore {
    public static void main(String[] args) {
        GestionOs gestion = new GestionOs();
        gestion.inicio();



        // Conexion DAO ok
        try (Connection connection = new MySQLDAOManager().conectar()) {
            // Código que utiliza la conexión
        } catch (SQLException e) {
            // Manejo de excepciones
            e.printStackTrace();
        }



        // Conexión sin uso de DAO
//        MySQLConnector connector = new MySQLConnector();
//        Connection connection = connector.getConnection();
    }
}