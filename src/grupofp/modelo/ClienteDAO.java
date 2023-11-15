package grupofp.modelo;

import java.util.ArrayList;
public interface ClienteDAO {
    Cliente getClienteById(int clienteId);
    void addCliente(Cliente cliente);
    void updateCliente(Cliente cliente);
    void deleteCliente(int clienteId);
    void getAllClientes();
}
