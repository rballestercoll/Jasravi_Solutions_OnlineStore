package grupofp.vista;

import grupofp.modelo.Cliente;
import grupofp.modelo.ClienteEstandar;
import grupofp.modelo.ClientePremium;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.InputStream;

public class OnlineStoreFX extends Application {
    private GestionOs gestion = new GestionOs();
    Scanner teclado = new Scanner(System.in);
    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene clientesScene;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        gestion = new GestionOs();

        primaryStage.setTitle("Online Store");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Button gestionArticulosBtn = new Button("Gestión Artículos");
        gestionArticulosBtn.setOnAction(e -> gestionArticulos(primaryStage));
        grid.add(gestionArticulosBtn, 0, 0);

        Button gestionClientesBtn = new Button("Gestión Clientes");
        gestionClientesBtn.setOnAction(e -> gestionClientes(primaryStage));
        grid.add(gestionClientesBtn, 0, 1);

        Button gestionPedidosBtn = new Button("Gestión Pedidos");
        gestionPedidosBtn.setOnAction(e -> gestionPedidos(primaryStage));
        grid.add(gestionPedidosBtn, 0, 2);

        Button salirBtn = new Button("Salir");
        salirBtn.setOnAction(e -> primaryStage.close());
        grid.add(salirBtn, 0, 3);

        Scene scene = new Scene(grid, 300, 200);
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    private void gestionClientes(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Botones para cada opción del menú
        Button addClienteBtn = new Button("Añadir Cliente");
        addClienteBtn.setOnAction(e -> mostrarVentanaAddCliente());
        Button mostrarClientesBtn = new Button("Mostrar Clientes");
        Button mostrarClientesEstandarBtn = new Button("Mostrar Clientes Estándar");
        Button mostrarClientesPremiumBtn = new Button("Mostrar Clientes Premium");
        Button volverBtn = new Button("Volver al Menú Principal");

        // EventHandlers para cada botón
        addClienteBtn.setOnAction(e -> mostrarVentanaAddCliente());
        mostrarClientesBtn.setOnAction(e -> gestion.mostrarClientes());
        mostrarClientesEstandarBtn.setOnAction(e -> gestion.mostrarClientesEstandar());
        mostrarClientesPremiumBtn.setOnAction(e -> gestion.mostrarClientesPremium());
        volverBtn.setOnAction(e -> primaryStage.setScene(primaryStage.getScene()));

        // Añadir botones al grid
        grid.add(addClienteBtn, 0, 0);
        grid.add(mostrarClientesBtn, 0, 1);
        grid.add(mostrarClientesEstandarBtn, 0, 2);
        grid.add(mostrarClientesPremiumBtn, 0, 3);
        grid.add(volverBtn, 0, 4);

        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarVentanaAddCliente() {
        Stage ventanaAddCliente = new Stage();
        ventanaAddCliente.setTitle("Añadir Cliente");

        // Crear los elementos de la ventana (TextField, etc.) para ingresar los datos del cliente
        TextField nombreField = new TextField();
        TextField domicilioField = new TextField();
        TextField nifField = new TextField();
        TextField emailField = new TextField();

        // ChoiceBox para seleccionar el tipo de cliente
        ChoiceBox<String> tipoClienteChoice = new ChoiceBox<>();
        tipoClienteChoice.getItems().addAll("Estandar", "Premium");
        tipoClienteChoice.setValue("Estandar");

        // Crear un TextField para el descuento
        TextField descuentoField = new TextField();
        descuentoField.setPromptText("Descuento");
        descuentoField.setDisable(true);  // Inicialmente deshabilitado

        // Añadir un ChangeListener para habilitar o deshabilitar el TextField de descuento
        tipoClienteChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if ("Premium".equals(newValue)) {
                descuentoField.setDisable(false);
            } else {
                descuentoField.setDisable(true);
            }
        });

        // Crear un botón para confirmar y añadir el cliente
        Button confirmarBtn = new Button("Confirmar");
        confirmarBtn.setOnAction(e -> {
            // Obtener los datos del TextField y llamar directamente a la función addCliente en GestionOs
            String tipoCliente = tipoClienteChoice.getValue();
            float descuento = 0.0f;

            // Verificar si es Cliente Premium y obtener el descuento
            if ("Premium".equals(tipoCliente)) {
                // Obtener el descuento directamente del TextField
                String descuentoText = descuentoField.getText();
                if (!descuentoText.isEmpty()) {
                    descuento = Float.parseFloat(descuentoText);
                }
            }

            addCliente(
                    nombreField.getText(),
                    domicilioField.getText(),
                    nifField.getText(),
                    emailField.getText(),
                    tipoCliente,
                    descuento
            );

            ventanaAddCliente.close(); // Cerrar la ventana después de añadir el cliente
        });

        // Crear un diseño para la ventana
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
                new Label("Nombre: "), nombreField,
                new Label("Domicilio: "), domicilioField,
                new Label("NIF: "), nifField,
                new Label("Email: "), emailField,
                new Label("Tipo de Cliente: "), tipoClienteChoice,
                descuentoField,
                confirmarBtn
        );
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout, 300, 500);
        ventanaAddCliente.setScene(scene);
        ventanaAddCliente.show();
    }

    private void addCliente(String nombre, String domicilio, String nif, String email, String tipoCliente, Float descuento) {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("hibernate.cfg.xml");

        System.out.println("Antes de crear el EntityManagerFactory");
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("onlinestore");
        System.out.println("Después de crear el EntityManagerFactory");

        EntityManager em = emf.createEntityManager();

        em.getTransaction().begin();

        Cliente nuevoCliente;
        if ("Estandar".equals(tipoCliente)) {
            nuevoCliente = new ClienteEstandar(nombre, domicilio, nif, email);
        } else if ("Premium".equals(tipoCliente)) {
            nuevoCliente = new ClientePremium(nombre, domicilio, nif, email, descuento);
        } else {
            // Tratar otro caso o lanzar una excepción, según tus necesidades
            throw new IllegalArgumentException("Tipo de cliente no válido");
        }

        em.persist(nuevoCliente);

        em.getTransaction().commit();

        em.close();
        emf.close();

        System.out.println("Se ha añadido nuevo cliente " + tipoCliente);
    }



    private void gestionArticulos(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Botones para cada opción del menú
        Button addArticuloBtn = new Button("Añadir Articulo");
        Button mostrarArticulosBtn = new Button("Mostrar Articulos");
        Button volverBtn = new Button("Volver al Menú Principal");

        // EventHandlers para cada botón
        addArticuloBtn.setOnAction(e -> gestion.addArticulo());
        mostrarArticulosBtn.setOnAction(e -> gestion.mostrarArticulos());
        volverBtn.setOnAction(e -> primaryStage.setScene(primaryStage.getScene()));

        // Añadir botones al grid
        grid.add(addArticuloBtn, 0, 0);
        grid.add(mostrarArticulosBtn, 0, 1);
        grid.add(volverBtn, 0, 2);

        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void gestionPedidos(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Botones para cada opción del menú
        Button addPedidoBtn = new Button("Añadir Pedido");
        Button eliminarPedidoBtn = new Button("Eliminar Pedido");
        Button mostrarPendientesBtn = new Button("Mostrar Pedidos Pendientes de Envío");
        Button mostrarEnviadosBtn = new Button("Mostrar Pedidos Enviados");
        Button volverBtn = new Button("Volver al Menú Principal");

        // EventHandlers para cada botón
        addPedidoBtn.setOnAction(e -> gestion.addPedido());
        eliminarPedidoBtn.setOnAction(e -> gestion.eliminarPedido());
        mostrarPendientesBtn.setOnAction(e -> gestion.mostrarPedidosPendientes());
        mostrarEnviadosBtn.setOnAction(e -> gestion.mostrarPedidosEnviados());
        volverBtn.setOnAction(e -> primaryStage.setScene(mainMenuScene));

        // Añadir botones al grid
        grid.add(addPedidoBtn, 0, 0);
        grid.add(eliminarPedidoBtn, 0, 1);
        grid.add(mostrarPendientesBtn, 0, 2);
        grid.add(mostrarEnviadosBtn, 0, 3);
        grid.add(volverBtn, 0, 4);

        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

}
