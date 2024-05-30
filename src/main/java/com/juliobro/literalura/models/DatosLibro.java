package com.juliobro.literalura.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public record DatosLibro(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatosAutor> autor,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") int descargas
) {
    @Override
    public String toString() {
        return "DatosLibro: {" +
                "titulo='" + titulo + '\'' +
                ", autor='" + autor.getFirst() + '\'' +
                ", idiomas='" + String.join(",", idiomas) +'\''+
                ", descargas=" + descargas +
                '}';
    }
}
