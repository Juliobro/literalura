package com.juliobro.literalura.main;

import com.juliobro.literalura.service.ConsumirAPI;
import com.juliobro.literalura.service.ConversorDatos;

import java.util.Scanner;

public class Main {
    private Scanner sc = new Scanner(System.in);
    private ConsumirAPI consumirAPI = new ConsumirAPI();
    private ConversorDatos conversor = new ConversorDatos();
    private final String URL_BASE = "https://gutendex.com/books?";

    public void ejecutarMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    ******* Bienvenido a nuestro buscador de series *******
                    1 - Adicionar serie
                    2 - Adicionar episodios
                    3 - Mostrar lista de Series en la BD
                    4 - Buscar series por titulo
                    5 - Buscar series por categoría
                                 \s
                    0 - Salir
                    *******************************************************
                   \s""";
            System.out.println(menu);
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1: adicionarLibro(); break;
                case 2: mostrarLibros(); break;
                case 3: mostrarAutores(); break;
                case 4: mostrarAutoresVivos(); break;
                case 5: mostrarLibrosPorIdioma(); break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida. Por favor, intenta nuevamente.");
            }
        }
        sc.close();
    }

    private void adicionarLibro() {

    }

    private void mostrarLibros() {

    }

    private void mostrarAutores() {

    }

    private void mostrarAutoresVivos() {

    }

    private void mostrarLibrosPorIdioma() {

    }
}