package com.thelibrary.service;

import com.thelibrary.dao.SugestaoDAO;
import com.thelibrary.model.Sugestao;
import com.thelibrary.model.Usuario;
import com.thelibrary.util.JPAUtil;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

public class SugestaoService {

    private final SugestaoDAO sugestaoDAO;

    public SugestaoService() {
        this.sugestaoDAO = new SugestaoDAO();
    }

    // CREATE - Sugerir Livro
    public Sugestao sugerirLivro(Integer usuarioId, String justificativa,
                                 String titulo, String autor, String editora, Date dataPublicacao) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Usuario usuario = em.find(Usuario.class, usuarioId);
            if (usuario == null) {
                throw new IllegalArgumentException("Usuário não encontrado");
            }

            Sugestao sugestao = Sugestao.criarSugestaoLivro(usuario, justificativa, titulo, autor, editora, dataPublicacao);
            em.persist(sugestao);

            em.getTransaction().commit();
            return sugestao;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao criar sugestão: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // CREATE - Sugerir Artigo
    public Sugestao sugerirArtigo(Integer usuarioId, String justificativa,
                                  String titulo, String autor, String revista, String revisor, Date dataRevisao) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Usuario usuario = em.find(Usuario.class, usuarioId);
            if (usuario == null) {
                throw new IllegalArgumentException("Usuário não encontrado");
            }

            Sugestao sugestao = Sugestao.criarSugestaoArtigo(usuario, justificativa, titulo, autor, revista, revisor, dataRevisao);
            em.persist(sugestao);

            em.getTransaction().commit();
            return sugestao;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao criar sugestão: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // CREATE - Sugerir Revista
    public Sugestao sugerirRevista(Integer usuarioId, String justificativa,
                                   String titulo, String autor, String nomeRevista, String numeroEdicao, Date dataEdicao) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Usuario usuario = em.find(Usuario.class, usuarioId);
            if (usuario == null) {
                throw new IllegalArgumentException("Usuário não encontrado");
            }

            Sugestao sugestao = Sugestao.criarSugestaoRevista(usuario, justificativa, titulo, autor, nomeRevista, numeroEdicao, dataEdicao);
            em.persist(sugestao);

            em.getTransaction().commit();
            return sugestao;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao criar sugestão: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // READ - Buscar por ID
    public Sugestao buscarPorId(int id) {
        Sugestao sugestao = sugestaoDAO.findById(id);
        if (sugestao == null) {
            throw new IllegalArgumentException("Sugestão não encontrada");
        }
        return sugestao;
    }

    // READ - Listar todas
    public List<Sugestao> listarTodas() {
        return sugestaoDAO.findAll();
    }

    // READ - Listar por usuário
    public List<Sugestao> listarPorUsuario(Integer usuarioId) {
        return sugestaoDAO.buscarPorUsuario(usuarioId);
    }

    // UPDATE - Atualizar justificativa
    public Sugestao atualizarJustificativa(int id, Integer usuarioId, String novaJustificativa) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Sugestao sugestao = em.find(Sugestao.class, id);
            if (sugestao == null) {
                throw new IllegalArgumentException("Sugestão não encontrada");
            }

            // Verifica se é o próprio usuário
            if (!sugestao.getUsuario().getId().equals(usuarioId)) {
                throw new IllegalArgumentException("Você só pode editar suas próprias sugestões");
            }

            sugestao.setJustificativa(novaJustificativa);
            em.merge(sugestao);

            em.getTransaction().commit();
            return sugestao;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao atualizar justificativa: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // DELETE - Excluir sugestão
    public void excluirSugestao(int id) {
        Sugestao sugestao = sugestaoDAO.findById(id);
        if (sugestao == null) {
            throw new IllegalArgumentException("Sugestão não encontrada");
        }
        sugestaoDAO.delete(id);
    }

    // DELETE - Usuário exclui sua própria sugestão
    public void excluirSugestaoDoUsuario(int id, Integer usuarioId) {
        Sugestao sugestao = sugestaoDAO.findById(id);
        if (sugestao == null) {
            throw new IllegalArgumentException("Sugestão não encontrada");
        }

        if (!sugestao.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Você só pode excluir suas próprias sugestões");
        }

        sugestaoDAO.delete(id);
    }
}