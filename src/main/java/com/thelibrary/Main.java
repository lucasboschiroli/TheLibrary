package com.thelibrary;

import com.thelibrary.model.Usuario;
import com.thelibrary.model.Bibliotecario;
import com.thelibrary.service.UsuarioService;
import com.thelibrary.service.BibliotecarioService;
import com.thelibrary.util.JPAUtil;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final UsuarioService usuarioService = new UsuarioService();
    private static final BibliotecarioService bibliotecarioService = new BibliotecarioService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            System.out.println("=== THE LIBRARY SYSTEM ===\n");

            boolean continuar = true;
            while (continuar) {
                exibirMenuPrincipal();
                int opcao = lerInteiro();

                switch (opcao) {
                    case 1:
                        menuUsuarios();
                        break;
                    case 2:
                        menuBibliotecarios();
                        break;
                    case 0:
                        continuar = false;
                        System.out.println("\nEncerrando sistema...");
                        break;
                    default:
                        System.out.println("\nOpção inválida!");
                }
            }
        } finally {
            JPAUtil.close();
            scanner.close();
        }
    }

    private static void exibirMenuPrincipal() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Gerenciar Usuários");
        System.out.println("2. Gerenciar Bibliotecários");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    // ========== MENU USUÁRIOS ==========
    private static void menuUsuarios() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- MENU USUÁRIOS ---");
            System.out.println("1. Cadastrar Usuário");
            System.out.println("2. Listar Todos");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Atualizar Usuário");
            System.out.println("5. Excluir Usuário");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    cadastrarUsuario();
                    break;
                case 2:
                    listarUsuarios();
                    break;
                case 3:
                    buscarUsuarioPorId();
                    break;
                case 4:
                    atualizarUsuario();
                    break;
                case 5:
                    excluirUsuario();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("\nOpção inválida!");
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
    // ========== MENU BIBLIOTECÁRIOS ==========
    private static void menuBibliotecarios() {
        boolean voltar = false;
        while (!voltar) {
            System.out.println("\n--- MENU BIBLIOTECÁRIOS ---");
            System.out.println("1. Cadastrar Bibliotecário");
            System.out.println("2. Listar Todos");
            System.out.println("3. Buscar por ID");
            System.out.println("4. Atualizar Bibliotecário");
            System.out.println("5. Excluir Bibliotecário");
            System.out.println("0. Voltar");
            System.out.print("Escolha uma opção: ");

            int opcao = lerInteiro();

            switch (opcao) {
                case 1:
                    cadastrarBibliotecario();
                    break;
                case 2:
                    listarBibliotecarios();
                    break;
                case 3:
                    buscarBibliotecarioPorId();
                    break;
                case 4:
                    atualizarBibliotecario();
                    break;
                case 5:
                    excluirBibliotecario();
                    break;
                case 0:
                    voltar = true;
                    break;
                default:
                    System.out.println("\nOpção inválida!");
            }
        }
    }

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