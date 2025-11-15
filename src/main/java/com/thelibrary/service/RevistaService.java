package com.thelibrary.service;

import com.thelibrary.dao.RevistaDAO;
import com.thelibrary.model.Revista;

import java.util.List;

public class RevistaService {

    private final RevistaDAO revistaDAO;

    public RevistaService() {
        this.revistaDAO = new RevistaDAO();
    }

    // CREATE
    public Revista cadastrarRevista(String titulo, String autor, String status, String nomeRevista, String numeroEdicao, java.util.Date dataEdicao) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("Autor é obrigatório");
        }
        if (nomeRevista == null || nomeRevista.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da revista é obrigatório");
        }
        if (numeroEdicao == null || numeroEdicao.trim().isEmpty()) {
            throw new IllegalArgumentException("Número da edição é obrigatório");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status é obrigatório");
        }

        Revista revista = new Revista(titulo, autor, status, nomeRevista, numeroEdicao, dataEdicao);
        return revistaDAO.save(revista);
    }

    // READ
    public Revista buscarPorId(int id) {
        Revista revista = revistaDAO.findById(id);
        if (revista == null) {
            throw new IllegalArgumentException("Revista não encontrada");
        }
        return revista;
    }

    public List<Revista> listarTodas() {
        return revistaDAO.findAll();
    }

    public List<Revista> buscarPorNumeroEdicao(String numeroEdicao) {
        if (numeroEdicao == null || numeroEdicao.trim().isEmpty()) {
            throw new IllegalArgumentException("Número de edição não pode ser vazio");
        }
        return revistaDAO.buscarPorNumeroEdicao(numeroEdicao);
    }

    public List<Revista> buscarPorNomeRevista(String nomeRevista) {
        if (nomeRevista == null || nomeRevista.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da revista não pode ser vazio");
        }
        return revistaDAO.buscarPorNomeRevista(nomeRevista);
    }

    // UPDATE
    public Revista atualizarRevista(int id, String titulo, String autor, String status, String nomeRevista, String numeroEdicao, java.util.Date dataEdicao) {
        Revista revista = revistaDAO.findById(id);
        if (revista == null) {
            throw new IllegalArgumentException("Revista não encontrada");
        }

        if (titulo != null && !titulo.trim().isEmpty()) {
            revista.setTitulo(titulo);
        }
        if (autor != null && !autor.trim().isEmpty()) {
            revista.setAutor(autor);
        }
        if (status != null && !status.trim().isEmpty()) {
            revista.setStatus(status);
        }
        if (nomeRevista != null && !nomeRevista.trim().isEmpty()) {
            revista.setRevista(nomeRevista);
        }
        if (numeroEdicao != null && !numeroEdicao.trim().isEmpty()) {
            revista.setNumeroEdicao(numeroEdicao);
        }
        if (dataEdicao != null) {
            revista.setDataDeEdicao(dataEdicao);
        }

        return revistaDAO.update(revista);
    }

    // DELETE
    public void excluirRevista(int id) {
        Revista revista = revistaDAO.findById(id);
        if (revista == null) {
            throw new IllegalArgumentException("Revista não encontrada");
        }
        revistaDAO.delete(id);
    }
}