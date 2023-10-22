import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import grupofp.controlador.Controlador;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.time.format.DateTimeFormatter;

public class ControladorTest {
    private Controlador controlador;

    @Before
    public void setUp() {
        controlador = new Controlador();
    }

    @Test
    public void testEntradaArticulo() {
        controlador.entradaArticulo("A001", "Artículo de prueba", 10.0f, 2.0f, 2);
        assertTrue(controlador.getDatos().getListaArticulos().getLista().stream().anyMatch(a -> a.getIdArticulo().equals("A001")));
    }

    @Test
    public void testEntradaCliente() {
        controlador.entradaCliente("Cliente1", "Dirección", "NIF001", "cliente1@example.com", 0.1f);
        assertTrue(controlador.getDatos().getListaClientes().getLista().stream().anyMatch(c -> c.getEmail().equals("cliente1@example.com")));
    }

//    @Test
//    public void testEntradaPedido() {
//        String fechaString = "01/02/2020/12/25"; // Make sure it matches your pattern
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy/HH/mm");
//        LocalDateTime fecha = LocalDateTime.parse(fechaString, formatter);
//
//        boolean pedidoExiste = controlador.entradaPedido(1, 5, fecha, "cliente1@example.com", "A001");
//        assertTrue(pedidoExiste);
//    }


    @Test
    public void testEliminarPedido() {
        controlador.entradaPedido(2, 3, LocalDateTime.now(), "cliente2@example.com", "A002");
        controlador.eliminarPedido(2);
        assertFalse(controlador.getDatos().getListaPedidos().getLista().stream().anyMatch(p -> p.getNumPedido() == 2));
    }

    @Test
    public void testRecogerTodosArticulos() {
        ArrayList<String> articulos = controlador.recogerTodosArticulos();
        assertNotNull(articulos);
        assertEquals(0, articulos.size()); // No debería haber artículos en este punto
    }

    @Test
    public void testRecogerTodosClientes() {
        ArrayList<String> clientes = controlador.recogerTodosClientes();
        assertNotNull(clientes);
        assertEquals(0, clientes.size()); // No debería haber clientes en este punto
    }

    @Test
    public void testRecogerClienteEstandar() {
        ArrayList<String> clientesEstandar = controlador.recogerClienteEstandar();
        assertNotNull(clientesEstandar);
        assertEquals(0, clientesEstandar.size()); // No debería haber clientes estándar en este punto
    }

    @Test
    public void testRecogerClientePremium() {
        ArrayList<String> clientesPremium = controlador.recogerClientePremium();
        assertNotNull(clientesPremium);
        assertEquals(0, clientesPremium.size()); // No debería haber clientes premium en este punto
    }
}
