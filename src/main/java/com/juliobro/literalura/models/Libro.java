package com.juliobro.literalura.models;

import jakarta.persistence.*;

@Entity
@Table(name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;

    private String titulo;
    private String idioma;
    private int descargas;

    public Libro() {}

    public Libro(DatosLibro l) {
        this.autor = new Autor(l.autor().getFirst());
        this.titulo = l.titulo();
        this.idioma = l.idioma().getFirst();
        this.descargas = l.descargas();
    }

    @Override
    public String toString() {
        return "**************** LIBRO ****************" +
                "\nTitulo: '" + titulo + '\'' +
                "\nAutor: " + autor.getNombre() +
                "\nIdioma: " + idioma +
                "\nNúmero de descargas: " + descargas +
                "\n***************************************\n";
    }

    /* ---------------------------- Getters & Setters Zone ---------------------------- */


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Autor getAutor() {
        return autor;
    }

    public void setAutor(Autor autor) {
        this.autor = autor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getIdioma() {
        return idioma;
    }

    public void setIdioma(String idioma) {
        this.idioma = idioma;
    }

    public int getDescargas() {
        return descargas;
    }

    public void setDescargas(int descargas) {
        this.descargas = descargas;
    }
}
