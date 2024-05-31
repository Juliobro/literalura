package com.juliobro.literalura.repository;

import com.juliobro.literalura.models.Libro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LibroRepository extends JpaRepository<Libro, Long> {

}
