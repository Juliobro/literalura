package com.juliobro.literalura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public record DatosBiblioteca(
        @JsonAlias("count") int totalResultados,
        @JsonAlias("results")List<DatosLibro> libros
) {
}
