package com.thelibrary.service;

import com.thelibrary.dao.LivroDAO;
import com.thelibrary.model.Livro;

import java.util.List;

public class LivroService {

    private final LivroDAO livroDAO;

    public LivroService() {
        this.livroDAO = new LivroDAO();
    }

    // CREATE
    public Livro cadastrarLivro(String titulo, String autor, String status, String editora, java.util.Date dataPublicacao) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("Título é obrigatório");
        }
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("Autor é obrigatório");
        }
        if (editora == null || editora.trim().isEmpty()) {
            throw new IllegalArgumentException("Editora é obrigatória");
        }
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status é obrigatório");
        }

        Livro livro = new Livro(titulo, autor, status, editora, dataPublicacao);
        return livroDAO.save(livro);
    }

    // READ
    public Livro buscarPorId(int id) {
        Livro livro = livroDAO.findById(id);
        if (livro == null) {
            throw new IllegalArgumentException("Livro não encontrado");
        }
        return livro;
    }

    public List<Livro> listarTodos() {
        return livroDAO.findAll();
    }

    public List<Livro> buscarPorEditora(String editora) {
        if (editora == null || editora.trim().isEmpty()) {
            throw new IllegalArgumentException("Editora não pode ser vazia");
        }
        return livroDAO.buscarPorEditora(editora);
    }

    public List<Livro> buscarPorAutor(String autor) {
        if (autor == null || autor.trim().isEmpty()) {
            throw new IllegalArgumentException("Autor não pode ser vazio");
        }
        return livroDAO.buscarPorAutor(autor);
    }

    // UPDATE
    public Livro atualizarLivro(int id, String titulo, String autor, String status, String editora, java.util.Date dataPublicacao) {
        Livro livro = livroDAO.findById(id);
        if (livro == null) {
            throw new IllegalArgumentException("Livro não encontrado");
        }

        if (titulo != null && !titulo.trim().isEmpty()) {
            livro.setTitulo(titulo);
        }
        if (autor != null && !autor.trim().isEmpty()) {
            livro.setAutor(autor);
        }
        if (status != null && !status.trim().isEmpty()) {
            livro.setStatus(status);
        }
        if (editora != null && !editora.trim().isEmpty()) {
            livro.setEditora(editora);
        }
        if (dataPublicacao != null) {
            livro.setDataDePublicacao(dataPublicacao);
        }

        return livroDAO.update(livro);
    }

    // DELETE
    public void excluirLivro(int id) {
        Livro livro = livroDAO.findById(id);
        if (livro == null) {
            throw new IllegalArgumentException("Livro não encontrado");
        }
        livroDAO.delete(id);
    }
}