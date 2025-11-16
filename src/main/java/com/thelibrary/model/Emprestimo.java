package com.thelibrary.model;

import javax.persistence.*;
import java.util.*;

@Entity
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private Usuario usuario;

    @Temporal(TemporalType.DATE)
    private Date dataEmprestimo;

    @Temporal(TemporalType.DATE)
    private Date dataPrevistaDevolucao;

    @Temporal(TemporalType.DATE)
    private Date dataDevolucao;

    private boolean renovado;

    @OneToMany(mappedBy = "emprestimo", cascade = CascadeType.ALL)
    private List<ItemEmprestimo> itens = new ArrayList<>();

    @OneToOne(mappedBy = "emprestimo", cascade = CascadeType.ALL)
    private Multa multa;

    public void adicionarItem(ItemEmprestimo item) {
        itens.add(item);
        item.setEmprestimo(this);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Date getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(Date dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public Date getDataPrevistaDevolucao() {
        return dataPrevistaDevolucao;
    }

    public void setDataPrevistaDevolucao(Date dataPrevistaDevolucao) {
        this.dataPrevistaDevolucao = dataPrevistaDevolucao;
    }

    public Date getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(Date dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    public boolean isRenovado() {
        return renovado;
    }

    public void setRenovado(boolean renovado) {
        this.renovado = renovado;
    }

    public List<ItemEmprestimo> getItens() {
        return itens;
    }

    public void setItens(List<ItemEmprestimo> itens) {
        this.itens = itens;
    }

    public Multa getMulta() {
        return multa;
    }

    public void setMulta(Multa multa) {
        this.multa = multa;
    }
}
