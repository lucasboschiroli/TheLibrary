package com.thelibrary.dao;

import com.thelibrary.model.Livro;
import com.thelibrary.model.Multa;
import com.thelibrary.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class MultaDAO extends GenericDAO<Multa>{
    public MultaDAO(){
        super(Multa.class);
    }

    public List<Multa> buscarPorIdEmprestimo(int id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Multa> query = em.createQuery(
                    "SELECT m FROM Multa m WHERE m.emprestimo.id = :emprestimo", Multa.class);
            query.setParameter("emprestimo", id);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
