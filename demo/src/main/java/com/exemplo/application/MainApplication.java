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
            System.out.println("3. Mover card");
            System.out.println("4. Bloquear/Desbloquear");
            System.out.println("5. Relatório");
            System.out.println("6. Voltar");
            System.out.print("Escolha: ");
            
            switch (scanner.nextLine()) {
                case "1": createCard(boardId, scanner); break;
                case "2": listCards(boardId); break;
                case "3": moveCard(scanner); break;
                case "4": toggleBlock(scanner); break;
                case "5": generateReport(boardId); break;
                case "6": inBoardMenu = false; break;
                default: System.out.println("Opção inválida!");
            }
        }
    }

    private static void createCard(int boardId, Scanner scanner) {
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
        System.out.println("Card criado!");
    }

    private static void listCards(int boardId) {
        List<Column> columns = columnRepository.findByBoardId(boardId);
        for (Column column : columns) {
            System.out.println("\nColuna: " + column.getName());
            List<Card> cards = cardRepository.findByColumnId(column.getId());
            cards.forEach(c -> System.out.println(" - " + c.getTitle()));
        }
    }


    private static void generateReport(int boardId) {
    }
}