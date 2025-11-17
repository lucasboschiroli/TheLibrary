package com.thelibrary.service;

import com.thelibrary.dao.AvaliacaoDAO;
import com.thelibrary.model.Avaliacao;
import com.thelibrary.model.MaterialBibliografico;
import com.thelibrary.model.Usuario;
import com.thelibrary.util.JPAUtil;

import javax.persistence.EntityManager;
import java.util.List;

public class AvaliacaoService {

    private final AvaliacaoDAO avaliacaoDAO;

    public AvaliacaoService() {
        this.avaliacaoDAO = new AvaliacaoDAO();
    }

    // CREATE - Criar avaliação (SEM COMENTÁRIO)
    public Avaliacao criarAvaliacao(Integer usuarioId, Integer materialId, Integer nota) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Usuario usuario = em.find(Usuario.class, usuarioId);
            if (usuario == null) {
                throw new IllegalArgumentException("Usuário não encontrado");
            }

            MaterialBibliografico material = em.find(MaterialBibliografico.class, materialId);
            if (material == null) {
                throw new IllegalArgumentException("Material não encontrado");
            }

            if (nota < 1 || nota > 5) {
                throw new IllegalArgumentException("A nota deve ser entre 1 e 5");
            }

            Avaliacao avaliacaoExistente = avaliacaoDAO.buscarPorUsuarioEMaterial(usuarioId, materialId);
            if (avaliacaoExistente != null) {
                throw new IllegalArgumentException("Você já avaliou este material");
            }

            // SEM COMENTÁRIO
            Avaliacao avaliacao = new Avaliacao(usuario, material, nota);
            em.persist(avaliacao);

            em.getTransaction().commit();
            return avaliacao;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao criar avaliação: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // READ - Buscar por ID
    public Avaliacao buscarPorId(int id) {
        Avaliacao avaliacao = avaliacaoDAO.findById(id);
        if (avaliacao == null) {
            throw new IllegalArgumentException("Avaliação não encontrada");
        }
        return avaliacao;
    }

    // READ - Listar todas
    public List<Avaliacao> listarTodas() {
        return avaliacaoDAO.findAll();
    }

    // READ - Listar por usuário
    public List<Avaliacao> listarPorUsuario(Integer usuarioId) {
        return avaliacaoDAO.buscarPorUsuario(usuarioId);
    }

    // READ - Listar por material
    public List<Avaliacao> listarPorMaterial(Integer materialId) {
        return avaliacaoDAO.buscarPorMaterial(materialId);
    }

    // UPDATE - Atualizar avaliação (APENAS NOTA)
    public Avaliacao atualizarAvaliacao(int id, Integer usuarioId, Integer novaNota) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();

            Avaliacao avaliacao = em.find(Avaliacao.class, id);
            if (avaliacao == null) {
                throw new IllegalArgumentException("Avaliação não encontrada");
            }

            if (!avaliacao.getUsuario().getId().equals(usuarioId)) {
                throw new IllegalArgumentException("Você só pode editar suas próprias avaliações");
            }

            if (novaNota < 1 || novaNota > 5) {
                throw new IllegalArgumentException("A nota deve ser entre 1 e 5");
            }

            // ATUALIZA APENAS A NOTA
            avaliacao.setNota(novaNota);

            em.merge(avaliacao);
            em.getTransaction().commit();
            return avaliacao;
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new RuntimeException("Erro ao atualizar avaliação: " + e.getMessage(), e);
        } finally {
            em.close();
        }
    }

    // DELETE - Excluir avaliação
    public void excluirAvaliacao(int id) {
        Avaliacao avaliacao = avaliacaoDAO.findById(id);
        if (avaliacao == null) {
            throw new IllegalArgumentException("Avaliação não encontrada");
        }
        avaliacaoDAO.delete(id);
    }

    // DELETE - Usuário exclui sua própria avaliação
    public void excluirAvaliacaoDoUsuario(int id, Integer usuarioId) {
        Avaliacao avaliacao = avaliacaoDAO.findById(id);
        if (avaliacao == null) {
            throw new IllegalArgumentException("Avaliação não encontrada");
        }

        if (!avaliacao.getUsuario().getId().equals(usuarioId)) {
            throw new IllegalArgumentException("Você só pode excluir suas próprias avaliações");
        }

        avaliacaoDAO.delete(id);
    }

    // Estatísticas do material
    public Double obterMediaNotasMaterial(Integer materialId) {
        return avaliacaoDAO.calcularMediaNotas(materialId);
    }

    public Long obterNumeroAvaliacoesMaterial(Integer materialId) {
        return avaliacaoDAO.contarAvaliacoesPorMaterial(materialId);
    }

    // Verificar se usuário já avaliou o material
    public boolean usuarioJaAvaliouMaterial(Integer usuarioId, Integer materialId) {
        return avaliacaoDAO.buscarPorUsuarioEMaterial(usuarioId, materialId) != null;
    }
}