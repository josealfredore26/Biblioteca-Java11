package view;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Autor;
import model.Biblioteca;
import model.Libro;

public class BibliotecaController {

    @FXML
    private BorderPane root;
    @FXML
    private TabPane tabPane;
    @FXML
    private Tab librosTab;
    @FXML
    private Tab autoresTab;
    @FXML
    private TableView<Libro> librosTable;
    @FXML
    private TableView<Autor> autoresTable;
    @FXML
    private TableColumn<Libro, String> tituloColumn;
    @FXML
    private TableColumn<Libro, String> autorColumn;
    @FXML
    private TableColumn<Libro, String> isbnColumn;
    @FXML
    private TableColumn<Libro, String> anioColumn;
    @FXML
    private TableColumn<Autor, Integer> idColumn;
    @FXML
    private TableColumn<Autor, String> nombreColumn;
    @FXML
    private TableColumn<Autor, String> nacionalidadColumn;
    @FXML
    private TextField tituloField;
    @FXML
    private TextField autorField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField anioField;
    @FXML
    private Button agregarLibroButton;
    @FXML
    private TextField buscarLibroField;
    @FXML
    private Button buscarLibroButton;
    @FXML
    private Button eliminarLibroButton;
    @FXML
    private TextField nombreAutorField;
    @FXML
    private TextField nacionalidadAutorField;
    @FXML
    private Button agregarAutorButton;
    @FXML
    private Button eliminarAutorButton;
    @FXML
    private Button actualizarAutorButton;

    private Stage stage;
    private Biblioteca biblioteca;
    
    private Autor autorParaActualizar;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void initialize() {
        biblioteca = Biblioteca.getInstance();
        setupLibrosTableColumns();
        setupAutoresTableColumns();
        
        this.actualizarAutorButton.setDisable(true);

        agregarLibroButton.setOnAction(e -> agregarLibro());
        buscarLibroButton.setOnAction(e -> buscarLibro());
        eliminarLibroButton.setOnAction(e -> eliminarLibro());
        agregarAutorButton.setOnAction(e -> agregarAutor());
        eliminarAutorButton.setOnAction(e -> eliminarAutor());
        actualizarAutorButton.setOnAction(e -> actualizarAutor());
        
        
        autoresTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                this.actualizarAutorButton.setDisable(false);
            }
        });
    }

    private void setupLibrosTableColumns() {
        tituloColumn.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        autorColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAutor().getNombre()));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        anioColumn.setCellValueFactory(new PropertyValueFactory<>("anioCreacion"));
        //anioColumn.setCellValueFactory(cellData -> new SimpleStringProperty(Integer.toString(cellData.getValue().getAnioCreacion())));
    }

    private void setupAutoresTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nombreColumn.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        nacionalidadColumn.setCellValueFactory(new PropertyValueFactory<>("nacionalidad"));
    }

    private void agregarLibro() {
        String titulo = tituloField.getText();
        String idAutor = autorField.getText();
        String isbn = isbnField.getText();
        String anio = anioField.getText();
        
        if(!titulo.isEmpty() && !titulo.isBlank() &&
        		!idAutor.isEmpty() && !idAutor.isBlank() &&
        		!isbn.isEmpty() && !isbn.isBlank() &&
        		!anio.isEmpty() && !anio.isBlank()) {
        	
            Libro libro = biblioteca.agregarLibro(titulo, Integer.parseInt(idAutor), Integer.parseInt(anio), isbn);
            
            librosTable.getItems().add(libro);
            
            mostrarAlerta("Libro añadido", "El libro con ISBN " + libro.getIsbn() + " fue añadido.", AlertType.INFORMATION);
        } else {
        	mostrarAlerta("Datos vacios", "Algun campo esta esta vacio.", AlertType.ERROR);
        }

        limpiarCampos();
    }

    private void buscarLibro() {
        String isbn = buscarLibroField.getText();
        
        if (isbn.isEmpty()) {
            mostrarAlerta("Error de búsqueda", "Por favor, ingresa un ISBN válido.", AlertType.ERROR);
            return;
        }
        try {
        	Libro libroEncontrado = biblioteca.buscarLibro(isbn);
            if (libroEncontrado != null) {
                mostrarVentanaDetalleLibro(libroEncontrado);
            } else {
                mostrarAlerta("Libro no encontrado", "No se encontró un libro con el ID proporcionado.", AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de búsqueda", "Por favor, ingresa un ISBN válido.", AlertType.ERROR);
        }
    }

    private void eliminarLibro() {
        int indiceSeleccionado = librosTable.getSelectionModel().getSelectedIndex();
        if (indiceSeleccionado >= 0) {
            Libro libro = librosTable.getItems().get(indiceSeleccionado);
            biblioteca.eliminarLibro(libro);
            librosTable.getItems().remove(indiceSeleccionado);
            mostrarAlerta("Libro eliminado", "El libro con ISBN " + libro.getIsbn() + " fue eliminado.", AlertType.INFORMATION);
        }
    }

    private void agregarAutor() {
        String nombre = nombreAutorField.getText();
        String nacionalidad = nacionalidadAutorField.getText();
        
        if(!nombre.isEmpty() && !nombre.isBlank() &&
        		!nacionalidad.isEmpty() && !nacionalidad.isBlank()) {
            Autor autor = biblioteca.agregarAutor(nombre, nacionalidad);
            autoresTable.getItems().add(autor);
            
            mostrarAlerta("Autor añadido", "El autor " + autor.getNombre() + " con el id " + autor.getId() + " fue añadido", AlertType.INFORMATION);
            
            actualizarAutores();
        } else {
        	mostrarAlerta("Datos vacios", "Algun campo esta esta vacio.", AlertType.ERROR);
        }

        limpiarCampos();
    }

    private void eliminarAutor() {
        int indiceSeleccionado = autoresTable.getSelectionModel().getSelectedIndex();
        if (indiceSeleccionado >= 0) {
            Autor autor = autoresTable.getItems().get(indiceSeleccionado);
            biblioteca.eliminarAutor(autor);
            actualizarAutores();
            mostrarAlerta("Autor eliminado", "El autor " + autor.getNombre() + " con el id " + autor.getId() + " fue eliminado.", AlertType.INFORMATION);
        }
    }
    
	private void actualizarAutor() {
		int indiceSeleccionado = autoresTable.getSelectionModel().getSelectedIndex();
		if (indiceSeleccionado >= 0) {
			Autor autor = autoresTable.getItems().get(indiceSeleccionado);
			if (autor.equals(autorParaActualizar)) {
				String nuevoNombre = nombreAutorField.getText();
				String nuevaNacionalidad = nacionalidadAutorField.getText();
				if (!nuevoNombre.isEmpty() && !nuevoNombre.isBlank() && !nuevaNacionalidad.isEmpty()
						&& !nuevaNacionalidad.isBlank()) {
					biblioteca.actualizarAutor(autorParaActualizar.getId(), nuevoNombre, nuevaNacionalidad);
					mostrarAlerta("Autor actualizado", "El autor " + autorParaActualizar.getNombre() + " con el id "
							+ autorParaActualizar.getId() + " fue actualizado.", AlertType.INFORMATION);
					actualizarAutores();
					limpiarCampos();
					this.autorParaActualizar = null;
					this.actualizarAutorButton.setDisable(true);
				} else {
					mostrarAlerta("Autor no pudo ser eliminado", "El autor " + autorParaActualizar.getNombre() + " con el id "
							+ autorParaActualizar.getId() + " no fue actualizado porque no hay datos para cambiar.", AlertType.ERROR);
				}
			} else {
				nombreAutorField.setText(autor.getNombre());
				nacionalidadAutorField.setText(autor.getNacionalidad());
				this.autorParaActualizar = autor;
			}
		}

	}
	
	private void actualizarAutores() {
		this.autoresTable.getItems().clear();
		this.autoresTable.getItems().addAll(biblioteca.obtenerAutores());
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
    

	private void mostrarAlerta(String titulo, String mensaje, AlertType alertType) {
	    Alert alerta = new Alert(alertType);
	    alerta.setTitle(titulo);
	    alerta.setHeaderText(null);
	    alerta.setContentText(mensaje);
	    alerta.showAndWait();
	}
	
	private void mostrarVentanaDetalleLibro(Libro libro) {
	    Stage ventanaDetalle = new Stage();
	    ventanaDetalle.setTitle("Detalle del libro");

	    Label lblTitulo = new Label("Título: " + libro.getTitulo());
	    Label lblAutor = new Label("Autor: " + libro.getAutor().getNombre());
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
