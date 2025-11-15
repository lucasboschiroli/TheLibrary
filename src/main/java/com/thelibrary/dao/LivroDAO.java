package com.thelibrary.dao;

import com.thelibrary.model.Livro;
import com.thelibrary.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class LivroDAO extends GenericDAO<Livro> {

    public LivroDAO() {
        super(Livro.class);
    }

    public List<Livro> buscarPorEditora(String editora) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Livro> query = em.createQuery(
                    "SELECT l FROM Livro l WHERE l.editora LIKE :editora", Livro.class);
            query.setParameter("editora", "%" + editora + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Livro> buscarPorAutor(String autor) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Livro> query = em.createQuery(
                    "SELECT l FROM Livro l WHERE l.autor LIKE :autor", Livro.class);
            query.setParameter("autor", "%" + autor + "%");
            return query.getResultList();
        } finally {
            em.close();
        }
    }
}