package com.thelibrary;

import com.thelibrary.model.Usuario;
import com.thelibrary.model.Bibliotecario;
import com.thelibrary.util.JPAUtil;

import javax.persistence.EntityManager;

public class Main {

    public static void main(String[] args) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            System.out.println("=== TESTE THE LIBRARY ===\n");

            em.getTransaction().begin();

            Usuario usuario = new Usuario("teste@email.com", "123", "João", "(11) 99999-9999");
            Bibliotecario biblio = new Bibliotecario("biblio@email.com", "456", "Maria", "(11) 88888-8888");

            em.persist(usuario);
            em.persist(biblio);

            em.getTransaction().commit();

            System.out.println("✓ Cadastrado: " + usuario);
            System.out.println("✓ Cadastrado: " + biblio);
            System.out.println("\n=== SUCESSO! ===");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
            JPAUtil.close();
        }
    }
}