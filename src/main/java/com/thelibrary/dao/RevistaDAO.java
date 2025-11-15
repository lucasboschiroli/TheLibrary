package com.thelibrary.dao;

import com.thelibrary.model.Revista;
import com.thelibrary.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class RevistaDAO extends GenericDAO<Revista> {

    public RevistaDAO() {
        super(Revista.class);
    }

    public List<Revista> buscarPorNumeroEdicao(String numeroEdicao) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Revista> query = em.createQuery(
                    "SELECT r FROM Revista r WHERE r.numeroEdicao = :numero", Revista.class);
            query.setParameter("numero", numeroEdicao);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Revista> buscarPorNomeRevista(String nomeRevista) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Revista> query = em.createQuery(
                    "SELECT r FROM Revista r WHERE r.revista LIKE :nome", Revista.class);
            query.setParameter("nome", "%" + nomeRevista + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}