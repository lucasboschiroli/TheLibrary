package com.thelibrary.service;

import com.thelibrary.dao.UsuarioDAO;
import com.thelibrary.model.Usuario;

import java.util.List;

public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    public UsuarioService() {
        this.usuarioDAO = new UsuarioDAO();
    }

    // CREATE
    public Usuario cadastrarUsuario(String email, String senha, String nome, String telefone) {
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
        if (usuarioDAO.findByEmail(email) != null) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Usuario usuario = new Usuario(email, senha, nome, telefone);
        return usuarioDAO.save(usuario);
    }

    // READ
    public Usuario buscarPorId(int id) {
        Usuario usuario = usuarioDAO.findById(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        return usuario;
    }

    public List<Usuario> listarTodos() {
        return usuarioDAO.findAll();
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioDAO.findByEmail(email);
    }

    // UPDATE
    public Usuario atualizarUsuario(int id, String email, String senha, String nome, String telefone) {
        Usuario usuario = usuarioDAO.findById(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        // Validações
        if (email != null && !email.trim().isEmpty()) {
            usuario.setEmail(email);
        }
        if (senha != null && senha.length() >= 3) {
            usuario.setSenha(senha);
        }
        if (nome != null && !nome.trim().isEmpty()) {
            usuario.setNome(nome);
        }
        if (telefone != null) {
            usuario.setTelefone(telefone);
        }

        return usuarioDAO.update(usuario);
    }

    // DELETE
    public void excluirUsuario(int id) {
        Usuario usuario = usuarioDAO.findById(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        usuarioDAO.delete(id);
    }

    // Lógica de negócio específica
    public Usuario realizarLogin(String email, String senha) {
        Usuario usuario = usuarioDAO.login(email, senha);
        if (usuario == null) {
            throw new IllegalArgumentException("Email ou senha inválidos");
        }
        return usuario;
    }

    public void incrementarEmprestimos(int id) {
        Usuario usuario = usuarioDAO.findById(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        usuario.incrementarEmprestimos();
        usuarioDAO.update(usuario);
    }

    public void decrementarEmprestimos(int id) {
        Usuario usuario = usuarioDAO.findById(id);
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }
        usuario.decrementarEmprestimos();
        usuarioDAO.update(usuario);
    }
}