package com.juliobro.literalura.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "autores")
public class Autor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "autor", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Libro> libros;

    private String nombre;
    private int fechaNacimiento;
    private int fechaMuerte;

    public Autor() {}

    public Autor(DatosAutor a) {
        this.nombre = a.nombre();
        this.fechaNacimiento = a.fechaNacimiento();
        this.fechaMuerte = a.fechaMuerte();
    }

    @Override
    public String toString() {
        return "**************** AUTOR ****************" +
                "\nNombre: " + nombre +
                "\nNacimiento: " + fechaNacimiento +
                "\nMuerte: " + fechaMuerte +
                "\nLibros en la BD: " + libros.stream()
                .map(l -> "'"+l.getTitulo()+"'")
                .collect(Collectors.joining(", ")) +
                "\n***************************************\n";
    }

    /* ---------------------------- Getters & Setters Zone ---------------------------- */


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(int fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public int getFechaMuerte() {
        return fechaMuerte;
    }

    public void setFechaMuerte(int fechaMuerte) {
        this.fechaMuerte = fechaMuerte;
    }
}
