package grupofp.vista;

import grupofp.dao.DAOException;
import java.sql.Connection;

public class OnlineStore {
    public static void main(String[] args) {
        GestionOs gestion = new GestionOs();
        gestion.inicio();

        DAOException connector = new DAOException();
        Connection connection = connector.getConnection();
    }
}