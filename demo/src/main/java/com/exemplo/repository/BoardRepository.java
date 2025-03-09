package com.exemplo.repository;

import com.exemplo.domain.Board;
import java.util.List;

public interface BoardRepository {
    Board save(Board board);
    Board findById(int id);
    List<Board> findAll();
    void deleteById(int id);
}
