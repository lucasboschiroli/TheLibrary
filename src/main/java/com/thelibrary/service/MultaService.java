package com.thelibrary.service;

import com.thelibrary.dao.MultaDAO;
import com.thelibrary.dao.EmprestimoDAO;
import com.thelibrary.model.Emprestimo;
import com.thelibrary.model.Multa;
import com.thelibrary.model.Usuario;


import java.util.Date;
import java.util.List;

public class MultaService {
    private final MultaDAO multaDAO;
    private final EmprestimoDAO emprestimoDAO;

    public MultaService(){this.multaDAO = new MultaDAO(); this.emprestimoDAO = new EmprestimoDAO();}

    public Multa registrarMulta(int emprestimoId, double valor, String justificativa) {
        if (emprestimoId == 0){
            throw new RuntimeException("Nenhum emprestimo selecionado");
        }

        if (valor <= 0.0){
            throw new RuntimeException("Nenhum valor informado");
        }

        if (justificativa == null){
            throw new RuntimeException("O motivo da multa não foi justificado");
        }
        List<Multa> multas = multaDAO.buscarPorIdEmprestimo(emprestimoId);
        if (!multas.isEmpty()){
            throw new RuntimeException("Este empréstimo já foi multado.");
        }
        Emprestimo e = emprestimoDAO.findById(emprestimoId);
        Multa multa = new Multa();
        multa.setEmprestimo(e);
        multa.setPaga(false);
        multa.setJustificativa(justificativa);
        multa.setValor(valor);
        multa.setData_emissao(new Date());
        return multaDAO.save(multa);
    }

    public void removerMulta(int id) {
        Multa multa = multaDAO.findById(id);
        if (multa == null){
            throw new RuntimeException("Nenhuma multa encontrada");
        }
        multaDAO.delete(id);
    }

    public void quitarMulta(int id) {
        Multa multa = multaDAO.findById(id);
        multa.setPaga(true);
        multaDAO.update(multa);
    }

    public List<Multa> listarTodos() {
        return multaDAO.findAll();
    }

    public Multa buscarPorId(int id) { return multaDAO.findById(id); }
}
