package com.thelibrary.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "sugestao")
public class Sugestao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Temporal(TemporalType.DATE)
    @Column(name = "data_sugestao")
    private Date dataSugestao;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(length = 500, nullable = false)
    private String justificativa;

    // Dados do material sugerido
    @Column(name = "tipo_material", length = 50, nullable = false)
    private String tipoMaterial; // "Livro", "Artigo", "Revista"

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, length = 150)
    private String autor;

    // Campos específicos para Livro
    @Column(length = 100)
    private String editora;

    @Column(name = "data_publicacao")
    @Temporal(TemporalType.DATE)
    private Date dataPublicacao;

    // Campos específicos para Artigo
    @Column(length = 150)
    private String revista;

    @Column(length = 100)
    private String revisor;

    @Column(name = "data_revisao")
    @Temporal(TemporalType.DATE)
    private Date dataRevisao;

    // Campos específicos para Revista
    @Column(name = "nome_revista", length = 150)
    private String nomeRevista;

    @Column(name = "numero_edicao", length = 20)
    private String numeroEdicao;

    @Column(name = "data_edicao")
    @Temporal(TemporalType.DATE)
    private Date dataEdicao;

    public Sugestao() {
        this.dataSugestao = new Date();
    }

    // Métodos factory estáticos
    public static Sugestao criarSugestaoLivro(Usuario usuario, String justificativa,
                                              String titulo, String autor,
                                              String editora, Date dataPublicacao) {
        Sugestao sugestao = new Sugestao();
        sugestao.usuario = usuario;
        sugestao.justificativa = justificativa;
        sugestao.tipoMaterial = "Livro";
        sugestao.titulo = titulo;
        sugestao.autor = autor;
        sugestao.editora = editora;
        sugestao.dataPublicacao = dataPublicacao;
        return sugestao;
    }

    public static Sugestao criarSugestaoArtigo(Usuario usuario, String justificativa,
                                               String titulo, String autor,
                                               String revista, String revisor, Date dataRevisao) {
        Sugestao sugestao = new Sugestao();
        sugestao.usuario = usuario;
        sugestao.justificativa = justificativa;
        sugestao.tipoMaterial = "Artigo";
        sugestao.titulo = titulo;
        sugestao.autor = autor;
        sugestao.revista = revista;
        sugestao.revisor = revisor;
        sugestao.dataRevisao = dataRevisao;
        return sugestao;
    }

    public static Sugestao criarSugestaoRevista(Usuario usuario, String justificativa,
                                                String titulo, String autor,
                                                String nomeRevista, String numeroEdicao, Date dataEdicao) {
        Sugestao sugestao = new Sugestao();
        sugestao.usuario = usuario;
        sugestao.justificativa = justificativa;
        sugestao.tipoMaterial = "Revista";
        sugestao.titulo = titulo;
        sugestao.autor = autor;
        sugestao.nomeRevista = nomeRevista;
        sugestao.numeroEdicao = numeroEdicao;
        sugestao.dataEdicao = dataEdicao;
        return sugestao;
    }

    // Getters e Setters (mantenha todos os getters e setters existentes, remova apenas o campo 'status')
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDataSugestao() {
        return dataSugestao;
    }

    public void setDataSugestao(Date dataSugestao) {
        this.dataSugestao = dataSugestao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
    }

    public String getTipoMaterial() {
        return tipoMaterial;
    }

    public void setTipoMaterial(String tipoMaterial) {
        this.tipoMaterial = tipoMaterial;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getEditora() {
        return editora;
    }

    public void setEditora(String editora) {
        this.editora = editora;
    }

    public Date getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao(Date dataPublicacao) {
        this.dataPublicacao = dataPublicacao;
    }

    public String getRevista() {
        return revista;
    }

    public void setRevista(String revista) {
        this.revista = revista;
    }

    public String getRevisor() {
        return revisor;
    }

    public void setRevisor(String revisor) {
        this.revisor = revisor;
    }

    public Date getDataRevisao() {
        return dataRevisao;
    }

    public void setDataRevisao(Date dataRevisao) {
        this.dataRevisao = dataRevisao;
    }

    public String getNomeRevista() {
        return nomeRevista;
    }

    public void setNomeRevista(String nomeRevista) {
        this.nomeRevista = nomeRevista;
    }

    public String getNumeroEdicao() {
        return numeroEdicao;
    }

    public void setNumeroEdicao(String numeroEdicao) {
        this.numeroEdicao = numeroEdicao;
    }

    public Date getDataEdicao() {
        return dataEdicao;
    }

    public void setDataEdicao(Date dataEdicao) {
        this.dataEdicao = dataEdicao;
    }

    @Override
    public String toString() {
        return "Sugestao{" +
                "id=" + id +
                ", tipoMaterial='" + tipoMaterial + '\'' +
                ", titulo='" + titulo + '\'' +
                ", autor='" + autor + '\'' +
                ", usuario=" + (usuario != null ? usuario.getNome() : "null") +
                ", dataSugestao=" + dataSugestao +
                '}';
    }
}