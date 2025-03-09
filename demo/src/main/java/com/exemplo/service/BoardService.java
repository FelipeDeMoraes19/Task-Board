package com.exemplo.service;

import com.exemplo.domain.Board;
import com.exemplo.domain.Column;
import com.exemplo.repository.BoardRepository;
import com.exemplo.repository.ColumnRepository;
import java.util.List;
import java.util.NoSuchElementException;

public class BoardService {
    private final BoardRepository boardRepository;
    private final ColumnRepository columnRepository;

    public BoardService(BoardRepository boardRepository, ColumnRepository columnRepository) {
        this.boardRepository = boardRepository;
        this.columnRepository = columnRepository;
    }

    public void createBoard(String name) {
        Board board = new Board();
        board.setName(name);
        boardRepository.save(board);

        createDefaultColumns(board.getId());
    }

    private void createDefaultColumns(int boardId) {
        String[][] defaultColumns = {
            {"Backlog", "Inicial"},
            {"Em Andamento", "Pendente"},
            {"Final", "Final"},
            {"Cancelado", "Cancelamento"}
        };

        for (int i = 0; i < defaultColumns.length; i++) {
            Column column = new Column();
            column.setBoardId(boardId);
            column.setName(defaultColumns[i][0]);
            column.setType(defaultColumns[i][1]);
            column.setColumnOrder(i);
            columnRepository.save(column);
        }
    }

    public void addColumn(int boardId, String name, String type, int order) {
        if (!type.equals("Pendente")) {
            throw new IllegalArgumentException("Somente colunas do tipo Pendente podem ser adicionadas");
        }

        Column column = new Column();
        column.setBoardId(boardId);
        column.setName(name);
        column.setType(type);
        column.setColumnOrder(order);
        columnRepository.save(column);

        reorganizeSpecialColumns(boardId, order);
    }

    private void reorganizeSpecialColumns(int boardId, int newOrder) {
        List<Column> columns = columnRepository.findByBoardId(boardId);
        
        Column finalCol = columns.stream()
            .filter(c -> c.getType().equals("Final"))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Coluna Final não encontrada"));

        Column cancelamento = columns.stream()
            .filter(c -> c.getType().equals("Cancelamento"))
            .findFirst()
            .orElseThrow(() -> new NoSuchElementException("Coluna Cancelamento não encontrada"));

        finalCol.setColumnOrder(newOrder + 1);
        cancelamento.setColumnOrder(newOrder + 2);

        columnRepository.update(finalCol);
        columnRepository.update(cancelamento);
    }

    public List<Board> listAllBoards() {
        return boardRepository.findAll();
    }

    public void deleteBoard(int boardId) {
        boardRepository.delete(boardId);
    }
}