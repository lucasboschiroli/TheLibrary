package com.thelibrary.service;

import com.thelibrary.dao.ArtigoDAO;
import com.thelibrary.model.Artigo;

import java.util.List;

public class ArtigoService {

    private final ArtigoDAO artigoDAO;

    public ArtigoService() {
        this.artigoDAO = new ArtigoDAO();
    }

    // CREATE
    public Artigo cadastrarArtigo(String titulo, String autor, String status, String revista, String revisor, java.util.Date dataRevisao) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("Autor é obrigatório");
        }
        if (revista == null || revista.trim().isEmpty()) {
            throw new IllegalArgumentException("Revista é obrigatória");
        }
        if (revisor == null || revisor.trim().isEmpty()) {
            throw new IllegalArgumentException("Revisor é obrigatório");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status é obrigatório");
        }

        Artigo artigo = new Artigo(titulo, autor, status, revista, revisor, dataRevisao);
        return artigoDAO.save(artigo);
    }

    // READ
    public Artigo buscarPorId(int id) {
        Artigo artigo = artigoDAO.findById(id);
        if (artigo == null) {
            throw new IllegalArgumentException("Artigo não encontrado");
        }
        return artigo;
    }

    public List<Artigo> listarTodos() {
        return artigoDAO.findAll();
    }

    public List<Artigo> buscarPorRevista(String revista) {
        if (revista == null || revista.trim().isEmpty()) {
            throw new IllegalArgumentException("Revista não pode ser vazia");
        }
        return artigoDAO.buscarPorRevista(revista);
    }

    public List<Artigo> buscarPorRevisor(String revisor) {
        if (revisor == null || revisor.trim().isEmpty()) {
            throw new IllegalArgumentException("Revisor não pode ser vazio");
        }
        return artigoDAO.buscarPorRevisor(revisor);
    }

    // UPDATE
    public Artigo atualizarArtigo(int id, String titulo, String autor, String status, String revista, String revisor, java.util.Date dataRevisao) {
        Artigo artigo = artigoDAO.findById(id);
        if (artigo == null) {
            throw new IllegalArgumentException("Artigo não encontrado");
        }

        if (titulo != null && !titulo.trim().isEmpty()) {
            artigo.setTitulo(titulo);
        }
        if (autor != null && !autor.trim().isEmpty()) {
            artigo.setAutor(autor);
        }
        if (status != null && !status.trim().isEmpty()) {
            artigo.setStatus(status);
        }
        if (revista != null && !revista.trim().isEmpty()) {
            artigo.setRevista(revista);
        }
        if (revisor != null && !revisor.trim().isEmpty()) {
            artigo.setRevisor(revisor);
        }
        if (dataRevisao != null) {
            artigo.setDataDeRevisao(dataRevisao);
        }

        return artigoDAO.update(artigo);
    }

    // DELETE
    public void excluirArtigo(int id) {
        Artigo artigo = artigoDAO.findById(id);
        if (artigo == null) {
            throw new IllegalArgumentException("Artigo não encontrado");
        }
        artigoDAO.delete(id);
    }
}