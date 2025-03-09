package com.exemplo.application;

import java.util.Scanner;

public class MainApplication {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            System.out.println("----- MENU PRINCIPAL -----");
            System.out.println("1. Criar novo board");
            System.out.println("2. Selecionar board");
            System.out.println("3. Excluir boards");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    System.out.println("Saindo do programa...");
                    running = false;
                    break;
                default:
                    System.out.println("Opção inválida, tente novamente.");
            }
        }
        
        scanner.close();
    }
}
