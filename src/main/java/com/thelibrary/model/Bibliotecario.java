package com.thelibrary.model;

import javax.persistence.*;

@Entity
@Table(name = "bibliotecario")
public class Bibliotecario extends Pessoa {

    public Bibliotecario() {
        super();
    }

    public Bibliotecario(String email, String senha, String nome, String telefone) {
        super(email, senha, nome, telefone);
    }

    @Override
    public String toString() {
        return "Bibliotecario{id=" + getId() + ", nome='" + getNome() + "'}";
    }
}