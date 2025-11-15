package com.thelibrary.dao;

import com.thelibrary.model.Bibliotecario;
import com.thelibrary.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class BibliotecarioDAO extends GenericDAO<Bibliotecario> {

    public BibliotecarioDAO() {
        super(Bibliotecario.class);
    }

    // Método específico: buscar bibliotecário por email
    public Bibliotecario findByEmail(String email) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT b FROM Bibliotecario b WHERE b.email = :email";
            TypedQuery<Bibliotecario> query = em.createQuery(jpql, Bibliotecario.class);
            query.setParameter("email", email);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }

    // Método específico: verificar login
    public Bibliotecario login(String email, String senha) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            String jpql = "SELECT b FROM Bibliotecario b WHERE b.email = :email AND b.senha = :senha";
            TypedQuery<Bibliotecario> query = em.createQuery(jpql, Bibliotecario.class);
            query.setParameter("email", email);
            query.setParameter("senha", senha);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        } finally {
            em.close();
        }
    }
}