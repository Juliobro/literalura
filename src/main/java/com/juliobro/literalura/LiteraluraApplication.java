package com.juliobro.literalura;

import com.juliobro.literalura.main.Main;
import com.juliobro.literalura.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    private final AutorRepository repositorioAutor;
    private final LibroRepository repositorioLibro;

    public LiteraluraApplication(AutorRepository repositorioAutor, LibroRepository repositorioLibro) {
        this.repositorioAutor = repositorioAutor;
        this.repositorioLibro = repositorioLibro;
    }

    @Override
    public void run(String... args) {
        Main main = new Main(repositorioAutor, repositorioLibro);
        main.ejecutarMenu();
    }
}
