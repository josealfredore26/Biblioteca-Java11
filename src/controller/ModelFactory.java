package controller;

import model.Biblioteca;
//Clase ModelFactory
public class ModelFactory {
    private static Biblioteca biblioteca;

    public static Biblioteca getBiblioteca() {
        if (biblioteca == null) {
            biblioteca = Biblioteca.getInstance();
        }
        return biblioteca;
    }
}
