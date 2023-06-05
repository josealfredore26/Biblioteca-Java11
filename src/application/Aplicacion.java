package application;

import java.util.Map;

import controller.ModelFactory;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import model.Libro;
import model.Autor;
import model.Biblioteca;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;


public class Aplicacion extends Application {
    private TableView<Libro> librosTable;
    private TableView<Autor> autoresTable;
    private TextField tituloField;
    private TextField autorField;
    private TextField isbnField;
    private TextField anioField;
    private Button agregarLibroButton;
    private TextField buscarLibroField;
    private Button buscarLibroButton;
    private Button eliminarLibroButton;
    private Button agregarAutorButton;
    private TextField nombreAutorField;
    private TextField nacionalidadAutorField;
    private Button eliminarAutorButton;

    private Biblioteca biblioteca;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        biblioteca = Biblioteca.getInstance();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Tab librosTab = new Tab("Libros");
        librosTab.setContent(createLibrosTabContent());
        Tab autoresTab = new Tab("Autores");
        autoresTab.setContent(createAutoresTabContent());

        tabPane.getTabs().addAll(librosTab, autoresTab);

        HBox bottomBox = new HBox(10);
        bottomBox.setAlignment(Pos.CENTER_RIGHT);
        bottomBox.setPadding(new Insets(10));

        tituloField = new TextField();
        tituloField.setPromptText("Título");
        autorField = new TextField();
        autorField.setPromptText("Autor");
        isbnField = new TextField();
        isbnField.setPromptText("ISBN");
        anioField = new TextField();
        anioField.setPromptText("Año");

        agregarLibroButton = new Button("Agregar Libro");
        agregarLibroButton.setOnAction(e -> agregarLibro());

        buscarLibroField = new TextField();
        buscarLibroField.setPromptText("ISBN del libro a buscar");

        buscarLibroButton = new Button("Buscar Libro");
        buscarLibroButton.setOnAction(e -> buscarLibro());

        eliminarLibroButton = new Button("Eliminar Libro");
        eliminarLibroButton.setOnAction(e -> eliminarLibro());

        nombreAutorField = new TextField();
        nombreAutorField.setPromptText("Nombre");
        nacionalidadAutorField = new TextField();
        nacionalidadAutorField.setPromptText("Nacionalidad");

        agregarAutorButton = new Button("Agregar Autor");
        agregarAutorButton.setOnAction(e -> agregarAutor());

        eliminarAutorButton = new Button("Eliminar Autor");
        eliminarAutorButton.setOnAction(e -> eliminarAutor());

        bottomBox.getChildren().addAll(
                tituloField, autorField, isbnField, anioField, agregarLibroButton,
                buscarLibroField, buscarLibroButton, eliminarLibroButton,
                nombreAutorField, nacionalidadAutorField, agregarAutorButton,
                eliminarAutorButton);

        root.setCenter(tabPane);
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Biblioteca");
        primaryStage.show();
    }

    private TableView<Libro> createLibrosTable() {
        TableView<Libro> table = new TableView<>();

        TableColumn<Libro, String> tituloColumn = new TableColumn<>("Título");
        tituloColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTitulo()));

        TableColumn<Libro, String> autorColumn = new TableColumn<>("Autor");
        autorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAutor().getNombre()));

        TableColumn<Libro, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIsbn()));

        TableColumn<Libro, String> anioColumn = new TableColumn<>("Año");
        anioColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getAnioCreacion())));


        table.getColumns().addAll(tituloColumn, autorColumn, isbnColumn, anioColumn);

        return table;
    }

    private TableView<Autor> createAutoresTable() {
        TableView<Autor> table = new TableView<>();
        
        TableColumn<Autor, Integer> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());

        TableColumn<Autor, String> nombreColumn = new TableColumn<>("Nombre");
        nombreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));

        TableColumn<Autor, String> nacionalidadColumn = new TableColumn<>("Nacionalidad");
        nacionalidadColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNacionalidad()));

        table.getColumns().addAll(idColumn, nombreColumn, nacionalidadColumn);

        return table;
    }

    private void agregarLibro() {
        String titulo = tituloField.getText();
        int idAutor = Integer.parseInt(autorField.getText());
        String isbn = isbnField.getText();
        int anio = Integer.parseInt(anioField.getText());
        Libro libro = biblioteca.agregarLibro(titulo, idAutor, anio, isbn);
        librosTable.getItems().add(libro);

        limpiarCampos();
    }

    private void buscarLibro() {
        String isbn = buscarLibroField.getText();
        
        if (isbn.isEmpty()) {
            mostrarAlerta("Error de búsqueda", "Por favor, ingresa un ISBN válido.");
            return;
        }
        try {
        	Libro libroEncontrado = biblioteca.buscarLibro(isbn);
            if (libroEncontrado != null) {
                mostrarVentanaDetalleLibro(libroEncontrado);
            } else {
                mostrarAlerta("Libro no encontrado", "No se encontró un libro con el ID proporcionado.");
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de búsqueda", "Por favor, ingresa un ISBN válido.");
        }
    }

    private void eliminarLibro() {
        int indiceSeleccionado = librosTable.getSelectionModel().getSelectedIndex();
        if (indiceSeleccionado >= 0) {
            Libro libro = librosTable.getItems().get(indiceSeleccionado);
            biblioteca.eliminarLibro(libro);
            librosTable.getItems().remove(indiceSeleccionado);
        }
    }

    private void agregarAutor() {
        String nombre = nombreAutorField.getText();
        String nacionalidad = nacionalidadAutorField.getText();
        Autor autor = biblioteca.agregarAutor(nombre, nacionalidad);
        autoresTable.getItems().add(autor);

        limpiarCampos();
    }

    private void eliminarAutor() {
        int indiceSeleccionado = autoresTable.getSelectionModel().getSelectedIndex();
        if (indiceSeleccionado >= 0) {
            Autor autor = autoresTable.getItems().get(indiceSeleccionado);
            biblioteca.eliminarAutor(autor);
            autoresTable.getItems().remove(indiceSeleccionado);
        }
    }

    private void limpiarCampos() {
        tituloField.clear();
        autorField.clear();
        isbnField.clear();
        anioField.clear();
        buscarLibroField.clear();
        nombreAutorField.clear();
        nacionalidadAutorField.clear();
    }

    private BorderPane createLibrosTabContent() {
        BorderPane pane = new BorderPane();

        librosTable = createLibrosTable();
        pane.setCenter(librosTable);

        return pane;
    }

    private BorderPane createAutoresTabContent() {
        BorderPane pane = new BorderPane();

        autoresTable = createAutoresTable();
        pane.setCenter(autoresTable);

        return pane;
    }
    

	private void mostrarAlerta(String titulo, String mensaje) {
	    Alert alerta = new Alert(Alert.AlertType.INFORMATION);
	    alerta.setTitle(titulo);
	    alerta.setHeaderText(null);
	    alerta.setContentText(mensaje);
	    alerta.showAndWait();
	}
	
	private void mostrarVentanaDetalleLibro(Libro libro) {
	    Stage ventanaDetalle = new Stage();
	    ventanaDetalle.setTitle("Detalle del libro");

	    Label lblTitulo = new Label("Título: " + libro.getTitulo());
	    Label lblAutor = new Label("Autor: " + libro.getAutor());
	    Label lblISBN = new Label("ISBN: " + libro.getIsbn());
	    Label lblAnio = new Label("Año: " + libro.getAnioCreacion());

	    VBox contenedor = new VBox(10);
	    contenedor.setAlignment(Pos.CENTER);
	    contenedor.getChildren().addAll(lblTitulo, lblAutor, lblISBN, lblAnio);

	    Scene escena = new Scene(contenedor, 300, 200);
	    ventanaDetalle.setScene(escena);
	    ventanaDetalle.show();
	}

	private void mostrarVentanaDetalleAutor(Autor autor) {
	    Stage ventanaDetalle = new Stage();
	    ventanaDetalle.setTitle("Detalle del autor");

	    Label lblNombre = new Label("Nombre: " + autor.getNombre());
	    Label lblNacionalidad = new Label("Nacionalidad: " + autor.getNacionalidad());

	    VBox contenedor = new VBox(10);
	    contenedor.setAlignment(Pos.CENTER);
	    contenedor.getChildren().addAll(lblNombre, lblNacionalidad);

	    Scene escena = new Scene(contenedor, 300, 200);
	    ventanaDetalle.setScene(escena);
	    ventanaDetalle.show();
	}

}

