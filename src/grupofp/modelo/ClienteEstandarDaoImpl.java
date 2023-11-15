package grupofp.modelo;

import java.sql.*;

public class ClienteEstandarDaoImpl implements ClienteDAO {

    @Override
    public Cliente getClienteById(int clienteId) {
        // Implementa la lógica para recuperar un cliente por su ID desde la base de datos

        try {
            MySQLConnector connector = new MySQLConnector();
            // Establecer la conexión con la base de datos
            Connection conexion = connector.getConnection();

            String sql = "{call ObtenerClienteEstandarPorID(?, ?)}";
            CallableStatement callableStatement = conexion.prepareCall(sql);

            callableStatement.setInt(1, clienteId);
            callableStatement.registerOutParameter(2, Types.INTEGER);

            callableStatement.execute();

            int hayResults = callableStatement.getInt(2);

            if (hayResults == 1) {
                ResultSet resultSet = callableStatement.getResultSet();
                resultSet.next();
                int id = resultSet.getInt("idCliente");
                String nombre = resultSet.getString("nombre");
                String domicilio = resultSet.getString("domicilio");
                String nif = resultSet.getString("nif");
                String email = resultSet.getString("email");
                System.out.println("ID: " + id + " Nombre: " + nombre + " Domicilio: " + domicilio + " NIF: " + nif + " Email: " + email);
            }

            else System.out.println("El cliente con el ID " + clienteId + " no existe como cliente estándar");

            callableStatement.close();
            conexion.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addCliente(Cliente cliente) {

        try {

            MySQLConnector connector = new MySQLConnector();

            Connection conexion = connector.getConnection();

            String sql = "{call insertarClienteEstandar(?, ?, ?, ?, ?)}";
            CallableStatement callableStatement = conexion.prepareCall(sql);

            callableStatement.setInt(1, cliente.getId());
            callableStatement.setString(2, cliente.getNombre());
            callableStatement.setString(3, cliente.getDomicilio());
            callableStatement.setString(4, cliente.getNif());
            callableStatement.setString(5, cliente.getEmail());

            callableStatement.execute();

            callableStatement.close();
            conexion.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCliente(Cliente cliente) {
        // Implementa la lógica para actualizar un cliente en la base de datos
        // O en la lista de clientes en memoria
        // Puedes utilizar el ID del cliente para identificarlo y realizar la actualización
    }

    @Override
    public void deleteCliente(int clienteId) {
        // Implementa la lógica para eliminar un cliente de la base de datos
        // O de la lista de clientes en memoria
    }

    @Override
    public void getAllClientes() {
        // Implementa la lógica para obtener todos los clientes desde la base de datos

        try {
            MySQLConnector connector = new MySQLConnector();
            // Establecer la conexión con la base de datos
            Connection conexion = connector.getConnection();

            String sql = "{call obtenerClientesEstandar()}";
            CallableStatement callableStatement = conexion.prepareCall(sql);

            boolean tieneResults = callableStatement.execute();

            if (tieneResults) {
                ResultSet resultSet = callableStatement.getResultSet();
                while (resultSet.next()) {
                    int id = resultSet.getInt("idCliente");
                    String nombre = resultSet.getString("nombre");
                    String domicilio = resultSet.getString("domicilio");
                    String nif = resultSet.getString("nif");
                    String email = resultSet.getString("email");
                    System.out.println("ID: " + id + " Nombre: " + nombre + " Domicilio: " + domicilio + " NIF: " + nif + " Email: " + email);
                }
            }

            callableStatement.close();
            conexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
