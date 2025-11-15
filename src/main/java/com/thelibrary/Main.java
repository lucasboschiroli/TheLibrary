package com.thelibrary;

import com.thelibrary.model.*;
import com.thelibrary.service.*;
import com.thelibrary.util.JPAUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final UsuarioService usuarioService = new UsuarioService();
    private static final BibliotecarioService bibliotecarioService = new BibliotecarioService();
    private static final LivroService livroService = new LivroService();
    private static final ArtigoService artigoService = new ArtigoService();
    private static final RevistaService revistaService = new RevistaService();
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private static Bibliotecario bibliotecarioLogado = null;
    private static Usuario usuarioLogado = null;

    public static void main(String[] args) {
        try {

            if (!telaAutenticacao()) {
                System.out.println("\nEncerrando sistema...");
                return;
            }

            // Menu principal após autenticação
            boolean continuar = true;
            while (continuar) {
                exibirMenuPrincipal();
                int opcao = lerInteiro();

                switch (opcao) {
                    case 1:
                        if (bibliotecarioLogado != null) {
                            menuPessoas();
                        } else {
                            System.out.println("\n✗ Acesso negado! Apenas bibliotecários.");
                        }
                        break;
                    case 2:
                        menuLivros();
                        break;
                    case 3:
                        menuArtigos();
                        break;
                    case 4:
                        menuRevistas();
                        break;
                    case 0:
                        continuar = false;
                        System.out.println("\nEncerrando sistema... Até logo!");
                        break;
                    default:
                        System.out.println("\n✗ Opção inválida!");
                }
            }
        } finally {
            JPAUtil.close();
            scanner.close();
        }
    }

    // ========== TELA INICIAL ==========
    private static boolean telaAutenticacao() {
        while (true) {
            System.out.println("\n╔════════════════════════════════╗");
            System.out.println("║     BEM-VINDO A THE LIBRARY    ║");
            System.out.println("╚════════════════════════════════╝");
            System.out.println("1. Cadastrar-se como Usuário");
            System.out.println("2. Cadastrar-se como Bibliotecário");
            System.out.println("3. Login como Usuário");
            System.out.println("4. Login como Bibliotecário");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    cadastrarNovoUsuario();
                    break;
                case 2:
                    cadastrarNovoBibliotecario();
                    break;
                case 3:
                    if (loginUsuario()) return true;
                    break;
                case 4:
                    if (loginBibliotecario()) return true;
                    break;
                case 0:
                    return false;
                default:
                    System.out.println("\n✗ Opção inválida!");
            }
        }
    }

    private static boolean loginBibliotecario() {
        try {
            System.out.println("\n--- LOGIN BIBLIOTECÁRIO ---");
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            bibliotecarioLogado = bibliotecarioService.realizarLogin(email, senha);
            System.out.println("\n✓ Login realizado com sucesso!");
            System.out.println("  Bem-vindo, " + bibliotecarioLogado.getNome() + "!");
            return true;
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
            return false;
        }
    }

    private static boolean loginUsuario() {
        try {
            System.out.println("\n--- LOGIN USUÁRIO ---");
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();

            usuarioLogado = usuarioService.realizarLogin(email, senha);
            System.out.println("\n✓ Login realizado com sucesso!");
            System.out.println("  Bem-vindo, " + usuarioLogado.getNome() + "!");
            return true;
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
            return false;
        }
    }

    private static void cadastrarNovoUsuario() {
        try {
            System.out.println("\n--- CADASTRO DE USUÁRIO ---");
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();

            Usuario usuario = usuarioService.cadastrarUsuario(email, senha, nome, telefone);
            System.out.println("\n✓ Cadastro realizado com sucesso!");
            System.out.println("  Agora você pode fazer login.");
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void cadastrarNovoBibliotecario() {
        try {
            System.out.println("\n--- CADASTRO DE BIBLIOTECÁRIO ---");
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();

            Bibliotecario biblio = bibliotecarioService.cadastrarBibliotecario(email, senha, nome, telefone);
            System.out.println("\n✓ Cadastro realizado com sucesso!");
            System.out.println("  Agora você pode fazer login.");
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n╔════════════════════════════════════════╗");
        System.out.println("║          MENU PRINCIPAL                ║");
        System.out.println("╚════════════════════════════════════════╝");

        if (bibliotecarioLogado != null) {
            System.out.println("Logado como: " + bibliotecarioLogado.getNome() + " (Bibliotecário)");
        } else if (usuarioLogado != null) {
            System.out.println("Logado como: " + usuarioLogado.getNome() + " (Usuário)");
        }

        System.out.println("────────────────────────────────────────");

        if (bibliotecarioLogado != null) {
            System.out.println("1. Gerenciar Pessoas (Usuários e Bibliotecários)");
        }

        System.out.println("2. Gerenciar Livros");
        System.out.println("3. Gerenciar Artigos");
        System.out.println("4. Gerenciar Revistas");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    private static boolean verificarAutenticacaoBibliotecario() {
        if (bibliotecarioLogado == null) {
            System.out.println("\n✗ Acesso negado! Apenas bibliotecários podem realizar esta ação.");
            return false;
        }
        return true;
    }

    // ========== MENU PESSOAS (USUÁRIOS E BIBLIOTECÁRIOS) ==========
    private static void menuPessoas() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n╔════════════════════════════════════════╗");
            System.out.println("║       GERENCIAR PESSOAS                ║");
            System.out.println("╚════════════════════════════════════════╝");
            System.out.println("--- USUÁRIOS ---");
            System.out.println("1. Cadastrar Usuário");
            System.out.println("2. Listar Todos os Usuários");
            System.out.println("3. Buscar Usuário por ID");
            System.out.println("4. Atualizar Usuário");
            System.out.println("5. Excluir Usuário");
            System.out.println("\n--- BIBLIOTECÁRIOS ---");
            System.out.println("6. Cadastrar Bibliotecário");
            System.out.println("7. Listar Todos os Bibliotecários");
            System.out.println("8. Buscar Bibliotecário por ID");
            System.out.println("9. Atualizar Bibliotecário");
            System.out.println("10. Excluir Bibliotecário");
            System.out.println("\n0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1: cadastrarUsuario(); break;
                case 2: listarUsuarios(); break;
                case 3: buscarUsuarioPorId(); break;
                case 4: atualizarUsuario(); break;
                case 5: excluirUsuario(); break;
                case 6: cadastrarBibliotecario(); break;
                case 7: listarBibliotecarios(); break;
                case 8: buscarBibliotecarioPorId(); break;
                case 9: atualizarBibliotecario(); break;
                case 10: excluirBibliotecario(); break;
                case 0: voltar = true; break;
                default: System.out.println("\n✗ Opção inválida!");
            }
        }
    }

    private static void cadastrarUsuario() {
        try {
            System.out.println("\n--- CADASTRAR USUÁRIO ---");
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();

            Usuario usuario = usuarioService.cadastrarUsuario(email, senha, nome, telefone);
            System.out.println("\n✓ Usuário cadastrado: " + usuario);
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void listarUsuarios() {
        try {
            System.out.println("\n--- LISTA DE USUÁRIOS ---");
            List<Usuario> usuarios = usuarioService.listarTodos();
            if (usuarios.isEmpty()) {
                System.out.println("Nenhum usuário cadastrado.");
            } else {
                usuarios.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarUsuarioPorId() {
        try {
            System.out.println("\n--- BUSCAR USUÁRIO ---");
            System.out.print("ID: ");
            int id = lerInteiro();

            Usuario usuario = usuarioService.buscarPorId(id);
            System.out.println("\n" + usuario);
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void atualizarUsuario() {
        try {
            System.out.println("\n--- ATUALIZAR USUÁRIO ---");
            System.out.print("ID do usuário: ");
            int id = lerInteiro();

            System.out.print("Novo Email (Enter para manter): ");
            String email = scanner.nextLine();
            System.out.print("Nova Senha (Enter para manter): ");
            String senha = scanner.nextLine();
            System.out.print("Novo Nome (Enter para manter): ");
            String nome = scanner.nextLine();
            System.out.print("Novo Telefone (Enter para manter): ");
            String telefone = scanner.nextLine();

            Usuario usuario = usuarioService.atualizarUsuario(id,
                    email.isEmpty() ? null : email,
                    senha.isEmpty() ? null : senha,
                    nome.isEmpty() ? null : nome,
                    telefone.isEmpty() ? null : telefone);

            System.out.println("\n✓ Usuário atualizado: " + usuario);
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void excluirUsuario() {
        try {
            System.out.println("\n--- EXCLUIR USUÁRIO ---");
            System.out.print("ID: ");
            int id = lerInteiro();

            usuarioService.excluirUsuario(id);
            System.out.println("\n✓ Usuário excluído com sucesso!");
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    // ========== MENU BIBLIOTECÁRIOS (REMOVED - NOW IN menuPessoas) ==========

    private static void cadastrarBibliotecario() {
        try {
            System.out.println("\n--- CADASTRAR BIBLIOTECÁRIO ---");
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Senha: ");
            String senha = scanner.nextLine();
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Telefone: ");
            String telefone = scanner.nextLine();

            Bibliotecario biblio = bibliotecarioService.cadastrarBibliotecario(email, senha, nome, telefone);
            System.out.println("\n✓ Bibliotecário cadastrado: " + biblio);
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void listarBibliotecarios() {
        try {
            System.out.println("\n--- LISTA DE BIBLIOTECÁRIOS ---");
            List<Bibliotecario> bibliotecarios = bibliotecarioService.listarTodos();
            if (bibliotecarios.isEmpty()) {
                System.out.println("Nenhum bibliotecário cadastrado.");
            } else {
                bibliotecarios.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarBibliotecarioPorId() {
        try {
            System.out.println("\n--- BUSCAR BIBLIOTECÁRIO ---");
            System.out.print("ID: ");
            int id = lerInteiro();

            Bibliotecario biblio = bibliotecarioService.buscarPorId(id);
            System.out.println("\n" + biblio);
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void atualizarBibliotecario() {
        try {
            System.out.println("\n--- ATUALIZAR BIBLIOTECÁRIO ---");
            System.out.print("ID do bibliotecário: ");
            int id = lerInteiro();

            System.out.print("Novo Email (Enter para manter): ");
            String email = scanner.nextLine();
            System.out.print("Nova Senha (Enter para manter): ");
            String senha = scanner.nextLine();
            System.out.print("Novo Nome (Enter para manter): ");
            String nome = scanner.nextLine();
            System.out.print("Novo Telefone (Enter para manter): ");
            String telefone = scanner.nextLine();

            Bibliotecario biblio = bibliotecarioService.atualizarBibliotecario(id,
                    email.isEmpty() ? null : email,
                    senha.isEmpty() ? null : senha,
                    nome.isEmpty() ? null : nome,
                    telefone.isEmpty() ? null : telefone);

            System.out.println("\n✓ Bibliotecário atualizado: " + biblio);
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void excluirBibliotecario() {
        try {
            System.out.println("\n--- EXCLUIR BIBLIOTECÁRIO ---");
            System.out.print("ID: ");
            int id = lerInteiro();

            bibliotecarioService.excluirBibliotecario(id);
            System.out.println("\n✓ Bibliotecário excluído com sucesso!");
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    // ========== MENU LIVROS ==========
    private static void menuLivros() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- MENU LIVROS ---");
            System.out.println("1. Cadastrar Livro 🔒");
            System.out.println("2. Listar Todos");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Buscar por Editora");
            System.out.println("5. Buscar por Autor");
            System.out.println("6. Atualizar Livro 🔒");
            System.out.println("7. Excluir Livro 🔒");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    if (verificarAutenticacaoBibliotecario()) cadastrarLivro();
                    break;
                case 2: listarLivros(); break;
                case 3: buscarLivroPorId(); break;
                case 4: buscarLivroPorEditora(); break;
                case 5: buscarLivroPorAutor(); break;
                case 6:
                    if (verificarAutenticacaoBibliotecario()) atualizarLivro();
                    break;
                case 7:
                    if (verificarAutenticacaoBibliotecario()) excluirLivro();
                    break;
                case 0: voltar = true; break;
                default: System.out.println("\n✗ Opção inválida!");
            }
        }
    }

    private static void cadastrarLivro() {
        try {
            System.out.println("\n--- CADASTRAR LIVRO ---");
            System.out.print("Título: ");
            String titulo = scanner.nextLine();
            System.out.print("Autor: ");
            String autor = scanner.nextLine();
            System.out.print("Status (Disponível/Emprestado/Reservado): ");
            String status = scanner.nextLine();
            System.out.print("Editora: ");
            String editora = scanner.nextLine();
            System.out.print("Data de Publicação (dd/MM/yyyy): ");
            String dataStr = scanner.nextLine();

            Date dataPublicacao = null;
            if (!dataStr.isEmpty()) {
                dataPublicacao = dateFormat.parse(dataStr);
            }

            Livro livro = livroService.cadastrarLivro(titulo, autor, status, editora, dataPublicacao);
            System.out.println("\n✓ Livro cadastrado: " + livro);
        } catch (ParseException e) {
            System.out.println("\n✗ Erro: Data inválida. Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void listarLivros() {
        try {
            System.out.println("\n--- LISTA DE LIVROS ---");
            List<Livro> livros = livroService.listarTodos();
            if (livros.isEmpty()) {
                System.out.println("Nenhum livro cadastrado.");
            } else {
                livros.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarLivroPorId() {
        try {
            System.out.println("\n--- BUSCAR LIVRO ---");
            System.out.print("ID: ");
            int id = lerInteiro();

            Livro livro = livroService.buscarPorId(id);
            System.out.println("\n" + livro);
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarLivroPorEditora() {
        try {
            System.out.println("\n--- BUSCAR LIVRO POR EDITORA ---");
            System.out.print("Editora: ");
            String editora = scanner.nextLine();

            List<Livro> livros = livroService.buscarPorEditora(editora);
            if (livros.isEmpty()) {
                System.out.println("Nenhum livro encontrado.");
            } else {
                livros.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarLivroPorAutor() {
        try {
            System.out.println("\n--- BUSCAR LIVRO POR AUTOR ---");
            System.out.print("Autor: ");
            String autor = scanner.nextLine();

            List<Livro> livros = livroService.buscarPorAutor(autor);
            if (livros.isEmpty()) {
                System.out.println("Nenhum livro encontrado.");
            } else {
                livros.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void atualizarLivro() {
        try {
            System.out.println("\n--- ATUALIZAR LIVRO ---");
            System.out.print("ID do livro: ");
            int id = lerInteiro();

            System.out.print("Novo Título (Enter para manter): ");
            String titulo = scanner.nextLine();
            System.out.print("Novo Autor (Enter para manter): ");
            String autor = scanner.nextLine();
            System.out.print("Novo Status (Enter para manter): ");
            String status = scanner.nextLine();
            System.out.print("Nova Editora (Enter para manter): ");
            String editora = scanner.nextLine();
            System.out.print("Nova Data de Publicação (dd/MM/yyyy) (Enter para manter): ");
            String dataStr = scanner.nextLine();

            Date dataPublicacao = null;
            if (!dataStr.isEmpty()) {
                dataPublicacao = dateFormat.parse(dataStr);
            }

            Livro livro = livroService.atualizarLivro(id,
                    titulo.isEmpty() ? null : titulo,
                    autor.isEmpty() ? null : autor,
                    status.isEmpty() ? null : status,
                    editora.isEmpty() ? null : editora,
                    dataPublicacao);

            System.out.println("\n✓ Livro atualizado por: " + bibliotecarioLogado.getNome());
            System.out.println("  " + livro);
        } catch (ParseException e) {
            System.out.println("\n✗ Erro: Data inválida. Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void excluirLivro() {
        try {
            System.out.println("\n--- EXCLUIR LIVRO ---");
            System.out.print("ID: ");
            int id = lerInteiro();

            livroService.excluirLivro(id);
            System.out.println("\n✓ Livro excluído por: " + bibliotecarioLogado.getNome());
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    // ========== MENU ARTIGOS ==========
    private static void menuArtigos() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- MENU ARTIGOS ---");
            System.out.println("1. Cadastrar Artigo 🔒 (Bibliotecário)");
            System.out.println("2. Listar Todos");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Buscar por Revista");
            System.out.println("5. Buscar por Revisor");
            System.out.println("6. Atualizar Artigo 🔒 (Bibliotecário)");
            System.out.println("7. Excluir Artigo 🔒 (Bibliotecário)");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    if (verificarAutenticacaoBibliotecario()) cadastrarArtigo();
                    break;
                case 2: listarArtigos(); break;
                case 3: buscarArtigoPorId(); break;
                case 4: buscarArtigoPorRevista(); break;
                case 5: buscarArtigoPorRevisor(); break;
                case 6:
                    if (verificarAutenticacaoBibliotecario()) atualizarArtigo();
                    break;
                case 7:
                    if (verificarAutenticacaoBibliotecario()) excluirArtigo();
                    break;
                case 0: voltar = true; break;
                default: System.out.println("\n✗ Opção inválida!");
            }
        }
    }

    private static void cadastrarArtigo() {
        try {
            System.out.println("\n--- CADASTRAR ARTIGO ---");
            System.out.print("Título: ");
            String titulo = scanner.nextLine();
            System.out.print("Autor: ");
            String autor = scanner.nextLine();
            System.out.print("Status (Disponível/Emprestado/Reservado): ");
            String status = scanner.nextLine();
            System.out.print("Revista: ");
            String revista = scanner.nextLine();
            System.out.print("Revisor: ");
            String revisor = scanner.nextLine();
            System.out.print("Data de Revisão (dd/MM/yyyy): ");
            String dataStr = scanner.nextLine();

            Date dataRevisao = null;
            if (!dataStr.isEmpty()) {
                dataRevisao = dateFormat.parse(dataStr);
            }

            Artigo artigo = artigoService.cadastrarArtigo(titulo, autor, status, revista, revisor, dataRevisao);
            System.out.println("\n✓ Artigo cadastrado por: " + bibliotecarioLogado.getNome());
            System.out.println("  " + artigo);
        } catch (ParseException e) {
            System.out.println("\n✗ Erro: Data inválida. Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void listarArtigos() {
        try {
            System.out.println("\n--- LISTA DE ARTIGOS ---");
            List<Artigo> artigos = artigoService.listarTodos();
            if (artigos.isEmpty()) {
                System.out.println("Nenhum artigo cadastrado.");
            } else {
                artigos.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarArtigoPorId() {
        try {
            System.out.println("\n--- BUSCAR ARTIGO ---");
            System.out.print("ID: ");
            int id = lerInteiro();

            Artigo artigo = artigoService.buscarPorId(id);
            System.out.println("\n" + artigo);
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarArtigoPorRevista() {
        try {
            System.out.println("\n--- BUSCAR ARTIGO POR REVISTA ---");
            System.out.print("Revista: ");
            String revista = scanner.nextLine();

            List<Artigo> artigos = artigoService.buscarPorRevista(revista);
            if (artigos.isEmpty()) {
                System.out.println("Nenhum artigo encontrado.");
            } else {
                artigos.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarArtigoPorRevisor() {
        try {
            System.out.println("\n--- BUSCAR ARTIGO POR REVISOR ---");
            System.out.print("Revisor: ");
            String revisor = scanner.nextLine();

            List<Artigo> artigos = artigoService.buscarPorRevisor(revisor);
            if (artigos.isEmpty()) {
                System.out.println("Nenhum artigo encontrado.");
            } else {
                artigos.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void atualizarArtigo() {
        try {
            System.out.println("\n--- ATUALIZAR ARTIGO ---");
            System.out.print("ID do artigo: ");
            int id = lerInteiro();

            System.out.print("Novo Título (Enter para manter): ");
            String titulo = scanner.nextLine();
            System.out.print("Novo Autor (Enter para manter): ");
            String autor = scanner.nextLine();
            System.out.print("Novo Status (Enter para manter): ");
            String status = scanner.nextLine();
            System.out.print("Nova Revista (Enter para manter): ");
            String revista = scanner.nextLine();
            System.out.print("Novo Revisor (Enter para manter): ");
            String revisor = scanner.nextLine();
            System.out.print("Nova Data de Revisão (dd/MM/yyyy) (Enter para manter): ");
            String dataStr = scanner.nextLine();

            Date dataRevisao = null;
            if (!dataStr.isEmpty()) {
                dataRevisao = dateFormat.parse(dataStr);
            }

            Artigo artigo = artigoService.atualizarArtigo(id,
                    titulo.isEmpty() ? null : titulo,
                    autor.isEmpty() ? null : autor,
                    status.isEmpty() ? null : status,
                    revista.isEmpty() ? null : revista,
                    revisor.isEmpty() ? null : revisor,
                    dataRevisao);

            System.out.println("\n✓ Artigo atualizado por: " + bibliotecarioLogado.getNome());
            System.out.println("  " + artigo);
        } catch (ParseException e) {
            System.out.println("\n✗ Erro: Data inválida. Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void excluirArtigo() {
        try {
            System.out.println("\n--- EXCLUIR ARTIGO ---");
            System.out.print("ID: ");
            int id = lerInteiro();

            artigoService.excluirArtigo(id);
            System.out.println("\n✓ Artigo excluído por: " + bibliotecarioLogado.getNome());
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    // ========== MENU REVISTAS ==========
    private static void menuRevistas() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- MENU REVISTAS ---");
            System.out.println("1. Cadastrar Revista 🔒 (Bibliotecário)");
            System.out.println("2. Listar Todas");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Buscar por Nome da Revista");
            System.out.println("5. Buscar por Número de Edição");
            System.out.println("6. Atualizar Revista 🔒 (Bibliotecário)");
            System.out.println("7. Excluir Revista 🔒 (Bibliotecário)");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    if (verificarAutenticacaoBibliotecario()) cadastrarRevista();
                    break;
                case 2: listarRevistas(); break;
                case 3: buscarRevistaPorId(); break;
                case 4: buscarRevistaPorNome(); break;
                case 5: buscarRevistaPorNumeroEdicao(); break;
                case 6:
                    if (verificarAutenticacaoBibliotecario()) atualizarRevista();
                    break;
                case 7:
                    if (verificarAutenticacaoBibliotecario()) excluirRevista();
                    break;
                case 0: voltar = true; break;
                default: System.out.println("\n✗ Opção inválida!");
            }
        }
    }

    private static void cadastrarRevista() {
        try {
            System.out.println("\n--- CADASTRAR REVISTA ---");
            System.out.print("Título: ");
            String titulo = scanner.nextLine();
            System.out.print("Autor: ");
            String autor = scanner.nextLine();
            System.out.print("Status (Disponível/Emprestado/Reservado): ");
            String status = scanner.nextLine();
            System.out.print("Nome da Revista: ");
            String nomeRevista = scanner.nextLine();
            System.out.print("Número da Edição: ");
            String numeroEdicao = scanner.nextLine();
            System.out.print("Data da Edição (dd/MM/yyyy): ");
            String dataStr = scanner.nextLine();

            Date dataEdicao = null;
            if (!dataStr.isEmpty()) {
                dataEdicao = dateFormat.parse(dataStr);
            }

            Revista revista = revistaService.cadastrarRevista(titulo, autor, status, nomeRevista, numeroEdicao, dataEdicao);
            System.out.println("\n✓ Revista cadastrada por: " + bibliotecarioLogado.getNome());
            System.out.println("  " + revista);
        } catch (ParseException e) {
            System.out.println("\n✗ Erro: Data inválida. Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void listarRevistas() {
        try {
            System.out.println("\n--- LISTA DE REVISTAS ---");
            List<Revista> revistas = revistaService.listarTodas();
            if (revistas.isEmpty()) {
                System.out.println("Nenhuma revista cadastrada.");
            } else {
                revistas.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarRevistaPorId() {
        try {
            System.out.println("\n--- BUSCAR REVISTA ---");
            System.out.print("ID: ");
            int id = lerInteiro();

            Revista revista = revistaService.buscarPorId(id);
            System.out.println("\n" + revista);
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarRevistaPorNome() {
        try {
            System.out.println("\n--- BUSCAR REVISTA POR NOME ---");
            System.out.print("Nome da Revista: ");
            String nomeRevista = scanner.nextLine();

            List<Revista> revistas = revistaService.buscarPorNomeRevista(nomeRevista);
            if (revistas.isEmpty()) {
                System.out.println("Nenhuma revista encontrada.");
            } else {
                revistas.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarRevistaPorNumeroEdicao() {
        try {
            System.out.println("\n--- BUSCAR REVISTA POR NÚMERO DE EDIÇÃO ---");
            System.out.print("Número da Edição: ");
            String numeroEdicao = scanner.nextLine();

            List<Revista> revistas = revistaService.buscarPorNumeroEdicao(numeroEdicao);
            if (revistas.isEmpty()) {
                System.out.println("Nenhuma revista encontrada.");
            } else {
                revistas.forEach(System.out::println);
            }
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void atualizarRevista() {
        try {
            System.out.println("\n--- ATUALIZAR REVISTA ---");
            System.out.print("ID da revista: ");
            int id = lerInteiro();

            System.out.print("Novo Título (Enter para manter): ");
            String titulo = scanner.nextLine();
            System.out.print("Novo Autor (Enter para manter): ");
            String autor = scanner.nextLine();
            System.out.print("Novo Status (Enter para manter): ");
            String status = scanner.nextLine();
            System.out.print("Novo Nome da Revista (Enter para manter): ");
            String nomeRevista = scanner.nextLine();
            System.out.print("Novo Número da Edição (Enter para manter): ");
            String numeroEdicao = scanner.nextLine();
            System.out.print("Nova Data da Edição (dd/MM/yyyy) (Enter para manter): ");
            String dataStr = scanner.nextLine();

            Date dataEdicao = null;
            if (!dataStr.isEmpty()) {
                dataEdicao = dateFormat.parse(dataStr);
            }

            Revista revista = revistaService.atualizarRevista(id,
                    titulo.isEmpty() ? null : titulo,
                    autor.isEmpty() ? null : autor,
                    status.isEmpty() ? null : status,
                    nomeRevista.isEmpty() ? null : nomeRevista,
                    numeroEdicao.isEmpty() ? null : numeroEdicao,
                    dataEdicao);

            System.out.println("\n✓ Revista atualizada por: " + bibliotecarioLogado.getNome());
            System.out.println("  " + revista);
        } catch (ParseException e) {
            System.out.println("\n✗ Erro: Data inválida. Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void excluirRevista() {
        try {
            System.out.println("\n--- EXCLUIR REVISTA ---");
            System.out.print("ID: ");
            int id = lerInteiro();

            revistaService.excluirRevista(id);
            System.out.println("\n✓ Revista excluída por: " + bibliotecarioLogado.getNome());
        } catch (Exception e) {
            System.out.println("\n✗ Erro: " + e.getMessage());
        }
    }

    // ========== UTILITÁRIOS ==========
    private static int lerInteiro() {
        try {
            int valor = Integer.parseInt(scanner.nextLine());
            return valor;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}