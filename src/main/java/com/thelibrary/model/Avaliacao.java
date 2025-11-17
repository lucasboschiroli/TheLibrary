package com.thelibrary.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "avaliacao")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "material_id", nullable = false)
    private MaterialBibliografico material;

    @Column(nullable = false)
    private Integer nota; // 1 a 5

    @Temporal(TemporalType.DATE)
    @Column(name = "data_avaliacao", nullable = false)
    private Date dataAvaliacao;

    // Construtores
    public Avaliacao() {
        this.dataAvaliacao = new Date();
    }

    public Avaliacao(Usuario usuario, MaterialBibliografico material, Integer nota) {
        this();
        this.usuario = usuario;
        this.material = material;
        this.nota = nota;
    }

    // Getters e Setters (mantenha todos, remova apenas getComentario/setComentario)
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public MaterialBibliografico getMaterial() { return material; }
    public void setMaterial(MaterialBibliografico material) { this.material = material; }

    public Integer getNota() { return nota; }
    public void setNota(Integer nota) { this.nota = nota; }

    public Date getDataAvaliacao() { return dataAvaliacao; }
    public void setDataAvaliacao(Date dataAvaliacao) { this.dataAvaliacao = dataAvaliacao; }

    @Override
    public String toString() {
        return "Avaliacao{" +
                "id=" + id +
                ", usuario=" + usuario.getNome() +
                ", material=" + material.getTitulo() +
                ", nota=" + nota +
                ", dataAvaliacao=" + dataAvaliacao +
                '}';
    }
}