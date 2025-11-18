package com.thelibrary.dao;

import com.thelibrary.model.Comentario;
import com.thelibrary.model.Multa;
import com.thelibrary.model.Usuario;
import com.thelibrary.util.JPAUtil;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ComentarioDAO extends GenericDAO<Comentario>{
    public ComentarioDAO(){
        super(Comentario.class);
    }

    public List<Comentario> buscarPorUsuario(Usuario usuario){
        int id = usuario.getId();
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Comentario> query = em.createQuery(
                    "SELECT c FROM Comentario c WHERE c.usuario.id = :usuarioId", Comentario.class);
            query.setParameter("usuarioId", id);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Comentario> buscarPorMaterialBibliografico(int id){
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Comentario> query = em.createQuery(
                    "SELECT c FROM Comentario c WHERE c.materialBibliografico.id = :materialBibliograficoId", Comentario.class);
            query.setParameter("materialBibliograficoId", id);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public List<Comentario> buscarPorMaterialBibliograficoEUsuario(int materialid, int usuarioId){
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Comentario> query = em.createQuery(
                    "SELECT c FROM Comentario c WHERE c.materialBibliografico.id = :materialBibliograficoId AND c.usuario.id = :usuarioId", Comentario.class);
            query.setParameter("materialBibliograficoId", materialid);
            query.setParameter("usuarioId", usuarioId);


            return query.getResultList();
        } finally {
            em.close();
        }
    }
}
