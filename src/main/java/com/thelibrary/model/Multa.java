package com.thelibrary.model;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Multa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private double valor;
    private boolean paga;
    private String justificativa;

    @Temporal(TemporalType.DATE)
    private Date data_emissao;

    @OneToOne
    @JoinColumn(name = "emprestimo_id", unique = true, nullable = false)
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

    public String getJustificativa(){return justificativa;}

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public Date getData_emissao() {
        return data_emissao;
    }

    public void setData_emissao(Date data_emissao) {
        this.data_emissao = data_emissao;
    }

    @Override
    public String toString() {
        return "Multa{id=" + id + ", valor='" + valor + "', justificativa='" + justificativa + "', situação='" + paga + "', data de emissão='" + data_emissao + "',id do emprestimo='" + emprestimo.getId() +"'}";
    }
}
