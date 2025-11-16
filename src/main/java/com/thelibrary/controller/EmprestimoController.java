package com.thelibrary.controller;

import com.thelibrary.model.Emprestimo;
import com.thelibrary.model.ItemEmprestimo;
import com.thelibrary.model.MaterialBibliografico;
import com.thelibrary.service.EmprestimoService;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static com.thelibrary.Main.usuarioLogado;

public class EmprestimoController {

    private static final EmprestimoService emprestimoService = new EmprestimoService();
    private static final Scanner scanner = new Scanner(System.in);

    // ================================
    //      AÇÕES DO BIBLIOTECÁRIO
    // ================================

    public static void registrarEmprestimo() {
        try {
            Integer usuarioId = lerInteger("ID do usuário: ");
            String materiaisStr = lerTexto("IDs dos materiais (separados por vírgula): ");

            List<Integer> materiaisIds = parseIds(materiaisStr);
            if (materiaisIds.isEmpty()) {
                println("\n✗ Nenhum ID válido informado.");
                return;
            }

            Emprestimo emp = emprestimoService.registrarEmprestimo(usuarioId, materiaisIds);
            println("\n✓ Empréstimo registrado com sucesso! ID: " + emp.getId());

        } catch (Exception e) {
            erro("Erro ao registrar empréstimo", e);
        }
    }

    public static void listarEmprestimos() {
        try {
            List<Emprestimo> lista = emprestimoService.listar();
            if (lista.isEmpty()) {
                println("\nNenhum empréstimo encontrado.");
                return;
            }

            println("\n===== LISTA DE EMPRÉSTIMOS =====");
            for (Emprestimo e : lista) {
                exibirEmprestimo(e);
            }

        } catch (Exception e) {
            erro("Erro ao listar empréstimos", e);
        }
    }

    public static void buscarEmprestimoPorId() {
        try {
            Integer id = lerInteger("ID do empréstimo: ");
            Emprestimo e = emprestimoService.buscarPorId(id);

            if (e == null) {
                println("\n✗ Empréstimo não encontrado.");
                return;
            }

            exibirEmprestimo(e);

        } catch (Exception ex) {
            erro("Erro ao buscar empréstimo", ex);
        }
    }

    public static void devolverEmprestimo() {
        try {
            Integer id = lerInteger("ID do empréstimo: ");
            emprestimoService.registrarDevolucao(id);
            println("\n✓ Devolução registrada com sucesso!");
        } catch (Exception e) {
            erro("Erro ao registrar devolução", e);
        }
    }

    public static void renovarEmprestimo() {
        try {
            Integer id = lerInteger("ID do empréstimo: ");
            emprestimoService.renovar(id);
            println("\n✓ Empréstimo renovado com sucesso!");
        } catch (Exception e) {
            erro("Erro ao renovar empréstimo", e);
        }
    }

    public static void excluirEmprestimo() {
        try {
            Integer id = lerInteger("ID do empréstimo: ");
            emprestimoService.excluir(id);
            println("\n✓ Empréstimo excluído!");
        } catch (Exception e) {
            erro("Erro ao excluir empréstimo", e);
        }
    }

    // ================================
    //        AÇÕES DO USUÁRIO
    // ================================

    public static void menuListarEmprestimosUsuario() {
        try {
            Integer id = usuarioLogado.getId();
            List<Emprestimo> lista = emprestimoService.listarPorUsuario(id);

            if (lista.isEmpty()) {
                println("\nVocê não possui empréstimos.");
                return;
            }

            println("\n--- SEUS EMPRÉSTIMOS ---");
            for (Emprestimo e : lista) {
                exibirEmprestimo(e);
            }

        } catch (Exception e) {
            erro("Erro ao listar seus empréstimos", e);
        }
    }

    public static void menuRenovarEmprestimoUsuario() {
        try {
            Integer id = lerInteger("ID do empréstimo: ");
            emprestimoService.renovarDoUsuario(id, usuarioLogado.getId());
            println("\n✓ Renovação realizada com sucesso!");
        } catch (Exception e) {
            erro("Erro ao renovar empréstimo", e);
        }
    }

    public static void menuDevolverEmprestimoUsuario() {
        try {
            Integer id = lerInteger("ID do empréstimo: ");
            emprestimoService.devolverDoUsuario(id, usuarioLogado.getId());
            println("\n✓ Devolução concluída!");
        } catch (Exception e) {
            erro("Erro ao devolver empréstimo", e);
        }
    }

    // ================================
    //      MÉTODOS AUXILIARES
    // ================================

    private static void exibirEmprestimo(Emprestimo e) {
        println("\n-----------------------------------");
        println("ID: " + e.getId());
        println("Usuário: " + safeNomeUsuario(e));
        println("Data empréstimo: " + safeDate(e.getDataEmprestimo()));
        println("Data prevista devolução: " + safeDate(e.getDataPrevistaDevolucao()));
        println("Data devolução: " + safeDate(e.getDataDevolucao()));
        println("Renovado: " + e.isRenovado());

        List<ItemEmprestimo> itens = safeItens(e);
        println("Itens: " + itens.size());
        for (ItemEmprestimo it : itens) {
            println(" - [" + it.getMaterial().getId() + "] " + it.getMaterial().getTitulo());
        }
    }

    private static void println(String msg) {
        System.out.println(msg);
    }

    private static void erro(String contexto, Exception e) {
        println("\n✗ " + contexto + ": " + e.getMessage());
    }

    private static String lerTexto(String msg) {
        System.out.print(msg);
        return scanner.nextLine().trim();
    }

    private static Integer lerInteger(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                println("✗ Digite apenas números.");
            }
        }
    }

    private static List<Integer> parseIds(String texto) {
        if (texto == null || texto.isBlank())
            return List.of();
        return Arrays.stream(texto.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    private static List<ItemEmprestimo> safeItens(Emprestimo e) {
        return e.getItens() == null ? List.of() : e.getItens();
    }

    private static String safeNomeUsuario(Emprestimo e) {
        return e.getUsuario() == null ? "(nulo)" : e.getUsuario().getNome();
    }

    private static String safeDate(java.util.Date d) {
        return d == null ? "-" : d.toString();
    }
}
