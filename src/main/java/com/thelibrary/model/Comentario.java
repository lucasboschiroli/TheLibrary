package com.thelibrary.model;

import javax.persistence.*;

@Entity
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String comentario;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private MaterialBibliografico materialBibliografico;

    public String getComentario() {
        return comentario;
    }

    public Integer getId() {
        return id;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public MaterialBibliografico getMaterialBibliografico() {
        return materialBibliografico;
    }

    public void setMaterialBibliografico(MaterialBibliografico materialBibliografico) {
        this.materialBibliografico = materialBibliografico;
    }
}
