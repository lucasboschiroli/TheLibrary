package com.thelibrary.service;

import com.thelibrary.dao.BibliotecarioDAO;
import com.thelibrary.model.Bibliotecario;

import java.util.List;

public class BibliotecarioService {

    private final BibliotecarioDAO bibliotecarioDAO;

    public BibliotecarioService() {
        this.bibliotecarioDAO = new BibliotecarioDAO();
    }

    // CREATE
    public Bibliotecario cadastrarBibliotecario(String email, String senha, String nome, String telefone) {
        // Validações de negócio
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        if (senha == null || senha.length() < 3) {
            throw new IllegalArgumentException("Senha deve ter pelo menos 3 caracteres");
        }
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome é obrigatório");
        }

        // Verificar se email já existe
        if (bibliotecarioDAO.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Bibliotecario bibliotecario = new Bibliotecario(email, senha, nome, telefone);
        return bibliotecarioDAO.save(bibliotecario);
    }

    // READ
    public Bibliotecario buscarPorId(int id) {
        Bibliotecario bibliotecario = bibliotecarioDAO.findById(id);
        if (bibliotecario == null) {
            throw new IllegalArgumentException("Bibliotecário não encontrado");
        }
        return bibliotecario;
    }

    public List<Bibliotecario> listarTodos() {
        return bibliotecarioDAO.findAll();
    }

    public Bibliotecario buscarPorEmail(String email) {
        return bibliotecarioDAO.findByEmail(email);
    }

    // UPDATE
    public Bibliotecario atualizarBibliotecario(int id, String email, String senha, String nome, String telefone) {
        Bibliotecario bibliotecario = bibliotecarioDAO.findById(id);
        if (bibliotecario == null) {
            throw new IllegalArgumentException("Bibliotecário não encontrado");
        }

        // Validações
        if (email != null && !email.trim().isEmpty()) {
            bibliotecario.setEmail(email);
        }
        if (senha != null && senha.length() >= 3) {
            bibliotecario.setSenha(senha);
        }
        if (nome != null && !nome.trim().isEmpty()) {
            bibliotecario.setNome(nome);
        }
        if (telefone != null) {
            bibliotecario.setTelefone(telefone);
        }

        return bibliotecarioDAO.update(bibliotecario);
    }

    // DELETE
    public void excluirBibliotecario(int id) {
        Bibliotecario bibliotecario = bibliotecarioDAO.findById(id);
        if (bibliotecario == null) {
            throw new IllegalArgumentException("Bibliotecário não encontrado");
        }
        bibliotecarioDAO.delete(id);
    }

    // Lógica de negócio específica
    public Bibliotecario realizarLogin(String email, String senha) {
        Bibliotecario bibliotecario = bibliotecarioDAO.login(email, senha);
        if (bibliotecario == null) {
            throw new IllegalArgumentException("Email ou senha inválidos");
        }
        return bibliotecario;
    }
}