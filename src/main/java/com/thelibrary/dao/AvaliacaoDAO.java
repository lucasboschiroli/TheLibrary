package com.thelibrary.dao;

import com.thelibrary.model.Avaliacao;
import com.thelibrary.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class AvaliacaoDAO extends GenericDAO<Avaliacao> {

    public AvaliacaoDAO() {
        super(Avaliacao.class);
    }

    // Buscar avaliações por usuário
    public List<Avaliacao> buscarPorUsuario(Integer usuarioId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Avaliacao> query = em.createQuery(
                    "SELECT a FROM Avaliacao a WHERE a.usuario.id = :usuarioId ORDER BY a.dataAvaliacao DESC",
                    Avaliacao.class);
            query.setParameter("usuarioId", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Buscar avaliações por material
    public List<Avaliacao> buscarPorMaterial(Integer materialId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Avaliacao> query = em.createQuery(
                    "SELECT a FROM Avaliacao a WHERE a.material.id = :materialId ORDER BY a.dataAvaliacao DESC",
                    Avaliacao.class);
            query.setParameter("materialId", materialId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    // Buscar avaliação específica de um usuário para um material
    public Avaliacao buscarPorUsuarioEMaterial(Integer usuarioId, Integer materialId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Avaliacao> query = em.createQuery(
                    "SELECT a FROM Avaliacao a WHERE a.usuario.id = :usuarioId AND a.material.id = :materialId",
                    Avaliacao.class);
            query.setParameter("usuarioId", usuarioId);
            query.setParameter("materialId", materialId);
            List<Avaliacao> resultados = query.getResultList();
            return resultados.isEmpty() ? null : resultados.get(0);
        } finally {
            em.close();
        }
    }

    // Calcular média de notas de um material
    public Double calcularMediaNotas(Integer materialId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Double> query = em.createQuery(
                    "SELECT AVG(a.nota) FROM Avaliacao a WHERE a.material.id = :materialId",
                    Double.class);
            query.setParameter("materialId", materialId);
            Double media = query.getSingleResult();
            return media != null ? Math.round(media * 10.0) / 10.0 : 0.0; // Arredonda para 1 casa decimal
        } finally {
            em.close();
        }
    }

    // Contar número de avaliações de um material
    public Long contarAvaliacoesPorMaterial(Integer materialId) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Long> query = em.createQuery(
                    "SELECT COUNT(a) FROM Avaliacao a WHERE a.material.id = :materialId",
                    Long.class);
            query.setParameter("materialId", materialId);
            return query.getSingleResult();
        } finally {
            em.close();
        }
    }
}