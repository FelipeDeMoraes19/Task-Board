package com.exemplo.service;

import com.exemplo.domain.Board;
import com.exemplo.domain.Column;
import com.exemplo.repository.BoardRepository;
import com.exemplo.repository.ColumnRepository;
import java.util.List;

public class BoardService {
    
    private final BoardRepository boardRepository;
    private final ColumnRepository columnRepository;

    public BoardService(BoardRepository boardRepository, ColumnRepository columnRepository) {
        this.boardRepository = boardRepository;
        this.columnRepository = columnRepository;
    }

    public Board createBoard(String name) {
        Board board = new Board();
        board.setName(name);
        board = boardRepository.save(board);
        createDefaultColumns(board.getId());
        
        List<Column> columns = columnRepository.findByBoardId(board.getId());
        validateColumns(columns);
        
        return board;
    }

    private void createDefaultColumns(int boardId) {
        createColumn(boardId, "To Do", "Inicial", 1);
        createColumn(boardId, "Done", "Final", 2);
        createColumn(boardId, "Canceled", "Cancelamento", 3);
    }

    private void createColumn(int boardId, String name, String type, int order) {
        Column column = new Column();
        column.setBoardId(boardId);
        column.setName(name);
        column.setType(type);
        column.setColumnOrder(order);
        columnRepository.save(column);
    }

    private void validateColumns(List<Column> columns) {
        if(columns.size() < 3) {
            throw new IllegalStateException("O board deve ter pelo menos 3 colunas");
        }
        
        Column inicial = columns.get(0);
        Column finalCol = columns.get(columns.size()-2);
        Column cancelamento = columns.get(columns.size()-1);
        
        if(!inicial.getType().equals("Inicial") || 
           !finalCol.getType().equals("Final") || 
           !cancelamento.getType().equals("Cancelamento")) {
            throw new IllegalStateException("Ordem inválida das colunas especiais");
        }
    }

    public List<Board> listAllBoards() {
        return boardRepository.findAll();
    }

    public Board getBoardWithColumns(int boardId) {
        Board board = boardRepository.findById(boardId);
        if(board != null) {
            List<Column> columns = columnRepository.findByBoardId(boardId);
            board.setColumns(columns);
        }
        return board;
    }

    public void deleteBoard(int id) {
        columnRepository.deleteByBoardId(id);
        boardRepository.deleteById(id);
    }
}