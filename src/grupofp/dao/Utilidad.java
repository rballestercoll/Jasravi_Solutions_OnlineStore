package grupofp.dao;

import java.sql.*;
import java.sql.Connection;

public class Utilidad {
    protected Connection conexion;
    private final String url = "jdbc:mysql://localhost:3306/onlinestore";
    private final String user = "root";
    private final String password = "recelus777";
    private final String driver = "com.mysql.cj.jdbc.Driver";
    Connection cx;
    public Utilidad(){

    }

    public Connection conectar(){
        try {
            Class.forName(driver);
            cx = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa a la base de datos MySQL");

        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al conectar a la base de datos:");
            throw new RuntimeException(e);
        }
        return cx;
    }

    public void desconectar() throws SQLException{
        if(cx != null){
            if(!cx.isClosed()){
                cx.close();
            }
        }
    }
}
