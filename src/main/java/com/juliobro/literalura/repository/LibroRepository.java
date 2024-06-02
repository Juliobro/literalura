package com.juliobro.literalura.repository;

import com.juliobro.literalura.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {
    Optional<Libro> findByTituloContainsIgnoreCase(String titulo);

    @Query("SELECT l.idioma FROM Libro l")
    List<String> listarIdiomasEnBD();

    @Query("SELECT l FROM Libro l WHERE l.idioma ILIKE :codigoIdioma")
    List<Libro> listarLibrosSegunIdioma(String codigoIdioma);

    List<Libro> findTop5ByOrderByDescargasDesc();
}
