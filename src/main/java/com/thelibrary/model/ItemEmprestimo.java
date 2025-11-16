package com.thelibrary.model;

import javax.persistence.*;

@Entity
public class ItemEmprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Emprestimo emprestimo;

    @ManyToOne
    private MaterialBibliografico material;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Emprestimo getEmprestimo() {
        return emprestimo;
    }

    public void setEmprestimo(Emprestimo emprestimo) {
        this.emprestimo = emprestimo;
    }

    public MaterialBibliografico getMaterial() {
        return material;
    }

    public void setMaterial(MaterialBibliografico material) {
        this.material = material;
    }
}
