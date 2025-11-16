package com.thelibrary.service;

import com.thelibrary.model.*;
import com.thelibrary.util.JPAUtil;

import javax.persistence.EntityManager;
import java.util.*;

public class EmprestimoService {

    // ----------------------------------------------------------------------
    // REGISTRAR EMPRÉSTIMO
    // ----------------------------------------------------------------------
    public Emprestimo registrarEmprestimo(Long usuarioIdLong, List<Long> materiaisIdsLong) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        // Converter Long → Integer
        Integer usuarioId = usuarioIdLong.intValue();

        Usuario usuario = em.find(Usuario.class, usuarioId);
        if (usuario == null)
            throw new RuntimeException("Usuário não encontrado.");

        Emprestimo emp = new Emprestimo();
        emp.setUsuario(usuario);
        emp.setDataEmprestimo(new Date());

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        emp.setDataPrevistaDevolucao(cal.getTime());

        for (Long idMatLong : materiaisIdsLong) {

            Integer idMat = idMatLong.intValue(); // 🔥 CONVERSÃO AQUI

            MaterialBibliografico m = em.find(MaterialBibliografico.class, idMat);
            if (m == null)
                throw new RuntimeException("Material não encontrado: " + idMat);

            ItemEmprestimo item = new ItemEmprestimo();
            item.setMaterial(m);
            emp.adicionarItem(item);
        }

        em.persist(emp);
        em.getTransaction().commit();
        em.close();

        return emp;
    }

    // ----------------------------------------------------------------------
    // LISTAR TODOS OS EMPRÉSTIMOS
    // ----------------------------------------------------------------------
    public List<Emprestimo> listar() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            "SELECT e FROM Emprestimo e", Emprestimo.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    // ----------------------------------------------------------------------
    // BUSCAR POR ID
    // ----------------------------------------------------------------------
    public Emprestimo buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Emprestimo.class, id);
        } finally {
            em.close();
        }
    }

    // ----------------------------------------------------------------------
    // REGISTRAR DEVOLUÇÃO
    // ----------------------------------------------------------------------
    public void registrarDevolucao(Long id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            Emprestimo emp = em.find(Emprestimo.class, id);
            if (emp == null)
                throw new RuntimeException("Empréstimo não encontrado.");

            emp.setDataDevolucao(new Date());

            // Verifica atraso
            if (emp.getDataDevolucao().after(emp.getDataPrevistaDevolucao())) {
                Multa multa = new Multa();
                multa.setValor(10.0);
                multa.setPaga(false);
                multa.setEmprestimo(emp);

                emp.setMulta(multa);
                em.persist(multa);
            }

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    // ----------------------------------------------------------------------
    // RENOVAR EMPRÉSTIMO
    // ----------------------------------------------------------------------
    public void renovar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            Emprestimo emp = em.find(Emprestimo.class, id);
            if (emp == null)
                throw new RuntimeException("Empréstimo não encontrado.");

            if (emp.isRenovado())
                throw new RuntimeException("Esse empréstimo já foi renovado!");

            Calendar cal = Calendar.getInstance();
            cal.setTime(emp.getDataPrevistaDevolucao());
            cal.add(Calendar.DAY_OF_MONTH, 7);

            emp.setDataPrevistaDevolucao(cal.getTime());
            emp.setRenovado(true);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    // ----------------------------------------------------------------------
    // EXCLUIR EMPRÉSTIMO
    // ----------------------------------------------------------------------
    public void excluir(Long id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            em.getTransaction().begin();

            Emprestimo emp = em.find(Emprestimo.class, id);
            if (emp != null)
                em.remove(emp);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    public List<Emprestimo> listarPorUsuario(int usuarioId) {
        EntityManager em = JPAUtil.getEntityManager();

        List<Emprestimo> lista = em.createQuery(
                        "SELECT e FROM Emprestimo e WHERE e.usuario.id = :id",
                        Emprestimo.class
                )
                .setParameter("id", usuarioId)
                .getResultList();

        em.close();
        return lista;
    }

    public void renovarDoUsuario(int emprestimoId, int usuarioId) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        Emprestimo emp = em.find(Emprestimo.class, emprestimoId);

        if (emp == null)
            throw new RuntimeException("Empréstimo não encontrado.");

        if (emp.getUsuario().getId() != usuarioId)
            throw new RuntimeException("Você não pode renovar empréstimos de outro usuário!");

        if (emp.isRenovado())
            throw new RuntimeException("Este empréstimo já foi renovado!");

        Calendar cal = Calendar.getInstance();
        cal.setTime(emp.getDataPrevistaDevolucao());
        cal.add(Calendar.DAY_OF_MONTH, 7);
        emp.setDataPrevistaDevolucao(cal.getTime());

        emp.setRenovado(true);

        em.getTransaction().commit();
        em.close();
    }

    public void devolverDoUsuario(int emprestimoId, int usuarioId) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        Emprestimo emp = em.find(Emprestimo.class, emprestimoId);

        if (emp == null)
            throw new RuntimeException("Empréstimo não encontrado.");

        if (emp.getUsuario().getId() != usuarioId)
            throw new RuntimeException("Você não pode devolver empréstimos de outro usuário!");

        emp.setDataDevolucao(new Date());

        // multa por atraso
        if (emp.getDataDevolucao().after(emp.getDataPrevistaDevolucao())) {
            Multa multa = new Multa();
            multa.setEmprestimo(emp);
            multa.setPaga(false);
            multa.setValor(10.0);
            em.persist(multa);
            emp.setMulta(multa);
        }

        em.getTransaction().commit();
        em.close();
    }

}
