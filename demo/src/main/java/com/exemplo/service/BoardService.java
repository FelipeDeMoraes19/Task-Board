package com.exemplo.service;

import com.exemplo.domain.Board;
import com.exemplo.repository.BoardRepository;
import java.util.List;

public class BoardService {
    
    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Board createBoard(String name) {
        Board board = new Board();
        board.setName(name);
        
        return boardRepository.save(board);
    }

    public List<Board> listAllBoards() {
        return boardRepository.findAll();
    }

    public Board findBoard(int id) {
        return boardRepository.findById(id);
    }

    public void deleteBoard(int id) {
        boardRepository.deleteById(id);
    }
}
