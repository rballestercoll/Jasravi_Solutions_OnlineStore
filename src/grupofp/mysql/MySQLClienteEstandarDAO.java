package grupofp.mysql;

import grupofp.dao.ClienteEstandarDAO;
import grupofp.dao.DAOException;
import grupofp.modelo.ClienteEstandar;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class MySQLClienteEstandarDAO implements ClienteEstandarDAO {
    final String INSERT = "INSERT INTO cliente_estandar (email_estandar, nombre, nif, domicilio) VALUES (?,?,?,?);";
    final String GETALL = "SELECT email_estandar, nombre, nif, domicilio FROM cliente_estandar";
    final String UPDATE = "UPDATE cliente_estandar SET nombre=?, nif=?, domicilio=? WHERE email_estandar=?";
    final String DELETE = "DELETE FROM cliente_estandar WHERE email_estandar=?";
    private Connection conn;

    public MySQLClienteEstandarDAO(Connection conn){
        this.conn = conn;
    }

    public MySQLClienteEstandarDAO() {

    }

    @Override
    public void insertar(ClienteEstandar a) throws DAOException {
        conn = new MySQLDAOManager().conectar();
        PreparedStatement stat = null;
        try {
            stat = conn.prepareStatement(INSERT);
            stat.setString(1, a.getEmail());
            stat.setString(2, a.getNombre());
            stat.setString(3, a.getNif());
            stat.setString(4, a.getDomicilio());
            stat.executeUpdate();

        } catch (SQLException ex){
            throw new DAOException("Error en SQL", ex);
        } finally {
            if(stat != null){
                try {
                    stat.close();
                } catch (SQLException ex){
                    throw new DAOException("Error en SQL", ex);
                }
            }

        }try {
            conn.close();
            System.out.println("Se ha desconectado de la bbdd");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modificar(ClienteEstandar a) throws DAOException {
        PreparedStatement stat = null;
        try {
            conn = new MySQLDAOManager().conectar();
            stat = conn.prepareStatement(UPDATE);
            stat.setString(1, a.getNombre());
            stat.setString(2, a.getNif());
            stat.setString(3, a.getDomicilio());
            stat.setString(4, a.getEmail());
            stat.executeUpdate();
        } catch (SQLException ex) {
            throw new DAOException("Error en SQL al modificar", ex);
        } finally {
            // Cerrar recursos
        }
    }

    @Override
    public void eliminar(ClienteEstandar a) throws DAOException {
        PreparedStatement stat = null;
        try {
            conn = new MySQLDAOManager().conectar();
            stat = conn.prepareStatement(DELETE);
            stat.setString(1, a.getEmail());
            stat.executeUpdate();
        } catch (SQLException ex) {
            throw new DAOException("Error en SQL al eliminar", ex);
        } finally {
            // Cerrar recursos
        }
    }

    private ClienteEstandar convertir (ResultSet rs) throws SQLException{
        String email = rs.getString("email_estandar");
        String nombre = rs.getString("nombre");
        String nif = rs.getString("nif");
        String domicilio = rs.getString("domicilio");
        ClienteEstandar ce = new ClienteEstandar(nombre, domicilio, nif, email);

        return ce;
    }
    @Override
    public List<ClienteEstandar> obtenerTodos() throws DAOException {
        conn = new MySQLDAOManager().conectar();
        PreparedStatement stat = null;
        ResultSet rs = null;
        List<ClienteEstandar> clienteEstandars = new ArrayList<>();
        try{
            stat = conn.prepareStatement(GETALL);
            rs = stat.executeQuery();
            while (rs.next()){
                clienteEstandars.add(convertir(rs));
            }
        } catch (SQLException ex){
            throw new DAOException("Error en SQL", ex);
        } finally {
            if (rs != null){
                try {
                    rs.close();
                } catch (SQLException ex){
                    new DAOException("Error en SQL", ex);
                }
            }
            if (stat != null){
                try {
                    stat.close();
                } catch (SQLException ex){
                    new DAOException("Error en SQL", ex);
                }
            }
        }try {
            conn.close();
            System.out.println("Se ha desconectado de la bbdd");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return clienteEstandars;
    }

    @Override
    public ClienteEstandar obtener(Long id) {
        return null;
    }

    /*public static void main(String[] args) throws DAOException, SQLException {
        Connection conn = null;
        final String driver = "com.mysql.cj.jdbc.Driver";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection("jdbc:mysql://teamrocketmysql.mysql.database.azure.com:3306/teamrocket", "Administrador", "Pokemon1234");
            ClienteEstandarDAO dao = new MySQLClienteEstandarDAO(conn);
            ClienteEstandar ce = new ClienteEstandar("rhydty", "verrde", "fedea", "adae@");
            List<ClienteEstandar> clienteEstandars = dao.obtenerTodos();
            for (ClienteEstandar c : clienteEstandars) {
                System.out.println(ce.toString());
            }
            //dao.insertar(ce);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }*/
}
