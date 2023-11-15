package grupofp.modelo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.sql.SQLException;

public class ClientePremiumDaoImpl implements ClienteDAO {

    @Override
    public Cliente getClienteById(int clienteId) {
        // Implementa la lógica para recuperar un cliente por su ID desde la base de datos
        // O desde la lista de clientes en memoria
        try {
            MySQLConnector connector = new MySQLConnector();
            // Establecer la conexión con la base de datos
            Connection conexion = connector.getConnection();

            // Definir la consulta SQL
            String consulta = "SELECT * FROM cliente WHERE idCliente = ?";

            // Crear una declaración SQL
            PreparedStatement preparedStatement = conexion.prepareStatement(consulta);

            preparedStatement.setInt(1, clienteId);

            // Ejecutar la consulta y obtener un conjunto de resultados
            ResultSet resultado = preparedStatement.executeQuery();

            // Recorrer y mostrar los resultados
            if (resultado.next()) {
                int id = resultado.getInt("idCliente");
                String nombre = resultado.getString("nombre");
                String domicilio = resultado.getString("domicilio");
                String nif = resultado.getString("nif");
                String email = resultado.getString("email");

                System.out.println("ID: " + id + ", Nombre: " + nombre + ", Domicilio: " + domicilio + ", NIF: " + nif + ", email: " + email);
            }
            else {
                System.out.println("El cliente con el ID " + clienteId + " no existe en la bbdd.");
            }

            // Cerrar los recursos
            resultado.close();
            preparedStatement.close();
            conexion.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addCliente(Cliente cliente) {
        // Implementa la lógica para agregar un cliente a la base de datos
        // O a la lista de clientes en memoria
        try {

            MySQLConnector connector = new MySQLConnector();

            Connection conexion = connector.getConnection();

            String sql = "{call insertarClientePremium(?, ?, ?, ?, ?, ?)}";
            CallableStatement callableStatement = conexion.prepareCall(sql);

            callableStatement.setInt(1, cliente.getId());
            callableStatement.setString(2, cliente.getNombre());
            callableStatement.setString(3, cliente.getDomicilio());
            callableStatement.setString(4, cliente.getNif());
            callableStatement.setString(5, cliente.getEmail());
            callableStatement.setFloat(6, ((ClientePremium) cliente).getDescuento());

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
        // O desde la lista de clientes en memoria
        // Definir la información de conexión a la base de datos


        try {
            MySQLConnector connector = new MySQLConnector();
            // Establecer la conexión con la base de datos
            Connection conexion = connector.getConnection();

            String sql = "{call obtenerClientesPremium()}";
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
                    float descuento = resultSet.getFloat("descuento");
                    System.out.println("ID: " + id + " Nombre: " + nombre + " Domicilio: " + domicilio + " NIF: " + nif + " Email: " + email + " Descuento: " + descuento);
                }
            }

            callableStatement.close();
            conexion.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
