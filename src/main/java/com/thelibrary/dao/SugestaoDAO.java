package com.thelibrary.dao;

import com.thelibrary.model.Sugestao;
import com.thelibrary.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class SugestaoDAO extends GenericDAO<Sugestao> {

    public SugestaoDAO() {
        super(Sugestao.class);
    }

    // Buscar sugestões por usuário
    public List<Sugestao> buscarPorUsuario(Integer usuarioId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Sugestao> query = em.createQuery(
                    "SELECT s FROM Sugestao s WHERE s.usuario.id = :usuarioId ORDER BY s.dataSugestao DESC",
                    Sugestao.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Buscar todas as sugestões ordenadas por data
    public List<Sugestao> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Sugestao> query = em.createQuery(
                    "SELECT s FROM Sugestao s ORDER BY s.dataSugestao DESC",
                    Sugestao.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}