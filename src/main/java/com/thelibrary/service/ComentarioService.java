package com.thelibrary.service;

import com.thelibrary.dao.ComentarioDAO;
import com.thelibrary.dao.GenericDAO;
import com.thelibrary.dao.MaterialBibliograficoDAO;
import com.thelibrary.dao.MultaDAO;
import com.thelibrary.model.*;

import java.util.Date;
import java.util.List;

public class ComentarioService {
    private final ComentarioDAO comentarioDAO;
    private final MaterialBibliograficoDAO materialBibliograficoDAO;

    public ComentarioService(){this.comentarioDAO = new ComentarioDAO(); this.materialBibliograficoDAO = new MaterialBibliograficoDAO();}

    public Comentario enviarComentario(int materialBibliograficoId, String comentario, Usuario usuario) {
        if (materialBibliograficoId == 0){
            throw new RuntimeException("Nenhum material bibliográfico selecionado");
        }

        if (comentario == null){
            throw new RuntimeException("Nenhum comentário foi escrito");
        }

        if (usuario == null){
            throw new RuntimeException("O usuário não foi encontrado");
        }
        List<Comentario> comentarios = comentarioDAO.buscarPorMaterialBibliograficoEUsuario(materialBibliograficoId, usuario.getId());
        if (!comentarios.isEmpty()){
            throw new RuntimeException("Este livro já foi comentado por este usuário.");
        }
        MaterialBibliografico materialBibliografico = materialBibliograficoDAO.findById(materialBibliograficoId);
        Comentario comentarioObj = new Comentario();
        comentarioObj.setComentario(comentario);
        comentarioObj.setUsuario(usuario);
        comentarioObj.setMaterialBibliografico(materialBibliografico);
        return comentarioDAO.save(comentarioObj);
    }

    public void removerComentario(int id) {
        Comentario comentario = comentarioDAO.findById(id);
        if (comentario == null){
            throw new RuntimeException("Nenhum comentário encontrado");
        }
        comentarioDAO.delete(id);
    }

    public List<Comentario> buscarComentariosUsuario(Usuario usuario) {
        return comentarioDAO.buscarPorUsuario(usuario);
    }

    public List<Comentario> buscarComentariosLivro(int id) {
        return comentarioDAO.buscarPorMaterialBibliografico(id);
    }

    public Comentario editarComentario(int id, String novoComentario) {
        Comentario comentario = comentarioDAO.findById(id);
        comentario.setComentario(novoComentario);
        return comentarioDAO.update(comentario);
    }

    public Comentario buscarComentarioId(int id) {
        return comentarioDAO.findById(id);
    }
}
