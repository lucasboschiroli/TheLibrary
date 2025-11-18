package com.thelibrary;

import com.thelibrary.model.*;
import com.thelibrary.service.*;
import com.thelibrary.util.JPAUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Scanner;

/**
 * Main refatorado: mantém todas as funções e menus, organizados e com helpers.
 * Tudo em um único arquivo (opção B).
 */
public class Main {

    // Services (existentes no seu projeto)
    private static final UsuarioService usuarioService = new UsuarioService();
    private static final BibliotecarioService bibliotecarioService = new BibliotecarioService();
    private static final LivroService livroService = new LivroService();
    private static final ArtigoService artigoService = new ArtigoService();
    private static final RevistaService revistaService = new RevistaService();
    private static final SugestaoService sugestaoService = new SugestaoService();
    private static final AvaliacaoService avaliacaoService = new AvaliacaoService();
    private static final ComentarioService comentarioService = new ComentarioService();
    private static final MultaService multaService = new MultaService();
    private static final MaterialBibliograficoService materialBibliograficoService = new MaterialBibliograficoService();

    // Utilitários
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    // Sessão
    private static Bibliotecario bibliotecarioLogado = null;
    public static Usuario usuarioLogado = null;

    public static void main(String[] args) {
        try {
            iniciarSistema();

            if (!telaAutenticacao()) {
                println("\nEncerrando sistema...");
                return;
            }

            executarLoopPrincipal();

        } finally {
            finalizarRecursos();
        }
    }

    // ------------------------
    // Inicialização / Finalização
    // ------------------------
    private static void iniciarSistema() {
        println("\n╔════════════════════════════════╗");
        println("║     INICIANDO THE LIBRARY      ║");
        println("╚════════════════════════════════╝");
    }

    private static void finalizarRecursos() {
        JPAUtil.close();
        scanner.close();
    }

    // ------------------------
    // Fluxo principal
    // ------------------------
    private static boolean telaAutenticacao() {
        while (true) {
            println("\n╔════════════════════════════════╗");
            println("║     BEM-VINDO A THE LIBRARY    ║");
            println("╚════════════════════════════════╝");
            println("1. Cadastrar-se como Usuário");
            println("2. Cadastrar-se como Bibliotecário");
            println("3. Login como Usuário");
            println("4. Login como Bibliotecário");
            println("0. Sair");
            print("Escolha uma opção: ");

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
                    println("\n✗ Opção inválida!");
            }
        }
    }

    private static boolean loginBibliotecario() {
        try {
            println("\n--- LOGIN BIBLIOTECÁRIO ---");
            String email = lerLinha("Email: ");
            String senha = lerLinha("Senha: ");

            bibliotecarioLogado = bibliotecarioService.realizarLogin(email, senha);
            println("\n✓ Login realizado com sucesso!");
            println("  Bem-vindo, " + bibliotecarioLogado.getNome() + "!");
            return true;
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
            return false;
        }
    }

    private static boolean loginUsuario() {
        try {
            println("\n--- LOGIN USUÁRIO ---");
            String email = lerLinha("Email: ");
            String senha = lerLinha("Senha: ");

            usuarioLogado = usuarioService.realizarLogin(email, senha);
            println("\n✓ Login realizado com sucesso!");
            println("  Bem-vindo, " + usuarioLogado.getNome() + "!");
            return true;
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
            return false;
        }
    }

    // ---------- Loop principal de menus ----------
    private static void executarLoopPrincipal() {
        boolean continuar = true;
        while (continuar) {
            exibirMenuPrincipal();
            int opcao = lerInteiro();

            if (bibliotecarioLogado != null) {
                continuar = processarMenuBibliotecario(opcao);
            } else if (usuarioLogado != null) {
                continuar = processarMenuUsuario(opcao);
            } else {
                // Se não houver usuário logado (não deveria ocorrer porque passamos pela autenticação)
                println("\n✗ Nenhum usuário autenticado.");
                continuar = false;
            }
        }
    }

    private static void exibirMenuPrincipal() {
        println("\n╔════════════════════════════════════════╗");
        println("║          MENU PRINCIPAL                ║");
        println("╚════════════════════════════════════════╝");

        if (bibliotecarioLogado != null) {
            println("Logado como: " + bibliotecarioLogado.getNome() + " (Bibliotecário)");
            println("────────────────────────────────────────");
            println("1. Gerenciar Pessoas");
            println("2. Materiais Bibliográficos");
            println("3. Empréstimos");
            println("4. Multas");
            println("5. Interações dos Usuários");
            println("0. Sair");
        } else {
            // Usuário comum
            println("Logado como: " + usuarioLogado.getNome() + " (Usuário)");
            println("────────────────────────────────────────");
            println("1. Consultar Materiais");
            println("2. Meus Empréstimos");
            println("3. Minhas Interações");
            println("0. Sair");
        }
        print("Escolha uma opção: ");
    }

    private static boolean processarMenuBibliotecario(int opcao) {
        switch (opcao) {
            case 1: menuPessoas(); break;
            case 2: menuMateriais(); break;
            case 3: menuEmprestimos(); break;
            case 4: menuMultas(); break;
            case 5: menuInteracoes(); break;
            case 0:
                println("\nEncerrando sistema... Até logo!");
                return false;
            default:
                println("\n✗ Opção inválida!");
        }
        return true;
    }

    private static boolean processarMenuUsuario(int opcao) {
        switch (opcao) {
            case 1: menuConsultarMateriaisUsuario(); break;
            case 2: menuMeusEmprestimos(); break;
            case 3: menuMinhasInteracoesUsuario(); break;
            case 0:
                println("\nEncerrando sistema... Até logo!");
                return false;
            default:
                println("\n✗ Opção inválida!");
        }
        return true;
    }

    // ------------------------
    // MENUS: Pessoas
    // ------------------------
    private static void menuPessoas() {
        while (true) {
            println("\n╔════════════════════════════════════════╗");
            println("║           GERENCIAR PESSOAS            ║");
            println("╚════════════════════════════════════════╝");
            println("1. Usuários");
            println("2. Bibliotecários");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1: menuCRUDUsuarios(); break;
                case 2: menuCRUDBibliotecarios(); break;
                case 0: return;
                default: println("\n✗ Opção inválida!");
            }
        }
    }

    private static void menuCRUDUsuarios() {
        while (true) {
            println("\n--- USUÁRIOS ---");
            println("1. Cadastrar Usuário");
            println("2. Listar Usuários");
            println("3. Buscar Usuário por ID");
            println("4. Atualizar Usuário");
            println("5. Excluir Usuário");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1: cadastrarUsuario(); break;
                case 2: listarUsuarios(); break;
                case 3: buscarUsuarioPorId(); break;
                case 4: atualizarUsuario(); break;
                case 5: excluirUsuario(); break;
                case 0: return;
                default: println("\n✗ Opção inválida!");
            }
        }
    }

    private static void menuCRUDBibliotecarios() {
        while (true) {
            println("\n--- BIBLIOTECÁRIOS ---");
            println("1. Cadastrar Bibliotecário");
            println("2. Listar Bibliotecários");
            println("3. Buscar Bibliotecário por ID");
            println("4. Atualizar Bibliotecário");
            println("5. Excluir Bibliotecário");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1:
                    if (requireBibliotecario()) cadastrarBibliotecario();
                    break;
                case 2: listarBibliotecarios(); break;
                case 3: buscarBibliotecarioPorId(); break;
                case 4:
                    if (requireBibliotecario()) atualizarBibliotecario();
                    break;
                case 5:
                    if (requireBibliotecario()) excluirBibliotecario();
                    break;
                case 0: return;
                default: println("\n✗ Opção inválida!");
            }
        }
    }

    // ------------------------
    // MENUS: Materiais (Livros / Artigos / Revistas)
    // ------------------------
    private static void menuMateriais() {
        while (true) {
            println("\n╔════════════════════════════════════════╗");
            println("║        MATERIAIS BIBLIOGRÁFICOS        ║");
            println("╚════════════════════════════════════════╝");
            println("1. Livros");
            println("2. Artigos");
            println("3. Revistas");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1: menuCRUDLivros(); break;
                case 2: menuCRUDArtigos(); break;
                case 3: menuCRUDRevistas(); break;
                case 0: return;
                default: println("\n✗ Opção inválida!");
            }
        }
    }

    // ----- Livros -----
    private static void menuCRUDLivros() {
        while (true) {
            println("\n--- LIVROS ---");
            println("1. Cadastrar Livro");
            println("2. Listar Todos");
            println("3. Buscar por ID");
            println("4. Buscar por Editora");
            println("5. Buscar por Autor");
            println("6. Atualizar Livro");
            println("7. Excluir Livro");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1:
                    if (requireBibliotecario()) cadastrarLivro();
                    break;
                case 2: listarLivros(); break;
                case 3: buscarLivroPorId(); break;
                case 4: buscarLivroPorEditora(); break;
                case 5: buscarLivroPorAutor(); break;
                case 6:
                    if (requireBibliotecario()) atualizarLivro();
                    break;
                case 7:
                    if (requireBibliotecario()) excluirLivro();
                    break;
                case 0: return;
                default: println("\n✗ Opção inválida!");
            }
        }
    }

    // ----- Artigos -----
    private static void menuCRUDArtigos() {
        while (true) {
            println("\n--- ARTIGOS ---");
            println("1. Cadastrar Artigo");
            println("2. Listar Todos");
            println("3. Buscar por ID");
            println("4. Buscar por Revista");
            println("5. Buscar por Revisor");
            println("6. Atualizar Artigo");
            println("7. Excluir Artigo");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1:
                    if (requireBibliotecario()) cadastrarArtigo();
                    break;
                case 2: listarArtigos(); break;
                case 3: buscarArtigoPorId(); break;
                case 4: buscarArtigoPorRevista(); break;
                case 5: buscarArtigoPorRevisor(); break;
                case 6:
                    if (requireBibliotecario()) atualizarArtigo();
                    break;
                case 7:
                    if (requireBibliotecario()) excluirArtigo();
                    break;
                case 0: return;
                default: println("\n✗ Opção inválida!");
            }
        }
    }

    // ----- Revistas -----
    private static void menuCRUDRevistas() {
        while (true) {
            println("\n--- REVISTAS ---");
            println("1. Cadastrar Revista");
            println("2. Listar Todas");
            println("3. Buscar por ID");
            println("4. Buscar por Nome da Revista");
            println("5. Buscar por Número de Edição");
            println("6. Atualizar Revista");
            println("7. Excluir Revista");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1:
                    if (requireBibliotecario()) cadastrarRevista();
                    break;
                case 2: listarRevistas(); break;
                case 3: buscarRevistaPorId(); break;
                case 4: buscarRevistaPorNome(); break;
                case 5: buscarRevistaPorNumeroEdicao(); break;
                case 6:
                    if (requireBibliotecario()) atualizarRevista();
                    break;
                case 7:
                    if (requireBibliotecario()) excluirRevista();
                    break;
                case 0: return;
                default: println("\n✗ Opção inválida!");
            }
        }
    }

    // ------------------------
    // MENUS: Empréstimos (placeholders mínimos)
    // ------------------------
    private static final EmprestimoService emprestimoService = new EmprestimoService();

    private static void menuEmprestimos() {
        while (true) {
            println("\n--- EMPRÉSTIMOS ---");
            println("1. Registrar Empréstimo");
            println("2. Listar Empréstimos");
            println("3. Buscar Empréstimo (ID)");
            println("4. Registrar Devolução");
            println("5. Renovar Empréstimo");
            println("6. Excluir Empréstimo");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1: com.thelibrary.controller.EmprestimoController.registrarEmprestimo(); break;
                case 2: com.thelibrary.controller.EmprestimoController.listarEmprestimos(); break;
                case 3: com.thelibrary.controller.EmprestimoController.buscarEmprestimoPorId(); break;
                case 4: com.thelibrary.controller.EmprestimoController.devolverEmprestimo(); break;
                case 5: com.thelibrary.controller.EmprestimoController.renovarEmprestimo(); break;
                case 6: com.thelibrary.controller.EmprestimoController.excluirEmprestimo(); break;
                case 0: return;
                default: println("\n✗ Opção inválida!");
            }
        }
    }


    // ------------------------
    // MENUS: Multas (placeholders mínimos)
    // ------------------------
    private static void menuMultas() {
        while (true) {
            println("\n--- MULTAS ---");
            println("1. Listar Multas");
            println("2. Buscar Multa (ID)");
            println("3. Associar Multa a Empréstimo");
            println("4. Quitar Multa");
            println("5. Excluir Multa");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1: listarTodasAsMultas(); break;
                case 2: buscarMultaPorId(); break;
                case 3: menuMultar(); break;
                case 4: menuQuitarMulta(); break;
                case 5: menuExcluirMulta(); break;
                case 0: return;
                default: println("\n✗ Opção inválida!");
            }
        }
    }

    // ------------------------
    // MENUS: Interações (Comentários / Avaliações / Sugestões)
    // ------------------------
    private static void menuInteracoes() {
        while (true) {
            println("\n--- INTERAÇÕES ---");
            println("1. Comentários");
            println("2. Avaliações");
            println("3. Sugestões");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1: menuComentarios(); break;
                case 2: menuAvaliacoes(); break;
                case 3: menuSugestoes(); break;
                case 0: return;
                default: println("\n✗ Opção inválida!");
            }
        }
    }

    // Comentários (placeholders)
    private static void menuComentarios() {
        while (true) {
            println("\n--- COMENTÁRIOS ---");
            println("1. Listar Comentários");
            println("2. Buscar Comentário (ID)");
            println("3. Excluir Comentário");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1: visualizarComentariosLivro(); break;
                case 2: buscarComentarioId(); break;
                case 3:
                    if (requireBibliotecario()) removerComentarioId();
                    else println("\n✗ Ação reservada a bibliotecários.");
                    break;
                case 0: return;
                default: println("\n✗ Opção inválida!");
            }
        }
    }

    // Avaliações (placeholders)
    private static void menuAvaliacoes() {
        while (true) {
            println("\n--- AVALIAÇÕES ---");
            println("1. Nova Avaliação");          // ← ADICIONADO AQUI
            println("2. Listar Minhas Avaliações");
            println("3. Editar Minha Avaliação");
            println("4. Excluir Minha Avaliação");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1: criarNovaAvaliacao(); break;      // ← MOVIDO PARA AQUI
                case 2: listarMinhasAvaliacoes(); break;
                case 3: editarMinhaAvaliacao(); break;
                case 4: excluirMinhaAvaliacao(); break;
                case 0: return;
                default: println("\n✗ Opção inválida!");
            }
        }
    }

    // Sugestões (placeholders)
    private static void menuSugestoes() {
        while (true) {
            println("\n--- SUGESTÕES ---");
            println("1. Listar Sugestões");
            println("2. Buscar Sugestão (ID)");
            println("3. Excluir Sugestão");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1: println("Listar Sugestões: implementar SugestaoService"); break;
                case 2: println("Buscar Sugestão: implementar SugestaoService"); break;
                case 3:
                    if (requireBibliotecario()) println("Excluir Sugestão: implementar SugestaoService");
                    else println("\n✗ Ação reservada a bibliotecários.");
                    break;
                case 0: return;
                default: println("\n✗ Opção inválida!");
            }
        }
    }

    // ------------------------
    // MENUS: Usuário - consultas e próprias interações
    // ------------------------
    private static void menuConsultarMateriaisUsuario() {
        while (true) {
            println("\n--- CONSULTAR MATERIAIS ---");
            println("1. Livros");
            println("2. Artigos");
            println("3. Revistas");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1: listarLivros(); break;
                case 2: listarArtigos(); break;
                case 3: listarRevistas(); break;
                case 0: return;
                default: println("\n✗ Opção inválida!");
            }
        }
    }

    private static void menuMeusEmprestimos() {
        while (true) {
            println("\n--- MEUS EMPRÉSTIMOS ---");
            println("1. Listar meus empréstimos");
            println("2. Solicitar renovação");
            println("3. Registrar devolução");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            try {
                switch (op) {

                    case 1:
                        com.thelibrary.controller.EmprestimoController.menuListarEmprestimosUsuario();
                        break;
                    case 2:
                        com.thelibrary.controller.EmprestimoController.menuRenovarEmprestimoUsuario();
                        break;

                    case 3:
                        com.thelibrary.controller.EmprestimoController.menuDevolverEmprestimoUsuario();
                        break;

                    case 0:
                        return;

                    default:
                        println("\n✗ Opção inválida!");
                }
            } catch (Exception e) {
                println("\n✗ Erro: " + e.getMessage());
            }
        }
    }

    private static void menuMinhasInteracoesUsuario() {
        while (true) {
            println("\n--- MINHAS INTERAÇÕES ---");
            println("1. Comentários");
            println("2. Avaliações");
            println("3. Sugestões");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1: menuComentariosUsuario(); break;
                case 2: menuAvaliacoes(); break;
                case 3: menuMinhasSugestoes(); break;
                case 0: return;
                default: println("\n✗ Opção inválida!");
            }
        }
    }

    // ------------------------
    // CRUDs existentes (mantive sua lógica, apenas organizando)
    // ------------------------
    // Usuários
    private static void cadastrarNovoUsuario() {
        try {
            println("\n--- CADASTRO DE USUÁRIO ---");
            String email = lerLinha("Email: ");
            String senha = lerLinha("Senha: ");
            String nome = lerLinha("Nome: ");
            String telefone = lerLinha("Telefone: ");

            usuarioService.cadastrarUsuario(email, senha, nome, telefone);
            println("\n✓ Cadastro realizado com sucesso! Agora você pode fazer login.");
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void cadastrarUsuario() {
        // mesma função que cadastrarNovoUsuario, mantida para compatibilidade com o menu
        cadastrarNovoUsuario();
    }

    private static void listarUsuarios() {
        try {
            println("\n--- LISTA DE USUÁRIOS ---");
            List<Usuario> usuarios = usuarioService.listarTodos();
            imprimirLista(usuarios);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarUsuarioPorId() {
        try {
            println("\n--- BUSCAR USUÁRIO ---");
            int id = lerInteiroPrompt("ID: ");
            Usuario usuario = usuarioService.buscarPorId(id);
            println("\n" + usuario);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void atualizarUsuario() {
        try {
            println("\n--- ATUALIZAR USUÁRIO ---");
            int id = lerInteiroPrompt("ID do usuário: ");
            String email = lerLinha("Novo Email (Enter para manter): ");
            String senha = lerLinha("Nova Senha (Enter para manter): ");
            String nome = lerLinha("Novo Nome (Enter para manter): ");
            String telefone = lerLinha("Novo Telefone (Enter para manter): ");

            Usuario usuario = usuarioService.atualizarUsuario(id,
                    email.isEmpty() ? null : email,
                    senha.isEmpty() ? null : senha,
                    nome.isEmpty() ? null : nome,
                    telefone.isEmpty() ? null : telefone);

            println("\n✓ Usuário atualizado: " + usuario);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void excluirUsuario() {
        try {
            println("\n--- EXCLUIR USUÁRIO ---");
            int id = lerInteiroPrompt("ID: ");
            usuarioService.excluirUsuario(id);
            println("\n✓ Usuário excluído com sucesso!");
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    // Bibliotecários
    private static void cadastrarNovoBibliotecario() {
        try {
            println("\n--- CADASTRO DE BIBLIOTECÁRIO ---");
            String email = lerLinha("Email: ");
            String senha = lerLinha("Senha: ");
            String nome = lerLinha("Nome: ");
            String telefone = lerLinha("Telefone: ");

            bibliotecarioService.cadastrarBibliotecario(email, senha, nome, telefone);
            println("\n✓ Cadastro realizado com sucesso! Agora você pode fazer login.");
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void cadastrarBibliotecario() {
        cadastrarNovoBibliotecario();
    }

    private static void listarBibliotecarios() {
        try {
            println("\n--- LISTA DE BIBLIOTECÁRIOS ---");
            List<Bibliotecario> bibliotecarios = bibliotecarioService.listarTodos();
            imprimirLista(bibliotecarios);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarBibliotecarioPorId() {
        try {
            println("\n--- BUSCAR BIBLIOTECÁRIO ---");
            int id = lerInteiroPrompt("ID: ");
            Bibliotecario biblio = bibliotecarioService.buscarPorId(id);
            println("\n" + biblio);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void atualizarBibliotecario() {
        try {
            println("\n--- ATUALIZAR BIBLIOTECÁRIO ---");
            int id = lerInteiroPrompt("ID do bibliotecário: ");
            String email = lerLinha("Novo Email (Enter para manter): ");
            String senha = lerLinha("Nova Senha (Enter para manter): ");
            String nome = lerLinha("Novo Nome (Enter para manter): ");
            String telefone = lerLinha("Novo Telefone (Enter para manter): ");

            Bibliotecario biblio = bibliotecarioService.atualizarBibliotecario(id,
                    email.isEmpty() ? null : email,
                    senha.isEmpty() ? null : senha,
                    nome.isEmpty() ? null : nome,
                    telefone.isEmpty() ? null : telefone);

            println("\n✓ Bibliotecário atualizado: " + biblio);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void excluirBibliotecario() {
        try {
            println("\n--- EXCLUIR BIBLIOTECÁRIO ---");
            int id = lerInteiroPrompt("ID: ");
            bibliotecarioService.excluirBibliotecario(id);
            println("\n✓ Bibliotecário excluído com sucesso!");
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    // Livros (mantive sua lógica)
    private static void cadastrarLivro() {
        try {
            println("\n--- CADASTRAR LIVRO ---");
            String titulo = lerLinha("Título: ");
            String autor = lerLinha("Autor: ");
            String status = lerLinha("Status (Disponível/Emprestado/Reservado): ");
            String editora = lerLinha("Editora: ");
            Date dataPublicacao = lerDataOptional("Data de Publicação (dd/MM/yyyy): ");

            Livro livro = livroService.cadastrarLivro(titulo, autor, status, editora, dataPublicacao);
            println("\n✓ Livro cadastrado: " + livro);
        } catch (ParseException e) {
            println("\n✗ Erro: Data inválida. Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void listarLivros() {
        try {
            println("\n--- LISTA DE LIVROS ---");
            List<Livro> livros = livroService.listarTodos();
            imprimirLista(livros);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarLivroPorId() {
        try {
            println("\n--- BUSCAR LIVRO ---");
            int id = lerInteiroPrompt("ID: ");
            Livro livro = livroService.buscarPorId(id);
            println("\n" + livro);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarLivroPorEditora() {
        try {
            println("\n--- BUSCAR LIVRO POR EDITORA ---");
            String editora = lerLinha("Editora: ");
            List<Livro> livros = livroService.buscarPorEditora(editora);
            imprimirLista(livros);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarLivroPorAutor() {
        try {
            println("\n--- BUSCAR LIVRO POR AUTOR ---");
            String autor = lerLinha("Autor: ");
            List<Livro> livros = livroService.buscarPorAutor(autor);
            imprimirLista(livros);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void atualizarLivro() {
        try {
            println("\n--- ATUALIZAR LIVRO ---");
            int id = lerInteiroPrompt("ID do livro: ");
            String titulo = lerLinha("Novo Título (Enter para manter): ");
            String autor = lerLinha("Novo Autor (Enter para manter): ");
            String status = lerLinha("Novo Status (Enter para manter): ");
            String editora = lerLinha("Nova Editora (Enter para manter): ");
            Date dataPublicacao = lerDataOptional("Nova Data de Publicação (dd/MM/yyyy) (Enter para manter): ");

            Livro livro = livroService.atualizarLivro(id,
                    titulo.isEmpty() ? null : titulo,
                    autor.isEmpty() ? null : autor,
                    status.isEmpty() ? null : status,
                    editora.isEmpty() ? null : editora,
                    dataPublicacao);

            String autorAcao = bibliotecarioLogado != null ? bibliotecarioLogado.getNome() : "Sistema";
            println("\n✓ Livro atualizado por: " + autorAcao);
            println("  " + livro);
        } catch (ParseException e) {
            println("\n✗ Erro: Data inválida. Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void excluirLivro() {
        try {
            println("\n--- EXCLUIR LIVRO ---");
            int id = lerInteiroPrompt("ID: ");
            livroService.excluirLivro(id);
            String autorAcao = bibliotecarioLogado != null ? bibliotecarioLogado.getNome() : "Sistema";
            println("\n✓ Livro excluído por: " + autorAcao);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    // Artigos
    private static void cadastrarArtigo() {
        try {
            println("\n--- CADASTRAR ARTIGO ---");
            String titulo = lerLinha("Título: ");
            String autor = lerLinha("Autor: ");
            String status = lerLinha("Status (Disponível/Emprestado/Reservado): ");
            String revista = lerLinha("Revista: ");
            String revisor = lerLinha("Revisor: ");
            Date dataRevisao = lerDataOptional("Data de Revisão (dd/MM/yyyy): ");

            Artigo artigo = artigoService.cadastrarArtigo(titulo, autor, status, revista, revisor, dataRevisao);
            String autorAcao = bibliotecarioLogado != null ? bibliotecarioLogado.getNome() : "Sistema";
            println("\n✓ Artigo cadastrado por: " + autorAcao);
            println("  " + artigo);
        } catch (ParseException e) {
            println("\n✗ Erro: Data inválida. Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void listarArtigos() {
        try {
            println("\n--- LISTA DE ARTIGOS ---");
            List<Artigo> artigos = artigoService.listarTodos();
            imprimirLista(artigos);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarArtigoPorId() {
        try {
            println("\n--- BUSCAR ARTIGO ---");
            int id = lerInteiroPrompt("ID: ");
            Artigo artigo = artigoService.buscarPorId(id);
            println("\n" + artigo);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarArtigoPorRevista() {
        try {
            println("\n--- BUSCAR ARTIGO POR REVISTA ---");
            String revista = lerLinha("Revista: ");
            List<Artigo> artigos = artigoService.buscarPorRevista(revista);
            imprimirLista(artigos);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarArtigoPorRevisor() {
        try {
            println("\n--- BUSCAR ARTIGO POR REVISOR ---");
            String revisor = lerLinha("Revisor: ");
            List<Artigo> artigos = artigoService.buscarPorRevisor(revisor);
            imprimirLista(artigos);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void atualizarArtigo() {
        try {
            println("\n--- ATUALIZAR ARTIGO ---");
            int id = lerInteiroPrompt("ID do artigo: ");
            String titulo = lerLinha("Novo Título (Enter para manter): ");
            String autor = lerLinha("Novo Autor (Enter para manter): ");
            String status = lerLinha("Novo Status (Enter para manter): ");
            String revista = lerLinha("Nova Revista (Enter para manter): ");
            String revisor = lerLinha("Novo Revisor (Enter para manter): ");
            Date dataRevisao = lerDataOptional("Nova Data de Revisão (dd/MM/yyyy) (Enter para manter): ");

            Artigo artigo = artigoService.atualizarArtigo(id,
                    titulo.isEmpty() ? null : titulo,
                    autor.isEmpty() ? null : autor,
                    status.isEmpty() ? null : status,
                    revista.isEmpty() ? null : revista,
                    revisor.isEmpty() ? null : revisor,
                    dataRevisao);

            String autorAcao = bibliotecarioLogado != null ? bibliotecarioLogado.getNome() : "Sistema";
            println("\n✓ Artigo atualizado por: " + autorAcao);
            println("  " + artigo);
        } catch (ParseException e) {
            println("\n✗ Erro: Data inválida. Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void excluirArtigo() {
        try {
            println("\n--- EXCLUIR ARTIGO ---");
            int id = lerInteiroPrompt("ID: ");
            artigoService.excluirArtigo(id);
            String autorAcao = bibliotecarioLogado != null ? bibliotecarioLogado.getNome() : "Sistema";
            println("\n✓ Artigo excluído por: " + autorAcao);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    // Revistas
    private static void cadastrarRevista() {
        try {
            println("\n--- CADASTRAR REVISTA ---");
            String titulo = lerLinha("Título: ");
            String autor = lerLinha("Autor: ");
            String status = lerLinha("Status (Disponível/Emprestado/Reservado): ");
            String nomeRevista = lerLinha("Nome da Revista: ");
            String numeroEdicao = lerLinha("Número da Edição: ");
            Date dataEdicao = lerDataOptional("Data da Edição (dd/MM/yyyy): ");

            Revista revista = revistaService.cadastrarRevista(titulo, autor, status, nomeRevista, numeroEdicao, dataEdicao);
            String autorAcao = bibliotecarioLogado != null ? bibliotecarioLogado.getNome() : "Sistema";
            println("\n✓ Revista cadastrada por: " + autorAcao);
            println("  " + revista);
        } catch (ParseException e) {
            println("\n✗ Erro: Data inválida. Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void listarRevistas() {
        try {
            println("\n--- LISTA DE REVISTAS ---");
            List<Revista> revistas = revistaService.listarTodas();
            imprimirLista(revistas);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarRevistaPorId() {
        try {
            println("\n--- BUSCAR REVISTA ---");
            int id = lerInteiroPrompt("ID: ");
            Revista revista = revistaService.buscarPorId(id);
            println("\n" + revista);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarRevistaPorNome() {
        try {
            println("\n--- BUSCAR REVISTA POR NOME ---");
            String nomeRevista = lerLinha("Nome da Revista: ");
            List<Revista> revistas = revistaService.buscarPorNomeRevista(nomeRevista);
            imprimirLista(revistas);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarRevistaPorNumeroEdicao() {
        try {
            println("\n--- BUSCAR REVISTA POR NÚMERO DE EDIÇÃO ---");
            String numeroEdicao = lerLinha("Número da Edição: ");
            List<Revista> revistas = revistaService.buscarPorNumeroEdicao(numeroEdicao);
            imprimirLista(revistas);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void atualizarRevista() {
        try {
            println("\n--- ATUALIZAR REVISTA ---");
            int id = lerInteiroPrompt("ID da revista: ");
            String titulo = lerLinha("Novo Título (Enter para manter): ");
            String autor = lerLinha("Novo Autor (Enter para manter): ");
            String status = lerLinha("Novo Status (Enter para manter): ");
            String nomeRevista = lerLinha("Novo Nome da Revista (Enter para manter): ");
            String numeroEdicao = lerLinha("Novo Número da Edição (Enter para manter): ");
            Date dataEdicao = lerDataOptional("Nova Data da Edição (dd/MM/yyyy) (Enter para manter): ");

            Revista revista = revistaService.atualizarRevista(id,
                    titulo.isEmpty() ? null : titulo,
                    autor.isEmpty() ? null : autor,
                    status.isEmpty() ? null : status,
                    nomeRevista.isEmpty() ? null : nomeRevista,
                    numeroEdicao.isEmpty() ? null : numeroEdicao,
                    dataEdicao);

            String autorAcao = bibliotecarioLogado != null ? bibliotecarioLogado.getNome() : "Sistema";
            println("\n✓ Revista atualizada por: " + autorAcao);
            println("  " + revista);
        } catch (ParseException e) {
            println("\n✗ Erro: Data inválida. Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void excluirRevista() {
        try {
            println("\n--- EXCLUIR REVISTA ---");
            int id = lerInteiroPrompt("ID: ");
            revistaService.excluirRevista(id);
            String autorAcao = bibliotecarioLogado != null ? bibliotecarioLogado.getNome() : "Sistema";
            println("\n✓ Revista excluída por: " + autorAcao);
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    // ------------------------
    // UTILITÁRIOS / HELPERS
    // ------------------------
    private static void imprimirLista(List<?> lista) {
        if (lista == null || lista.isEmpty()) {
            println("Nenhum registro encontrado.");
            return;
        }
        lista.forEach(System.out::println);
    }

    private static boolean requireBibliotecario() {
        if (bibliotecarioLogado == null) {
            println("\n✗ Acesso negado! Apenas bibliotecários podem realizar esta ação.");
            return false;
        }
        return true;
    }

    private static boolean requireUsuarioLogado() {
        if (usuarioLogado == null) {
            println("\n✗ Ação disponível somente para usuários autenticados.");
            return false;
        }
        return true;
    }

    private static String lerLinha(String prompt) {
        print(prompt);
        return scanner.nextLine().trim();
    }

    private static int lerInteiroPrompt(String prompt) {
        print(prompt);
        return lerInteiro();
    }

    private static Date lerDataOptional(String prompt) throws ParseException {
        String dataStr = lerLinha(prompt);
        if (dataStr.isEmpty()) return null;
        return dateFormat.parse(dataStr);
    }

    private static int lerInteiro() {
        try {
            String linha = scanner.nextLine();
            return Integer.parseInt(linha.trim());
        } catch (Exception e) {
            return -1;
        }
    }

    private static void print(String s) {
        System.out.print(s);
    }

    private static void println(String s) {
        System.out.println(s);
    }
    private static void menuMinhasSugestoes() {
        while (true) {
            println("\n╔═══════════════════════════════════════╗");
            println("║         MINHAS SUGESTÕES              ║");
            println("╚═══════════════════════════════════════╝");
            println("1. Criar Nova Sugestão");
            println("2. Listar Minhas Sugestões");
            println("3. Editar Justificativa");
            println("4. Excluir Minha Sugestão");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1: criarNovaSugestao(); break;
                case 2: listarMinhasSugestoes(); break;
                case 3: editarJustificativaSugestao(); break;
                case 4: excluirMinhaSugestao(); break;
                case 0: return;
                default: println("\n✗ Opção inválida!");
            }
        }
    }

    private static void criarNovaSugestao() {
        try {
            println("\n--- CRIAR NOVA SUGESTÃO ---");
            println("Que tipo de material você deseja sugerir?");
            println("1. Livro");
            println("2. Artigo");
            println("3. Revista");
            print("Escolha: ");

            int tipo = lerInteiro();

            switch (tipo) {
                case 1: sugerirLivro(); break;
                case 2: sugerirArtigo(); break;
                case 3: sugerirRevista(); break;
                default: println("\n✗ Opção inválida!");
            }

        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }
    private static void sugerirLivro() {
        try {
            println("\n--- SUGERIR LIVRO ---");

            String titulo = lerLinha("Título do livro: ");
            String autor = lerLinha("Autor: ");
            String editora = lerLinha("Editora: ");
            Date dataPublicacao = lerDataOptional("Data de publicação (dd/MM/yyyy): ");

            println("\nPor que você sugere adicionar este livro ao catálogo?");
            String justificativa = lerLinha("Justificativa: ");

            if (justificativa.trim().isEmpty()) {
                println("\n✗ A justificativa não pode estar vazia!");
                return;
            }

            Sugestao sugestao = sugestaoService.sugerirLivro(
                    usuarioLogado.getId(),
                    justificativa,
                    titulo,
                    autor,
                    editora,
                    dataPublicacao
            );

            println("\n✓ Sugestão criada com sucesso!");
            println("  Título: " + sugestao.getTitulo());
            println("  Autor: " + sugestao.getAutor());
            println("  Editora: " + sugestao.getEditora());

        } catch (ParseException e) {
            println("\n✗ Erro: Data inválida. Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }
    private static void sugerirArtigo() {
        try {
            println("\n--- SUGERIR ARTIGO ---");

            String titulo = lerLinha("Título do artigo: ");
            String autor = lerLinha("Autor: ");
            String revista = lerLinha("Revista: ");
            String revisor = lerLinha("Revisor: ");
            Date dataRevisao = lerDataOptional("Data de revisão (dd/MM/yyyy): ");

            println("\nPor que você sugere adicionar este artigo ao catálogo?");
            String justificativa = lerLinha("Justificativa: ");

            if (justificativa.trim().isEmpty()) {
                println("\n✗ A justificativa não pode estar vazia!");
                return;
            }

            Sugestao sugestao = sugestaoService.sugerirArtigo(
                    usuarioLogado.getId(),
                    justificativa,
                    titulo,
                    autor,
                    revista,
                    revisor,
                    dataRevisao
            );

            println("\n✓ Sugestão criada com sucesso!");
            println("  Título: " + sugestao.getTitulo());
            println("  Autor: " + sugestao.getAutor());
            println("  Revista: " + sugestao.getRevista());


        } catch (ParseException e) {
            println("\n✗ Erro: Data inválida. Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void sugerirRevista() {
        try {
            println("\n--- SUGERIR REVISTA ---");

            String titulo = lerLinha("Título: ");
            String autor = lerLinha("Autor: ");
            String nomeRevista = lerLinha("Nome da revista: ");
            String numeroEdicao = lerLinha("Número da edição: ");
            Date dataEdicao = lerDataOptional("Data da edição (dd/MM/yyyy): ");

            println("\nPor que você sugere adicionar esta revista ao catálogo?");
            String justificativa = lerLinha("Justificativa: ");

            if (justificativa.trim().isEmpty()) {
                println("\n✗ A justificativa não pode estar vazia!");
                return;
            }

            Sugestao sugestao = sugestaoService.sugerirRevista(
                    usuarioLogado.getId(),
                    justificativa,
                    titulo,
                    autor,
                    nomeRevista,
                    numeroEdicao,
                    dataEdicao
            );

            println("\n✓ Sugestão criada com sucesso!");
            println("  Título: " + sugestao.getTitulo());
            println("  Autor: " + sugestao.getAutor());
            println("  Nome da revista: " + sugestao.getNomeRevista());

        } catch (ParseException e) {
            println("\n✗ Erro: Data inválida. Use o formato dd/MM/yyyy");
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void listarMinhasSugestoes() {
        try {
            println("\n--- MINHAS SUGESTÕES ---");
            List<Sugestao> sugestoes = sugestaoService.listarPorUsuario(usuarioLogado.getId());

            if (sugestoes.isEmpty()) {
                println("Você ainda não fez nenhuma sugestão.");
            } else {
                for (Sugestao s : sugestoes) {
                    println("─────────────────────────────────");
                    println("ID: " + s.getId());
                    println("Tipo: " + s.getTipoMaterial());
                    println("Título: " + s.getTitulo());
                    println("Autor: " + s.getAutor());

                    // Campos específicos por tipo
                    if ("Livro".equals(s.getTipoMaterial())) {
                        println("Editora: " + s.getEditora());
                        if (s.getDataPublicacao() != null) {
                            println("Data de publicação: " + dateFormat.format(s.getDataPublicacao()));
                        }
                    } else if ("Artigo".equals(s.getTipoMaterial())) {
                        println("Revista: " + s.getRevista());
                        println("Revisor: " + s.getRevisor());
                        if (s.getDataRevisao() != null) {
                            println("Data de revisão: " + dateFormat.format(s.getDataRevisao()));
                        }
                    } else if ("Revista".equals(s.getTipoMaterial())) {
                        println("Nome da revista: " + s.getNomeRevista());
                        println("Número da edição: " + s.getNumeroEdicao());
                        if (s.getDataEdicao() != null) {
                            println("Data da edição: " + dateFormat.format(s.getDataEdicao()));
                        }
                    }

                    println("Justificativa: " + s.getJustificativa());
                    println("Data da sugestão: " + dateFormat.format(s.getDataSugestao()));
                }
                println("─────────────────────────────────");
            }
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void editarJustificativaSugestao() {
        try {
            println("\n--- EDITAR JUSTIFICATIVA ---");

            // Primeiro listar as sugestões do usuário
            List<Sugestao> sugestoes = sugestaoService.listarPorUsuario(usuarioLogado.getId());

            if (sugestoes.isEmpty()) {
                println("Você não tem sugestões para editar.");
                return;
            }

            println("\nSuas sugestões:");
            for (Sugestao s : sugestoes) {
                println(s.getId() + " - " + s.getTitulo() + " (" + s.getTipoMaterial() + ")");
            }

            int id = lerInteiroPrompt("\nID da sugestão: ");

            Sugestao sugestao = sugestaoService.buscarPorId(id);

            println("\nJustificativa atual:");
            println(sugestao.getJustificativa());

            String novaJustificativa = lerLinha("\nNova justificativa: ");

            if (novaJustificativa.trim().isEmpty()) {
                println("\n✗ A justificativa não pode estar vazia!");
                return;
            }

            sugestaoService.atualizarJustificativa(id, usuarioLogado.getId(), novaJustificativa);

            println("\n✓ Justificativa atualizada com sucesso!");

        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void excluirMinhaSugestao() {
        try {
            println("\n--- EXCLUIR MINHA SUGESTÃO ---");

            // Primeiro listar as sugestões do usuário
            List<Sugestao> sugestoes = sugestaoService.listarPorUsuario(usuarioLogado.getId());

            if (sugestoes.isEmpty()) {
                println("Você não tem sugestões para excluir.");
                return;
            }

            println("\nSuas sugestões:");
            for (Sugestao s : sugestoes) {
                println(s.getId() + " - " + s.getTitulo() + " (" + s.getTipoMaterial() + ")");
            }

            int id = lerInteiroPrompt("\nID da sugestão: ");

            print("Tem certeza que deseja excluir? (S/N): ");
            String confirmacao = scanner.nextLine().trim().toUpperCase();

            if (confirmacao.equals("S")) {
                sugestaoService.excluirSugestaoDoUsuario(id, usuarioLogado.getId());
                println("\n✓ Sugestão excluída com sucesso!");
            } else {
                println("\n✗ Operação cancelada.");
            }

        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }
    private static void listarTodasAvaliacoes() {
        try {
            println("\n--- TODAS AS AVALIAÇÕES ---");
            List<Avaliacao> avaliacoes = avaliacaoService.listarTodas();
            if (avaliacoes.isEmpty()) {
                println("Nenhuma avaliação encontrada.");
            } else {
                for (Avaliacao a : avaliacoes) {
                    println("ID: " + a.getId() +
                            " | Usuário: " + a.getUsuario().getNome() +
                            " | Material: " + a.getMaterial().getTitulo() +
                            " | Nota: " + a.getNota() + "/5" +
                            " | Data: " + dateFormat.format(a.getDataAvaliacao()));
                    println("─────────────────────────────────");
                }
            }
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }
    private static void editarMinhaAvaliacao() {
        if (!requireUsuarioLogado()) return;

        try {
            println("\n--- EDITAR MINHA AVALIAÇÃO ---");

            List<Avaliacao> minhasAvaliacoes = avaliacaoService.listarPorUsuario(usuarioLogado.getId());

            if (minhasAvaliacoes.isEmpty()) {
                println("Você não tem avaliações para editar.");
                return;
            }

            println("\nSuas avaliações:");
            for (Avaliacao a : minhasAvaliacoes) {
                println("ID: " + a.getId() +
                        " | " + a.getMaterial().getTitulo() +
                        " | Nota: " + a.getNota() + "/5");
            }

            int avaliacaoId = lerInteiroPrompt("\nID da avaliação para editar: ");

            Avaliacao avaliacao = avaliacaoService.buscarPorId(avaliacaoId);

            if (!avaliacao.getUsuario().getId().equals(usuarioLogado.getId())) {
                println("\n✗ Você só pode editar suas próprias avaliações!");
                return;
            }

            // Exibir avaliação atual
            println("\n--- AVALIAÇÃO ATUAL ---");
            println("Material: " + avaliacao.getMaterial().getTitulo());
            println("Nota atual: " + avaliacao.getNota() + "/5");

            // Nova nota
            println("\n⭐ Nova avaliação (1-5):");
            int novaNota = lerInteiroPrompt("Sua nova nota (1-5): ");

            // ATUALIZAR APENAS A NOTA
            Avaliacao avaliacaoAtualizada = avaliacaoService.atualizarAvaliacao(
                    avaliacaoId, usuarioLogado.getId(), novaNota
            );

            println("\n✅ Avaliação atualizada com sucesso!");
            println("Nova nota: " + avaliacaoAtualizada.getNota() + "/5");

        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void buscarAvaliacaoPorId() {
        try {
            println("\n--- BUSCAR AVALIAÇÃO ---");
            int id = lerInteiroPrompt("ID da avaliação: ");
            Avaliacao avaliacao = avaliacaoService.buscarPorId(id);

            println("\n✓ Avaliação encontrada:");
            println("ID: " + avaliacao.getId());
            println("Usuário: " + avaliacao.getUsuario().getNome());
            println("Material: " + avaliacao.getMaterial().getTitulo() + " (" + avaliacao.getMaterial().obterTipo() + ")");
            println("Nota: " + avaliacao.getNota() + "/5");
            println("Data: " + dateFormat.format(avaliacao.getDataAvaliacao()));
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }
    private static void listarAvaliacoesPorMaterial() {
        try {
            println("\n--- AVALIAÇÕES POR MATERIAL ---");
            int materialId = lerInteiroPrompt("ID do material: ");

            List<Avaliacao> avaliacoes = avaliacaoService.listarPorMaterial(materialId);
            Double media = avaliacaoService.obterMediaNotasMaterial(materialId);
            Long total = avaliacaoService.obterNumeroAvaliacoesMaterial(materialId);

            println("\n📊 Estatísticas do Material:");
            println("Média de notas: " + media + "/5");
            println("Total de avaliações: " + total);

            if (avaliacoes.isEmpty()) {
                println("\nNenhuma avaliação encontrada para este material.");
            } else {
                println("\n--- AVALIAÇÕES ---");
                for (Avaliacao a : avaliacoes) {
                    println("👤 " + a.getUsuario().getNome() +
                            " | ⭐ " + a.getNota() + "/5" +
                            " | 📅 " + dateFormat.format(a.getDataAvaliacao()));
                    println("   ID Avaliação: " + a.getId());
                    println("─────────────────────────────────");
                }
            }
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void listarMinhasAvaliacoes() {
        if (!requireUsuarioLogado()) return;

        try {
            println("\n--- MINHAS AVALIAÇÕES ---");
            List<Avaliacao> avaliacoes = avaliacaoService.listarPorUsuario(usuarioLogado.getId());

            if (avaliacoes.isEmpty()) {
                println("Você ainda não fez nenhuma avaliação.");
            } else {
                for (Avaliacao a : avaliacoes) {
                    println("📚 " + a.getMaterial().getTitulo() + " (" + a.getMaterial().obterTipo() + ")" +
                            " | ⭐ " + a.getNota() + "/5" +
                            " | 📅 " + dateFormat.format(a.getDataAvaliacao()));
                    println("   ID Avaliação: " + a.getId());
                    println("─────────────────────────────────");
                }
            }
        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void criarNovaAvaliacao() {
        if (!requireUsuarioLogado()) return;

        try {
            println("\n--- NOVA AVALIAÇÃO ---");

            // Listar materiais disponíveis
            println("\n📚 Materiais Disponíveis:");

            // Listar Livros
            println("\n--- LIVROS ---");
            List<Livro> livros = livroService.listarTodos();
            for (Livro livro : livros) {
                println("ID: " + livro.getId() + " | " + livro.getTitulo());
            }

            // Listar Artigos
            println("\n--- ARTIGOS ---");
            List<Artigo> artigos = artigoService.listarTodos();
            for (Artigo artigo : artigos) {
                println("ID: " + artigo.getId() + " | " + artigo.getTitulo());
            }

            // Listar Revistas
            println("\n--- REVISTAS ---");
            List<Revista> revistas = revistaService.listarTodas();
            for (Revista revista : revistas) {
                println("ID: " + revista.getId() + " | " + revista.getTitulo());
            }

            if (livros.isEmpty() && artigos.isEmpty() && revistas.isEmpty()) {
                println("Nenhum material disponível para avaliação.");
                return;
            }

            int materialId = lerInteiroPrompt("\nID do material: ");

            // Verificar se já avaliou
            if (avaliacaoService.usuarioJaAvaliouMaterial(usuarioLogado.getId(), materialId)) {
                println("\n✗ Você já avaliou este material!");
                return;
            }

            println("\n⭐ Avalie de 1 a 5 estrelas:");
            int nota = lerInteiroPrompt("Sua nota (1-5): ");

            // CRIAR AVALIAÇÃO APENAS COM NOTA
            Avaliacao avaliacao = avaliacaoService.criarAvaliacao(
                    usuarioLogado.getId(), materialId, nota
            );

            println("\n✅ Avaliação criada com sucesso!");
            println("Material: " + avaliacao.getMaterial().getTitulo());
            println("Sua nota: " + avaliacao.getNota() + "/5");

        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void excluirMinhaAvaliacao() {
        if (!requireUsuarioLogado()) return;

        try {
            println("\n--- EXCLUIR MINHA AVALIAÇÃO ---");

            // Listar apenas as avaliações do usuário logado
            List<Avaliacao> minhasAvaliacoes = avaliacaoService.listarPorUsuario(usuarioLogado.getId());

            if (minhasAvaliacoes.isEmpty()) {
                println("Você não tem avaliações para excluir.");
                return;
            }

            println("\nSuas avaliações:");
            for (Avaliacao a : minhasAvaliacoes) {
                println("ID: " + a.getId() +
                        " | " + a.getMaterial().getTitulo() +
                        " | Nota: " + a.getNota() + "/5");
            }

            int avaliacaoId = lerInteiroPrompt("\nID da avaliação para excluir: ");

            // Verificar se a avaliação pertence ao usuário
            Avaliacao avaliacao = avaliacaoService.buscarPorId(avaliacaoId);
            if (!avaliacao.getUsuario().getId().equals(usuarioLogado.getId())) {
                println("\n✗ Você só pode excluir suas próprias avaliações!");
                return;
            }

            print("⚠️  Tem certeza que deseja excluir? (S/N): ");
            String confirmacao = scanner.nextLine().trim().toUpperCase();

            if (confirmacao.equals("S")) {
                avaliacaoService.excluirAvaliacaoDoUsuario(avaliacaoId, usuarioLogado.getId());
                println("\n✅ Avaliação excluída com sucesso!");
            } else {
                println("\n✗ Operação cancelada.");
            }

        } catch (Exception e) {
            println("\n✗ Erro: " + e.getMessage());
        }
    }

    private static void menuMultar() {
        try {
            com.thelibrary.controller.EmprestimoController.listarEmprestimos();
            println("\nEscreva o id do emprestimo para multar: ");
            int emprestimoId = lerInteiro();
            println("\nValor da multa: ");
            double valor = scanner.nextDouble();
            scanner.nextLine();
            String justificativa = lerLinha("Justificativa: ");
            multaService.registrarMulta(emprestimoId, valor, justificativa);
            println("\n Multa enviada com sucesso.");
        } catch (Exception e){
            println("\n Erro: " + e.getMessage());
        }
    }

    private static void listarTodasAsMultas(){
        List<Multa> multas = multaService.listarTodos();
        if (multas.isEmpty()) {
            println("Nenhuma multa encontrada");
            return;
        }
        for (Multa multa : multas) {
            println("ID: " + multa.getId() + " | valor: " + multa.getValor() + " | justificativa: " + multa.getJustificativa() + " | data de emissão: " + multa.getData_emissao() + " | id do emprestimo: " + (multa.getEmprestimo()).getId());
        }
    }

    private static void menuQuitarMulta() {
        try{
            println("\n Selecione a multa para quitar: ");
            multaService.listarTodos();
            int id = lerInteiroPrompt("Id da multa: ");
            multaService.quitarMulta(id);
            println("Multa quitada com sucesso.");

        }catch (Exception e){
            println("\n Erro: " + e.getMessage());
        }
    }

    private static void menuExcluirMulta(){
        try{
            println("\n Selecione a multa para excluir: ");
            multaService.listarTodos();
            int id = lerInteiroPrompt("Id da multa: ");
            multaService.removerMulta(id);
            println("Multa removida com sucesso.");

        }catch (Exception e){
            println("\n Erro: " + e.getMessage());
        }
    }

    private static void buscarMultaPorId() {
        println("\n --buscar multa por id--");
        int id = lerInteiroPrompt("Insira um id: ");
        Multa multa = multaService.buscarPorId(id);
        if (multa == null) {
            println("Nenhuma multa encontrada");
            return;
        }
        println("ID: " + multa.getId() + " | valor: " + multa.getValor() + " | justificativa: " + multa.getJustificativa() + " | data de emissão: " + multa.getData_emissao() + " | id do emprestimo: " + (multa.getEmprestimo()).getId());
    }

    private static void menuComentariosUsuario() {
        while (true) {
            println("\n--- MENU DE COMENTÁRIOS ---");
            println("1. Enviar comentário de livro");
            println("2. Visualizar meus comentários");
            println("3. Visualizar comentários de um livro");
            println("4. Editar comentário");
            println("0. Voltar");
            print("Escolha uma opção: ");

            int op = lerInteiro();
            switch (op) {
                case 1:
                    enviarComentario();
                    break;
                case 2:
                    visualizarComentariosUsuario();
                    break;
                case 3:
                    visualizarComentariosLivro();
                    break;
                case 4:
                    editarComentario();
                    break;
                case 0:
                    return;
                default:
                    println("\n✗ Opção inválida!");
            }
        }
    }
    private static void enviarComentario() {
        try {
            List<MaterialBibliografico> materiaisBibliograficos = materialBibliograficoService.listarTodos();
            for (MaterialBibliografico materialBibliografico : materiaisBibliograficos) {
                println("ID: " + materialBibliografico.getId() + " | " + materialBibliografico.getTitulo());
            }

            println("\nEscreva o id do Material Bibliográfico para enviar o comentário: ");
            int materialId = lerInteiro();
            println("\nComentário: ");
            String comentario = lerLinha("Insira o comentário: ");
            comentarioService.enviarComentario(materialId, comentario, usuarioLogado);
            println("\n Comentário enviado com sucesso.");
        } catch (Exception e){
            println("\n Erro: " + e.getMessage());
        }

    }

    private static void visualizarComentariosUsuario() {
        List<Comentario> comentarios = comentarioService.buscarComentariosUsuario(usuarioLogado);
        if (comentarios.isEmpty()) {
            println("Nenhum comentário seu encontrado");
            return;
        }
        println("\n Seus comentários");
        for (Comentario comentario : comentarios) {
            println("Material bibliográfico: " + (comentario.getMaterialBibliografico()).getTitulo() + " | Comentário: " + comentario.getComentario());
        }
    }

    private static void visualizarComentariosLivro() {
        List<MaterialBibliografico> materiaisBibliograficos = materialBibliograficoService.listarTodos();
        for (MaterialBibliografico materialBibliografico : materiaisBibliograficos) {
            println("ID: " + materialBibliografico.getId() + " | " + materialBibliografico.getTitulo());
        }
        int idLivro = lerInteiroPrompt("Escreva o id de um livro: ");
        List<Comentario> comentarios = comentarioService.buscarComentariosLivro(idLivro);
        if (comentarios.isEmpty()) {
            println("Nenhum comentário do livro encontrado");
            return;
        }
        println("\n Comentários do livro: " );
        for (Comentario comentario : comentarios) {
            println("ID: " + comentario.getId() + " | Material bibliográfico: " + (comentario.getMaterialBibliografico()).getTitulo() + " | Comentário: " + comentario.getComentario() + " | Usuário: " + (comentario.getUsuario()).getNome());
        }
    }

    private static void editarComentario() {
        try {
            List<Comentario> comentarios = comentarioService.buscarComentariosUsuario(usuarioLogado);
            if (comentarios.isEmpty()) {
                println("Nenhum comentário seu encontrado");
                return;
            }
            println("\n Seus comentários");
            for (Comentario comentario : comentarios) {
                println("ID: " + comentario.getId() + " | Material bibliográfico: " + (comentario.getMaterialBibliografico()).getTitulo() + " | Comentário: " + comentario.getComentario());
            }
            int idComentario = lerInteiroPrompt("\nInsira o id do comentario para editar a mensagem: ");
            String comentario = lerLinha("Insira o novo comentário: ");
            comentarioService.editarComentario(idComentario, comentario);
            println("Comentário alterado com sucesso!");
        }catch (Exception e){
            println("\n Erro: " + e.getMessage());
        }
    }

    private static void buscarComentarioId() {
        println("\n --buscar Comentário por id--");
        int id = lerInteiroPrompt("Insira um id: ");
        Comentario comentario = comentarioService.buscarComentarioId(id);
        if (comentario == null) {
            println("Nenhum comentário encontrado");
            return;
        }
        println("ID: " + comentario.getId() + " | Material bibliográfico: " + (comentario.getMaterialBibliografico()).getTitulo() + " | Comentário: " + comentario.getComentario());
    }

    private static void removerComentarioId() {
        try{
            println("\n Selecione um comentário para excluir: ");
            visualizarComentariosLivro();
            int id = lerInteiroPrompt("Id do comentário para excluir: ");
            comentarioService.removerComentario(id);
            println("Comentário removido com sucesso.");

        }catch (Exception e){
            println("\n Erro: " + e.getMessage());
        }
    }
}
