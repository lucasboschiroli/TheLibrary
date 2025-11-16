package com.thelibrary.model;

import javax.persistence.*;

@Entity
public class Multa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double valor;
    private boolean paga;

    @OneToOne
    private Emprestimo emprestimo;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public boolean isPaga() {
        return paga;
    }

    public void setPaga(boolean paga) {
        this.paga = paga;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }
}
