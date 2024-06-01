package com.juliobro.literalura.main;

import com.juliobro.literalura.models.*;
import com.juliobro.literalura.repository.AutorRepository;
import com.juliobro.literalura.repository.LibroRepository;
import com.juliobro.literalura.service.ConsumirAPI;
import com.juliobro.literalura.service.ConversorDatos;

import java.util.*;

public class Main {
    private Scanner sc = new Scanner(System.in);
    private ConsumirAPI consumirAPI = new ConsumirAPI();
    private ConversorDatos conversor = new ConversorDatos();
    private final String URL_BASE = "https://gutendex.com/books/?";
    private final AutorRepository repositorioAutor;
    private final LibroRepository repositorioLibro;

    public Main(AutorRepository repositorioAutor, LibroRepository repositorioLibro) {
        this.repositorioAutor = repositorioAutor;
        this.repositorioLibro = repositorioLibro;
    }

    public void ejecutarMenu() {
        var opcion = -1;
        mensajeBienvenida();

        while (opcion != 0) {
            menuSeleccion();
            try {
                opcion = sc.nextInt();
                sc.nextLine();

                switch (opcion) {
                    case 1: adicionarLibro(); break;
                    case 2: mostrarLibros(); break;
                    case 3: mostrarAutores(); break;
                    case 4: mostrarAutoresVivos(); break;
                    case 5: mostrarLibrosPorIdioma(); break;
                    case 0:
                        System.out.println("Cerrando la aplicación. ¡Gracias por usar!");
                        break;
                    default:
                        System.out.println("Opción inválida. Por favor, intenta nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor, ingresa una entrada numérica válida");
                sc.nextLine();
            }
        }
        sc.close();
    }

    private void adicionarLibro() {
        System.out.println("Escribe el nombre del libro que deseas agregar a la BD:");
        String nombreLibro = sc.nextLine().replace(" ", "%20");
        String json = consumirAPI.obtenerDatos(URL_BASE + "search=" + nombreLibro);
        DatosBiblioteca resultadoBusqueda = conversor.convertirDatos(json, DatosBiblioteca.class);

        Optional<DatosLibro> libroBuscado = resultadoBusqueda.libros()
                .stream()
                .findFirst();

        if (libroBuscado.isPresent()) {
            DatosLibro libroEncontrado = libroBuscado.get();
            System.out.println("¡Libro encontrado!\n" + libroEncontrado);

            Optional<Libro> libroEnBD = repositorioLibro.findByTitulo(libroEncontrado.titulo());
            if (libroEnBD.isEmpty()) {
                DatosAutor autorEncontrado = libroEncontrado.autor().getFirst();

                Autor autor = new Autor(autorEncontrado);
                Optional<Autor> autorEnBD = repositorioAutor.findByNombre(autor.getNombre());
                if (autorEnBD.isEmpty()) {
                    repositorioAutor.save(autor);
                } else {
                    autor = autorEnBD.get();
                }

                Libro libro = new Libro(libroEncontrado);
                libro.setAutor(autor);
                repositorioLibro.save(libro);

                System.out.println("El libro se ha adicionado a la base de datos correctamente");

            } else {
                System.out.println("El libro ya se encuentra en la BD");
            }
        } else {
            System.out.println("Libro no encontrado :(");
        }
    }

    private void mostrarLibros() {
        List<Libro> libros = repositorioLibro.findAll();
        libros.forEach(System.out::println);
    }

    private void mostrarAutores() {
        List<Autor> autores = repositorioAutor.findAll();
        autores.forEach(System.out::println);
    }

    private void mostrarAutoresVivos() {
        System.out.println("Ingresa el año en el que el autor estuvo vivo:");
        int anio = sc.nextInt(); sc.nextLine();
        List<Autor> autoresVivos = repositorioAutor.buscarAutoresVivos(anio);
        autoresVivos.forEach(System.out::println);
    }

    private void mostrarLibrosPorIdioma() {
        List<String> idiomasEnBD = repositorioLibro.listarIdiomasEnBD();
        Set<String> idiomas = new HashSet<>(idiomasEnBD);

        System.out.println("A continuación están los códigos de idioma que están en la base de datos:");
        idiomas.forEach(i -> System.out.println("- " + i));
        System.out.println("Ingresa ahora el código de idioma correspondiente a los libros que deseas ver:");
        String codigoIdioma = sc.nextLine();

        if (idiomas.contains(codigoIdioma)) {
            List<Libro> libros = repositorioLibro.listarLibrosSegunIdioma(codigoIdioma);
            libros.forEach(System.out::println);
        } else {
            System.out.println("El código de idioma proporcionaste no se encuentra en la base de datos :(");
        }
    }

    private static void mensajeBienvenida() {
        System.out.println("""
                 ************ ¡Bienvenido a la aplicación Literalura! ************
                                \s
                 A continuación encontrarás la lista de opciones disponibles para
                 interactuar con los libros y autores de la BD. Simplemente selecciona
                 el número correspondiente de la acción que desees realizar hasta que
                 decidas terminar, entonces debes oprimir el número '0'.
                                \s
                 ¡Espero disfrutes la experiencia!
                \s""");
    }

    private static void menuSeleccion() {
        System.out.println("""
                1 - Adicionar libros
                2 - Mostrar libros en la BD
                3 - Mostrar autores en la BD
                4 - Mostrar autores vivos en un determinado año
                5 - Mostrar libros por idioma
                \s
                0 - Salir
                \s""");
    }
}
