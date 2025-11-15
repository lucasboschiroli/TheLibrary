package com.thelibrary.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "livro")
public class Livro extends MaterialBibliografico {

    @Column(nullable = false, length = 100)
    private String editora;

    @Column(name = "data_publicacao")
    @Temporal(TemporalType.DATE)
    private Date dataDePublicacao;

    public Livro() {}

    public Livro(String titulo, String autor, String status,
                 String editora, Date dataDePublicacao) {
        super(titulo, autor, status);
        this.editora = editora;
        this.dataDePublicacao = dataDePublicacao;
    }

    @Override
    public String obterTipo() {
        return "Livro";
    }

    public String getEditora() { return editora; }
    public void setEditora(String editora) { this.editora = editora; }

    public Date getDataDePublicacao() { return dataDePublicacao; }
    public void setDataDePublicacao(Date dataDePublicacao) {
        this.dataDePublicacao = dataDePublicacao;
    }

    @Override
    public String toString() {
        return super.toString() + ", editora='" + editora + "', dataPublicacao=" + dataDePublicacao;
    }
}