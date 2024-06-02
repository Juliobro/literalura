package com.juliobro.literalura.main;

import com.juliobro.literalura.models.*;
import com.juliobro.literalura.repository.*;
import com.juliobro.literalura.service.*;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import java.util.*;
import java.util.stream.Collectors;

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
                    case 4: buscarLibro(); break;
                    case 5: buscarAutor(); break;
                    case 6: mostrarAutoresVivos(); break;
                    case 7: mostrarLibrosPorIdioma(); break;
                    case 8: verEstadisticasLibro(); break;
                    case 9: verTop5Libros(); break;
                    case 0:
                        System.out.println("Cerrando la aplicación. ¡Gracias por usar!");
                        break;
                    default:
                        System.out.println("Opción inválida. Por favor, intenta nuevamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor, ingresa una entrada numérica válida.");
                sc.nextLine();
            }
        }
        sc.close();
    }

    private void adicionarLibro() {
        System.out.println("Escribe el nombre del libro que deseas agregar a la BD:");
        DatosBiblioteca resultadoBusqueda = buscarLibroEnAPI();

        Optional<DatosLibro> libroBuscado = resultadoBusqueda.libros()
                .stream()
                .findFirst();

        if (libroBuscado.isPresent()) {
            DatosLibro libroEncontrado = libroBuscado.get();
            System.out.println("¡Libro encontrado!\n" + libroEncontrado);

            Optional<Libro> libroEnBD = repositorioLibro.findByTituloContainsIgnoreCase(libroEncontrado.titulo());
            if (libroEnBD.isEmpty()) {
                DatosAutor autorEncontrado = libroEncontrado.autor().getFirst();

                Autor autor = new Autor(autorEncontrado);
                Optional<Autor> autorEnBD = repositorioAutor.findByNombreContainsIgnoreCase(autor.getNombre());
                if (autorEnBD.isEmpty()) {
                    repositorioAutor.save(autor);
                } else {
                    autor = autorEnBD.get();
                }

                Libro libro = new Libro(libroEncontrado);
                libro.setAutor(autor);
                repositorioLibro.save(libro);

                System.out.println("El libro se ha adicionado a la base de datos correctamente.");

            } else {
                System.out.println("El libro ya se encuentra en la BD.");
            }
        } else {
            System.out.println("Libro no encontrado :(");
        }
    }

    private void mostrarLibros() {
        List<Libro> libros = repositorioLibro.findAll();
        if (!libros.isEmpty()) {
            libros.forEach(System.out::println);
        } else {
            System.out.println("No hay libros en tu BD.");
        }
    }

    private void mostrarAutores() {
        List<Autor> autores = repositorioAutor.findAll();
        if (!autores.isEmpty()) {
            autores.forEach(System.out::println);
        } else {
            System.out.println("No hay autores en tu BD.");
        }
    }

    private void buscarLibro() {
        System.out.println("Escribe el titulo del libro que deseas buscar en la BD:");
        String nombreLibro = sc.nextLine();
        try {
            Optional<Libro> libro = repositorioLibro.findByTituloContainsIgnoreCase(nombreLibro);
            System.out.println(libro.isPresent()
                    ? "¡Libro encontrado!\n" + libro.get()
                    : "Libro no encontrado :(");

        } catch (IncorrectResultSizeDataAccessException e) {
            System.out.println("Por favor, sé más específico con el titulo del libro que deseas buscar");
        }
    }

    private void buscarAutor() {
        System.out.println("Escribe el nombre del autor que deseas buscar en la BD:");
        String nombreAutor = sc.nextLine();
        try {
            Optional<Autor> autor = repositorioAutor.findByNombreContainsIgnoreCase(nombreAutor);
            System.out.println(autor.isPresent()
                    ? "¡Autor encontrado!\n" + autor.get()
                    : "Autor no encontrado :(");

        } catch (IncorrectResultSizeDataAccessException e) {
            System.out.println("Por favor, sé más específico con el nombre del autor que deseas buscar");
        }
    }

    private void mostrarAutoresVivos() {
        System.out.println("Ingresa el año en el que el autor estuvo vivo:");
        int anio = sc.nextInt(); sc.nextLine();
        List<Autor> autoresVivos = repositorioAutor.buscarAutoresVivos(anio);
        if (!autoresVivos.isEmpty()) {
            autoresVivos.forEach(System.out::println);
        } else {
            System.out.println("No se encontraron autores vivos en el año " + anio);
        }
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
            System.out.println("El código de idioma proporcionaste no se encuentra en la base de datos.");
        }
    }

    private void verEstadisticasLibro() {
        System.out.println("Ingresa el nombre del libro del que deseas ver estadísticas:");
        DatosBiblioteca resultadoBusqueda = buscarLibroEnAPI();
        var libroEncontrado = resultadoBusqueda.libros();

        if (!libroEncontrado.isEmpty()) {
            System.out.println("¡Libro encontrado!\n" + libroEncontrado);
            IntSummaryStatistics est = libroEncontrado.stream()
                    .collect(Collectors.summarizingInt(DatosLibro::descargas));
            System.out.println("Media de descargas: " + est.getAverage() +
                    "\nNúmero mayor de descargas: " + est.getMax() +
                    "\nNúmero menor de descargas: " + est.getMin() +
                    "\nNúmero total de registros contados: " + est.getCount()
            + "\n");
        } else {
            System.out.println("Libro no encontrado :(");
        }
    }

    private void verTop5Libros() {
        List<Libro> topLibros = repositorioLibro.findTop5ByOrderByDescargasDesc();
        if (!topLibros.isEmpty()) {
            System.out.println("Libros mejor valorados:\n********************************");
            for (int i = 0; i < topLibros.size(); i++) {
                System.out.println((i+1) + ". " + topLibros.get(i).getTitulo() +
                        " - " + topLibros.get(i).getDescargas());
            }
            System.out.println("********************************");
        } else {
            System.out.println("Aún no hay libros en tu base de datos.");
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
                4 - Buscar libro por nombre en la BD
                5 - Buscar autor por nombre en la BD
                6 - Mostrar autores vivos en un determinado año
                7 - Mostrar libros por idioma en la BD
                8 - Ver estadísticas de determinado libro
                9 - Mostrar top 5 libros más descargados
                \s
                0 - Salir
                \s""");
    }

    private DatosBiblioteca buscarLibroEnAPI() {
        String nombreLibro = sc.nextLine().replace(" ", "%20");
        String json = consumirAPI.obtenerDatos(URL_BASE + "search=" + nombreLibro);
        return conversor.convertirDatos(json, DatosBiblioteca.class);
    }
}
