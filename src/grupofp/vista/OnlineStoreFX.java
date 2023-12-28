package grupofp.vista;

import grupofp.controlador.Controlador;
import grupofp.modelo.Cliente;
import grupofp.modelo.ClienteEstandar;
import grupofp.modelo.ClientePremium;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.io.InputStream;

public class OnlineStoreFX extends Application {
    private GestionOs gestion = new GestionOs();
    private Controlador controlador;
    Scanner teclado = new Scanner(System.in);
    private Stage primaryStage;
    private Scene mainMenuScene;
    private Scene clientesScene;
    private ObservableList<String> pedidosEnviados = FXCollections.observableArrayList();

    public OnlineStoreFX() {
        controlador = new Controlador();
    }

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
        mostrarClientesBtn.setOnAction(e -> mostrarClientes());
        mostrarClientesEstandarBtn.setOnAction(e -> mostrarClientesEstandar());
        mostrarClientesPremiumBtn.setOnAction(e -> mostrarClientesPremium());
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

    private void mostrarClientes() {
        ArrayList<String> cliT = controlador.recogerTodosClientes();
        TableView<String> tableView = new TableView<>();
        ObservableList<String> data = FXCollections.observableArrayList(cliT);

        TableColumn<String, String> clienteColumn = new TableColumn<>("Clientes");
        clienteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        tableView.getColumns().addAll(clienteColumn);
        tableView.setItems(data);

        Button volverBtn = new Button("Volver al Menú Principal");
        volverBtn.setOnAction(e -> primaryStage.setScene(primaryStage.getScene()));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(tableView, volverBtn);

        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
    }

    private void mostrarClientesEstandar() {

        ArrayList<String> cliE = controlador.recogerClienteEstandar();
        TableView<String> tableView = new TableView<>();
        ObservableList<String> data = FXCollections.observableArrayList(cliE);

        TableColumn<String, String> clienteColumn = new TableColumn<>("Clientes Estándar");
        clienteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        tableView.getColumns().addAll(clienteColumn);
        tableView.setItems(data);

        Button volverBtn = new Button("Volver al Menú Principal");
        volverBtn.setOnAction(e -> primaryStage.setScene(primaryStage.getScene()));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(tableView, volverBtn);

        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
    }

    private void mostrarClientesPremium() {
        ArrayList<String> cliP = controlador.recogerClientePremium();
        TableView<String> tableView = new TableView<>();
        ObservableList<String> data = FXCollections.observableArrayList(cliP);

        TableColumn<String, String> clienteColumn = new TableColumn<>("Clientes Premium");
        clienteColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));

        tableView.getColumns().addAll(clienteColumn);
        tableView.setItems(data);

        Button volverBtn = new Button("Volver al Menú Principal");
        volverBtn.setOnAction(e -> primaryStage.setScene(primaryStage.getScene()));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(tableView, volverBtn);

        Scene scene = new Scene(vbox, 600, 400);
        primaryStage.setScene(scene);
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
        addArticuloBtn.setOnAction(e -> addArticulo());
        mostrarArticulosBtn.setOnAction(e -> mostrarArticulos());
        volverBtn.setOnAction(e -> primaryStage.setScene(primaryStage.getScene()));

        // Añadir botones al grid
        grid.add(addArticuloBtn, 0, 0);
        grid.add(mostrarArticulosBtn, 0, 1);
        grid.add(volverBtn, 0, 2);

        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarArticulos() {
        ArrayList<String> aArt = controlador.recogerTodosArticulos();

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Añadir encabezados
        gridPane.add(new Label("ID"), 0, 0);
        gridPane.add(new Label("Descripción"), 1, 0);
        gridPane.add(new Label("Precio"), 2, 0);
        gridPane.add(new Label("Gastos de envío"), 3, 0);
        gridPane.add(new Label("Tiempo de preparación"), 4, 0);

        int row = 1; // Comenzar desde la fila 1 para los datos

        for (String articulo : aArt) {
            // Separar los datos del artículo
            String[] datos = articulo.split(",");

            // Crear etiquetas para cada dato
            Label idLabel = new Label(datos[0]);
            Label descripcionLabel = new Label(datos[1]);
            Label precioLabel = new Label(datos[2]);
            Label gastosEnvioLabel = new Label(datos[3]);
            Label tiempoPreparacionLabel = new Label(datos[4]);

            // Añadir etiquetas al GridPane
            gridPane.add(idLabel, 0, row);
            gridPane.add(descripcionLabel, 1, row);
            gridPane.add(precioLabel, 2, row);
            gridPane.add(gastosEnvioLabel, 3, row);
            gridPane.add(tiempoPreparacionLabel, 4, row);

            row++; // Moverse a la siguiente fila
        }

        // Crear una escena y mostrarla en una ventana (Stage)
        Scene scene = new Scene(gridPane, 600, 400);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Lista de Artículos");
        stage.show();
    }

    private void addArticulo() {
        Stage stage = new Stage();
        stage.setTitle("Añadir Artículo");

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label idLabel = new Label("Id de Artículo:");
        TextField idTextField = new TextField();

        Label descripcionLabel = new Label("Descripción:");
        TextField descripcionTextField = new TextField();

        Label precioLabel = new Label("Precio:");
        TextField precioTextField = new TextField();

        Label gastosEnvioLabel = new Label("Gastos de Envío:");
        TextField gastosEnvioTextField = new TextField();

        Label tiempoPreparacionLabel = new Label("Tiempo de Preparación:");
        TextField tiempoPreparacionTextField = new TextField();

        Button confirmarBtn = new Button("Confirmar");
        confirmarBtn.setOnAction(e -> {
            // Obtener los valores de los campos de texto
            String id = idTextField.getText();
            String descripcion = descripcionTextField.getText();
            float precio = Float.parseFloat(precioTextField.getText());
            float gastosEnvio = Float.parseFloat(gastosEnvioTextField.getText());
            int tiempoPreparacion = Integer.parseInt(tiempoPreparacionTextField.getText());

            // Llamar al método para agregar el artículo
            controlador.entradaArticulo(id, descripcion, precio, gastosEnvio, tiempoPreparacion);

            System.out.println("Se ha añadido el nuevo Artículo");
            stage.close(); // Cerrar la ventana después de añadir el artículo
        });

        // Añadir nodos al GridPane
        gridPane.add(idLabel, 0, 0);
        gridPane.add(idTextField, 1, 0);
        gridPane.add(descripcionLabel, 0, 1);
        gridPane.add(descripcionTextField, 1, 1);
        gridPane.add(precioLabel, 0, 2);
        gridPane.add(precioTextField, 1, 2);
        gridPane.add(gastosEnvioLabel, 0, 3);
        gridPane.add(gastosEnvioTextField, 1, 3);
        gridPane.add(tiempoPreparacionLabel, 0, 4);
        gridPane.add(tiempoPreparacionTextField, 1, 4);
        gridPane.add(confirmarBtn, 0, 5, 2, 1);

        // Crear una escena y mostrarla en la ventana
        Scene scene = new Scene(gridPane, 300, 250);
        stage.setScene(scene);
        stage.show();
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
        addPedidoBtn.setOnAction(e -> addPedido());
        eliminarPedidoBtn.setOnAction(e -> eliminarPedido());
        mostrarPendientesBtn.setOnAction(e -> mostrarPedidosPendientes());
        mostrarEnviadosBtn.setOnAction(e -> mostrarPedidosEnviados());
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

    private void addPedido() {
        Stage stage = new Stage();
        stage.setTitle("Añadir Pedido");
        stage.initModality(Modality.APPLICATION_MODAL);

        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label numPedidoLabel = new Label("Número de Pedido:");
        TextField numPedidoTextField = new TextField();

        Label cantidadLabel = new Label("Cantidad:");
        TextField cantidadTextField = new TextField();

        Label fechaLabel = new Label("Fecha y Hora del Pedido (dd/MM/yyyy/HH/mm):");
        TextField fechaTextField = new TextField();

        Label emailLabel = new Label("Email del Cliente:");
        TextField emailTextField = new TextField();

        Label idArticuloLabel = new Label("Id de Artículo:");
        TextField idArticuloTextField = new TextField();

        Button confirmarBtn = new Button("Confirmar");
        confirmarBtn.setOnAction(e -> {
            // Obtener los valores de los campos de texto
            int numPedido = Integer.parseInt(numPedidoTextField.getText());
            int cantidad = Integer.parseInt(cantidadTextField.getText());
            LocalDateTime fecha = LocalDateTime.parse(fechaTextField.getText(), DateTimeFormatter.ofPattern("dd/MM/yyyy/HH/mm"));
            String email = emailTextField.getText();
            String idArticulo = idArticuloTextField.getText();

            // Verificar si el cliente existe, si no, abrir la ventana para añadir cliente
            if (!controlador.existeC(email)) {
                mostrarVentanaAddCliente();
            }

            // Llamar al método para agregar el pedido
            controlador.entradaPedido(numPedido, cantidad, fecha, email, idArticulo);

            System.out.println("Se ha añadido el nuevo Pedido");
            stage.close(); // Cerrar la ventana después de añadir el pedido
        });

        // Añadir nodos al GridPane
        gridPane.add(numPedidoLabel, 0, 0);
        gridPane.add(numPedidoTextField, 1, 0);
        gridPane.add(cantidadLabel, 0, 1);
        gridPane.add(cantidadTextField, 1, 1);
        gridPane.add(fechaLabel, 0, 2);
        gridPane.add(fechaTextField, 1, 2);
        gridPane.add(emailLabel, 0, 3);
        gridPane.add(emailTextField, 1, 3);
        gridPane.add(idArticuloLabel, 0, 4);
        gridPane.add(idArticuloTextField, 1, 4);
        gridPane.add(confirmarBtn, 0, 5, 2, 1);

        // Crear una escena y mostrarla en la ventana
        Scene scene = new Scene(gridPane, 500, 300);
        stage.setScene(scene);
        stage.showAndWait(); // Mostrar la ventana y esperar hasta que se cierre
    }

    private void mostrarPedidosEnviados() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Botones para cada opción del menú
        Button mostrarTodosBtn = new Button("Mostrar todos los Pedidos Enviados");
        Button filtrarClienteBtn = new Button("Filtrar por Cliente");
        Button volverBtn = new Button("Volver al Menú Principal");

        // EventHandlers para cada botón
        mostrarTodosBtn.setOnAction(e -> mostrarTodosEnviados());
        filtrarClienteBtn.setOnAction(e -> filtrarClienteEnv());
        volverBtn.setOnAction(e -> primaryStage.setScene(primaryStage.getScene()));

        // Añadir botones al grid
        grid.add(mostrarTodosBtn, 0, 0);
        grid.add(filtrarClienteBtn, 0, 1);
        grid.add(volverBtn, 0, 2);

        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void mostrarTodosEnviados() {
        // Llenar pedidosEnviados con datos de controlador.todosEnviados()
        pedidosEnviados.clear();
        pedidosEnviados.addAll(controlador.todosEnviados());

        // Crear un GridPane para mostrar los pedidos enviados
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Agregar una etiqueta para mostrar los pedidos enviados
        Label pedidosLabel = new Label("Pedidos Enviados:");
        gridPane.add(pedidosLabel, 0, 0);

        // Agregar pedidos al GridPane
        for (int i = 0; i < pedidosEnviados.size(); i++) {
            Label pedido = new Label(pedidosEnviados.get(i));
            gridPane.add(pedido, 0, i + 1);
        }

        // Botón para volver al menú principal
        Button volverBtn = new Button("Volver al Menú Principal");
        volverBtn.setOnAction(e -> primaryStage.setScene(primaryStage.getScene()));
        gridPane.add(volverBtn, 0, pedidosEnviados.size() + 1);

        // Crear una escena y mostrarla en una nueva ventana
        Scene pedidosScene = new Scene(gridPane, 400, 300);
        Stage pedidosStage = new Stage();
        pedidosStage.setScene(pedidosScene);
        pedidosStage.show();
    }

    private void filtrarClienteEnv() {
        Stage stage = new Stage();
        stage.setTitle("Filtrar por Cliente Enviado");
        stage.initModality(Modality.APPLICATION_MODAL);

        // Crear un GridPane para la ventana de filtrado
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Agregar una etiqueta y un TextField para ingresar el correo del cliente
        Label emailLabel = new Label("Introduce email del cliente:");
        TextField emailTextField = new TextField();
        gridPane.add(emailLabel, 0, 0);
        gridPane.add(emailTextField, 1, 0);

        // Botón para confirmar el filtrado
        Button confirmarBtn = new Button("Confirmar");
        confirmarBtn.setOnAction(e -> {
            // Obtener el correo del TextField
            String email = emailTextField.getText();

            // Realizar el filtrado por cliente y mostrar los resultados en un GridView
            mostrarResultadosFiltradoCliente(controlador.filtrarClienteEnviado(email));

            stage.close(); // Cerrar la ventana después de filtrar
        });
        gridPane.add(confirmarBtn, 0, 1, 2, 1);

        // Crear una escena y mostrarla en la ventana de filtrado
        Scene scene = new Scene(gridPane, 300, 150);
        stage.setScene(scene);
        stage.showAndWait(); // Mostrar la ventana y esperar hasta que se cierre
    }

    private void mostrarResultadosFiltradoCliente(ArrayList<String> resultados) {
        // Crear un GridPane para mostrar los resultados del filtrado
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Agregar una etiqueta para mostrar los resultados
        Label resultadosLabel = new Label("Resultados del Filtrado por Cliente:");
        gridPane.add(resultadosLabel, 0, 0);

        // Agregar resultados al GridPane
        for (int i = 0; i < resultados.size(); i++) {
            Label resultado = new Label(resultados.get(i));
            gridPane.add(resultado, 0, i + 1);
        }

        // Mostrar la ventana de resultados
        Stage resultadosStage = new Stage();
        resultadosStage.setTitle("Resultados del Filtrado por Cliente");
        resultadosStage.setScene(new Scene(gridPane, 400, 300));
        resultadosStage.show();
    }


    private void mostrarPedidosPendientes() {
        // Obtener la lista de todos los pedidos pendientes
        ArrayList<String> aTodosPendientes = controlador.todosPendientes();

        // Crear un GridPane para mostrar los pedidos pendientes
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Agregar una etiqueta para mostrar los pedidos pendientes
        Label pedidosLabel = new Label("Pedidos Pendientes:");
        gridPane.add(pedidosLabel, 0, 0);

        // Agregar pedidos pendientes al GridPane
        for (int i = 0; i < aTodosPendientes.size(); i++) {
            Label pedido = new Label(aTodosPendientes.get(i));
            gridPane.add(pedido, 0, i + 1);
        }

        // Botón para volver al menú principal
        Button volverBtn = new Button("Volver al Menú Principal");
        volverBtn.setOnAction(e -> primaryStage.setScene(mainMenuScene));
        gridPane.add(volverBtn, 0, aTodosPendientes.size() + 1);

        // Crear una escena y mostrarla en una nueva ventana
        Scene pedidosScene = new Scene(gridPane, 400, 300);
        Stage pedidosStage = new Stage();
        pedidosStage.setTitle("Pedidos Pendientes");
        pedidosStage.setScene(pedidosScene);
        pedidosStage.show();
    }


    private void eliminarPedido() {
        // Crear una nueva ventana para la eliminación de pedido
        Stage stage = new Stage();
        stage.setTitle("Eliminar Pedido");
        stage.initModality(Modality.APPLICATION_MODAL);

        // Crear un GridPane para la ventana de eliminación
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        // Agregar una etiqueta y un TextField para ingresar el número de pedido a eliminar
        Label numPedidoLabel = new Label("Número de Pedido:");
        TextField numPedidoTextField = new TextField();
        gridPane.add(numPedidoLabel, 0, 0);
        gridPane.add(numPedidoTextField, 1, 0);

        // Botón para confirmar la eliminación
        Button confirmarBtn = new Button("Confirmar");
        confirmarBtn.setOnAction(e -> {
            // Obtener el número de pedido del TextField
            int numPedido = Integer.parseInt(numPedidoTextField.getText());

            // Llamar al método para eliminar el pedido
            controlador.eliminarPedido(numPedido);

            System.out.println("Se ha eliminado el pedido con número: " + numPedido);
            stage.close(); // Cerrar la ventana después de eliminar el pedido
        });
        gridPane.add(confirmarBtn, 0, 1, 2, 1);

        // Crear una escena y mostrarla en la ventana de eliminación
        Scene scene = new Scene(gridPane, 300, 150);
        stage.setScene(scene);
        stage.showAndWait(); // Mostrar la ventana y esperar hasta que se cierre
    }

}
