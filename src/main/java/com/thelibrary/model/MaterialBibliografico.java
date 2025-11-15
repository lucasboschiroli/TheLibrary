package com.thelibrary.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "material_bibliografico")
public abstract class MaterialBibliografico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 150)
    private String autor;

    @Column(nullable = false, length = 50)
    private String status;

    public MaterialBibliografico() {}

    public MaterialBibliografico(String titulo, String autor, String status) {
        this.titulo = titulo;
        this.autor = autor;
        this.status = status;
    }

    public abstract String obterTipo();

    // Getters e Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return obterTipo() + "{id=" + id + ", titulo='" + titulo + "', autor='" + autor + "', status='" + status + "'}";
    }
}