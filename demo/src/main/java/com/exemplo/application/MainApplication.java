package com.exemplo.application;

import com.exemplo.domain.Board;
import com.exemplo.domain.Card;
import com.exemplo.domain.Column;
import com.exemplo.repository.*;
import com.exemplo.service.BoardService;
import com.exemplo.service.CardService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class MainApplication {
    private static BoardService boardService;
    private static CardService cardService;
    private static ColumnRepository columnRepository;
    private static CardRepository cardRepository;

    public static void main(String[] args) {
        initializeDependencies();
        runMainMenu();
    }

    private static void initializeDependencies() {
        ColumnRepository columnRepo = new ColumnRepositoryImpl();
        BoardRepository boardRepo = new BoardRepositoryImpl();
        cardRepository = new CardRepositoryImpl();
        boardService = new BoardService(boardRepo, columnRepo);
        cardService = new CardService(cardRepository, columnRepo);
        columnRepository = columnRepo;
    }

    private static void runMainMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;
        
        while (running) {
            printMainMenu();
            String option = scanner.nextLine();
            
            switch (option) {
                case "1": createBoard(scanner); break;
                case "2": selectBoard(scanner); break;
                case "3": deleteBoards(scanner); break;
                case "4": running = false; break;
                default: System.out.println("Opção inválida!");
            }
        }
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n----- MENU PRINCIPAL -----");
        System.out.println("1. Criar novo board");
        System.out.println("2. Selecionar board");
        System.out.println("3. Excluir boards");
        System.out.println("4. Sair");
        System.out.print("Escolha: ");
    }

    private static void createBoard(Scanner scanner) {
        System.out.print("Nome do board: ");
        String name = scanner.nextLine();
        boardService.createBoard(name);
        System.out.println("Board criado com sucesso!");
    }

    private static void selectBoard(Scanner scanner) {
        List<Board> boards = boardService.listAllBoards();
        if(boards.isEmpty()) {
            System.out.println("Nenhum board cadastrado!");
            return;
        }
        boards.forEach(b -> System.out.println(b.getId() + " - " + b.getName()));
        System.out.print("ID do board: ");
        int boardId = Integer.parseInt(scanner.nextLine());
        showBoardMenu(boardId, scanner);
    }

    private static void showBoardMenu(int boardId, Scanner scanner) {
        boolean inBoardMenu = true;
        while (inBoardMenu) {
            System.out.println("\n=== BOARD MENU ===");
            System.out.println("1. Criar card");
            System.out.println("2. Listar cards");
            System.out.println("3. Mover card para próxima coluna");
            System.out.println("4. Cancelar card");
            System.out.println("5. Bloquear/Desbloquear");
            System.out.println("6. Relatório");
            System.out.println("7. Voltar");
            System.out.print("Escolha: ");
            
            String opcao = scanner.nextLine();
            switch (opcao) {
                case "1": createCard(boardId, scanner); break;
                case "2": listCards(boardId); break;
                case "3": moveToNextColumn(scanner); break;
                case "4": cancelCard(scanner); break;
                case "5": toggleBlock(scanner); break;
                case "6": generateReport(boardId); break;
                case "7": inBoardMenu = false; break;
                default: System.out.println("Opção inválida!");
            }
        }
    }

    private static void createCard(int boardId, Scanner scanner) {
        try {
            List<Column> columns = columnRepository.findByBoardId(boardId);
            Column firstColumn = columns.get(0);
            
            System.out.print("Título: ");
            String title = scanner.nextLine();
            System.out.print("Descrição: ");
            String desc = scanner.nextLine();
            
            Card newCard = new Card();
            newCard.setTitle(title);
            newCard.setDescription(desc);
            newCard.setCreatedAt(LocalDateTime.now());
            newCard.setMovedAt(LocalDateTime.now());
            newCard.setColumnId(firstColumn.getId());
            
            cardRepository.save(newCard);
            System.out.println("Card criado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao criar card: " + e.getMessage());
        }
    }

    private static void listCards(int boardId) {
        try {
            List<Column> columns = columnRepository.findByBoardId(boardId);
            if(columns.isEmpty()) {
                System.out.println("Nenhuma coluna encontrada!");
                return;
            }
            
            for (Column column : columns) {
                System.out.println("\n[" + column.getType() + "] " + column.getName());
                List<Card> cards = cardRepository.findByColumnId(column.getId());
                if(cards.isEmpty()) {
                    System.out.println("  - Nenhum card nesta coluna");
                } else {
                    cards.forEach(c -> System.out.printf("  %d - %s %s%n", 
                        c.getId(), c.getTitle(), c.isBlocked() ? "(BLOQUEADO)" : ""));
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar cards: " + e.getMessage());
        }
    }

    private static void moveToNextColumn(Scanner scanner) {
        try {
            System.out.print("ID do card: ");
            int cardId = Integer.parseInt(scanner.nextLine());
            System.out.print("ID da coluna atual: ");
            int currentColumnId = Integer.parseInt(scanner.nextLine());
            
            cardService.moveCardToNextColumn(cardId, currentColumnId);
            System.out.println("Card movido com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao mover card: " + e.getMessage());
        }
    }

    private static void cancelCard(Scanner scanner) {
        try {
            System.out.print("ID do card: ");
            int cardId = Integer.parseInt(scanner.nextLine());
            cardService.cancelCard(cardId);
            System.out.println("Card cancelado com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao cancelar card: " + e.getMessage());
        }
    }

    private static void toggleBlock(Scanner scanner) {
        try {
            System.out.print("ID do card: ");
            int cardId = Integer.parseInt(scanner.nextLine());
            System.out.print("Motivo: ");
            String motivo = scanner.nextLine();
            System.out.print("Bloquear (S/N)? ");
            boolean bloquear = scanner.nextLine().equalsIgnoreCase("S");
            
            cardService.toggleBlockStatus(cardId, motivo, bloquear);
            System.out.println("Status do card atualizado!");
        } catch (Exception e) {
            System.out.println("Erro ao alterar status: " + e.getMessage());
        }
    }

    private static void generateReport(int boardId) {
        try {
            String relatorio = cardService.generateBlockReport(boardId);
            System.out.println("\n=== RELATÓRIO DE BLOQUEIOS ===");
            System.out.println(relatorio);
        } catch (Exception e) {
            System.out.println("Erro ao gerar relatório: " + e.getMessage());
        }
    }

    private static void deleteBoards(Scanner scanner) {
        try {
            System.out.print("IDs dos boards para excluir (separados por vírgula): ");
            String[] ids = scanner.nextLine().split(",");
            
            for(String id : ids) {
                boardService.deleteBoard(Integer.parseInt(id.trim()));
            }
            System.out.println("Boards excluídos com sucesso!");
        } catch (Exception e) {
            System.out.println("Erro ao excluir boards: " + e.getMessage());
        }
    }
}