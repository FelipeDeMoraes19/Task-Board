package com.exemplo.repository;

import com.exemplo.domain.Board;
import com.exemplo.util.DatabaseConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BoardRepositoryImpl implements BoardRepository {

    @Override
    public Board save(Board board) {
        String sql = "INSERT INTO board (name) VALUES (?)";
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, board.getName());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    board.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return board;
    }

    @Override
    public Board findById(int id) {
        Board board = null;
        String sql = "SELECT * FROM board WHERE id = ?";
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    board = new Board();
                    board.setId(rs.getInt("id"));
                    board.setName(rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return board;
    }

    @Override
    public List<Board> findAll() {
        List<Board> boards = new ArrayList<>();
        String sql = "SELECT * FROM board";
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Board board = new Board();
                board.setId(rs.getInt("id"));
                board.setName(rs.getString("name"));
                boards.add(board);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return boards;
    }

    @Override
    public void deleteById(int id) {
        String deleteColumns = "DELETE FROM board_column WHERE board_id = ?";
        String deleteBoard   = "DELETE FROM board WHERE id = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmtCols = conn.prepareStatement(deleteColumns);
                 PreparedStatement stmtBoard = conn.prepareStatement(deleteBoard)) {
                
                stmtCols.setInt(1, id);
                stmtCols.executeUpdate();

                stmtBoard.setInt(1, id);
                stmtBoard.executeUpdate();

                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
