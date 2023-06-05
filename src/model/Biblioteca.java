package model;

import java.util.*;

import javafx.collections.ObservableList;
//Clase Biblioteca
public class Biblioteca {

	private Map<String, Libro> libros;
    private Map<Integer, Autor> autores;
    private int nextLibroId;
    private int nextAutorId;

    private static Biblioteca instance;

    private Biblioteca() {
        libros = new HashMap<>();
        autores = new HashMap<>();
        nextLibroId = 1;
        nextAutorId = 1;
    }

    public static Biblioteca getInstance() {
        if (instance == null) {
            instance = new Biblioteca();
        }
        return instance;
    }

    public void agregarLibro(Libro libro) {
        libros.put(libro.getIsbn(), libro);
    }

    public Libro agregarLibro(String titulo, int idAutor, int anioCreacion, String isbn) {
        int id = nextLibroId++;
        Libro libro = new Libro(titulo, buscarAutor(id), anioCreacion, isbn);
        libros.put(isbn, libro);
        return libro;
    }

    public void eliminarLibro(Libro libro) {
        libros.remove(libro.getIsbn());
    }

    public void eliminarLibro(String isbn) {
        libros.remove(isbn);
    }

    public List<Libro> obtenerLibros() {
        return new ArrayList<>(libros.values());
    }

    public Libro buscarLibro(String isbn) {
        return libros.get(isbn);
    }

    public void actualizarLibro(String titulo, int idAutor, int anioCreacion, String isbn) {
        Libro libro = buscarLibro(isbn);
        if (libro != null) {
            libro.setTitulo(titulo);
            libro.setAutor(buscarAutor(idAutor));
            libro.setAnioCreacion(anioCreacion);
        }
    }

    public void agregarAutor(Autor autor) {
        autores.put(autor.getId(), autor);
    }

    public Autor agregarAutor(String nombre, String nacionalidad) {
        int id = nextAutorId++;
        Autor autor = new Autor(id, nombre, nacionalidad);
        autores.put(id, autor);
        return autor;
    }

    public void eliminarAutor(Autor autor) {
        autores.remove(autor.getId());
    }

    public void eliminarAutor(int id) {
        autores.remove(id);
    }

    public List<Autor> obtenerAutores() {
        return new ArrayList<>(autores.values());
    }

    public Autor buscarAutor(int id) {
        return autores.get(id);
    }

    public void actualizarAutor(int id, String nuevoNombre, String nuevaNacionalidad) {
        Autor autor = buscarAutor(id);
        if (autor != null) {
            autor.setNombre(nuevoNombre);
            autor.setNacionalidad(nuevaNacionalidad);
        }
    }
	
}
