package com.thelibrary.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "revista")
public class Revista extends MaterialBibliografico {

    @Column(nullable = false, length = 150)
    private String revista;

    @Column(name = "numero_edicao", nullable = false, length = 20)
    private String numeroEdicao;

    @Column(name = "data_edicao")
    @Temporal(TemporalType.DATE)
    private Date dataDeEdicao;

    public Revista() {}

    public Revista(String titulo, String autor, String status,
                   String revista, String numeroEdicao, Date dataDeEdicao) {
        super(titulo, autor, status);
        this.revista = revista;
        this.numeroEdicao = numeroEdicao;
        this.dataDeEdicao = dataDeEdicao;
    }

    @Override
    public String obterTipo() {
        return "Revista";
    }

    public String getRevista() { return revista; }
    public void setRevista(String revista) { this.revista = revista; }

    public String getNumeroEdicao() { return numeroEdicao; }
    public void setNumeroEdicao(String numeroEdicao) {
        this.numeroEdicao = numeroEdicao;
    }

    public Date getDataDeEdicao() { return dataDeEdicao; }
    public void setDataDeEdicao(Date dataDeEdicao) {
        this.dataDeEdicao = dataDeEdicao;
    }

    @Override
    public String toString() {
        return super.toString() + ", revista='" + revista + "', numeroEdicao='" + numeroEdicao + "', dataEdicao=" + dataDeEdicao;
    }
}