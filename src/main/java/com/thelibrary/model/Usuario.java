package com.thelibrary.model;

import javax.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario extends Pessoa {

    @Column(name = "qtd_de_emprestimos")
    private int qtdDeEmprestimos;

    public Usuario() {
        super();
        this.qtdDeEmprestimos = 0;
    }

    public Usuario(String email, String senha, String nome, String telefone) {
        super(email, senha, nome, telefone);
        this.qtdDeEmprestimos = 0;
    }

    public int getQtdDeEmprestimos() { return qtdDeEmprestimos; }
    public void setQtdDeEmprestimos(int qtdDeEmprestimos) { this.qtdDeEmprestimos = qtdDeEmprestimos; }

    public void incrementarEmprestimos() { this.qtdDeEmprestimos++; }
    public void decrementarEmprestimos() {
        if (this.qtdDeEmprestimos > 0) this.qtdDeEmprestimos--;
    }

    @Override
    public String toString() {
        return "Usuario{id=" + getId() + ", nome='" + getNome() + "', qtdEmprestimos=" + qtdDeEmprestimos + "}";
    }
}