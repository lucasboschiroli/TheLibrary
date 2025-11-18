package com.thelibrary.service;

import com.thelibrary.dao.MaterialBibliograficoDAO;
import com.thelibrary.model.MaterialBibliografico;

import java.util.List;

public class MaterialBibliograficoService {
    private static MaterialBibliograficoDAO materialBibliograficoDAO;
    public MaterialBibliograficoService() {this.materialBibliograficoDAO = new MaterialBibliograficoDAO();}

    public List<MaterialBibliografico> listarTodos() {
        return materialBibliograficoDAO.findAll();
    }

}
