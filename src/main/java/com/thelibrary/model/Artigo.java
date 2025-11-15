package com.thelibrary.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "artigo")
public class Artigo extends MaterialBibliografico {

    @Column(nullable = false, length = 150)
    private String revista;

    @Column(nullable = false, length = 100)
    private String revisor;

    @Column(name = "data_revisao")
    @Temporal(TemporalType.DATE)
    private Date dataDeRevisao;

    public Artigo() {}

    public Artigo(String titulo, String autor, String status,
                  String revista, String revisor, Date dataDeRevisao) {
        super(titulo, autor, status);
        this.revista = revista;
        this.revisor = revisor;
        this.dataDeRevisao = dataDeRevisao;
    }

    @Override
    public String obterTipo() {
        return "Artigo";
    }

    public String getRevista() { return revista; }
    public void setRevista(String revista) { this.revista = revista; }

    public String getRevisor() { return revisor; }
    public void setRevisor(String revisor) { this.revisor = revisor; }

    public Date getDataDeRevisao() { return dataDeRevisao; }
    public void setDataDeRevisao(Date dataDeRevisao) {
        this.dataDeRevisao = dataDeRevisao;
    }

    @Override
    public String toString() {
        return super.toString() + ", revista='" + revista + "', revisor='" + revisor + "', dataRevisao=" + dataDeRevisao;
    }
}