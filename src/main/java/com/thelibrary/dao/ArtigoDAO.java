package com.thelibrary.dao;

import com.thelibrary.model.Artigo;
import com.thelibrary.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ArtigoDAO extends GenericDAO<Artigo> {

    public ArtigoDAO() {
        super(Artigo.class);
    }

    public List<Artigo> buscarPorRevista(String revista) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Artigo> query = em.createQuery(
                    "SELECT a FROM Artigo a WHERE a.revista LIKE :revista", Artigo.class);
            query.setParameter("revista", "%" + revista + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Artigo> buscarPorRevisor(String revisor) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Artigo> query = em.createQuery(
                    "SELECT a FROM Artigo a WHERE a.revisor LIKE :revisor", Artigo.class);
            query.setParameter("revisor", "%" + revisor + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}