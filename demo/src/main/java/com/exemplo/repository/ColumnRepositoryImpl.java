package com.exemplo.repository;

import com.exemplo.domain.Column;
import com.exemplo.util.DatabaseConnectionUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ColumnRepositoryImpl implements ColumnRepository {

    @Override
    public Column save(Column column) {
        String sql = "INSERT INTO board_column (board_id, name, type, column_order) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setInt(1, column.getBoardId());
            stmt.setString(2, column.getName());
            stmt.setString(3, column.getType());
            stmt.setInt(4, column.getColumnOrder());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    column.setId(rs.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return column;
    }

    @Override
    public List<Column> findByBoardId(int boardId) {
        List<Column> columns = new ArrayList<>();
        String sql = "SELECT * FROM board_column WHERE board_id = ? ORDER BY column_order";
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, boardId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Column column = new Column();
                    column.setId(rs.getInt("id"));
                    column.setName(rs.getString("name"));
                    column.setType(rs.getString("type"));
                    column.setColumnOrder(rs.getInt("column_order"));
                    column.setBoardId(boardId);
                    columns.add(column);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return columns;
    }

    @Override
    public Column findById(int id) {
        Column column = null;
        String sql = "SELECT * FROM board_column WHERE id = ?";
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    column = new Column();
                    column.setId(rs.getInt("id"));
                    column.setName(rs.getString("name"));
                    column.setType(rs.getString("type"));
                    column.setColumnOrder(rs.getInt("column_order"));
                    column.setBoardId(rs.getInt("board_id"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return column;
    }

    @Override
    public void update(Column column) {
        String sql = "UPDATE board_column SET name = ?, type = ?, column_order = ? WHERE id = ?";
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, column.getName());
            stmt.setString(2, column.getType());
            stmt.setInt(3, column.getColumnOrder());
            stmt.setInt(4, column.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByBoardId(int boardId) {
        String sql = "DELETE FROM board_column WHERE board_id = ?";
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, boardId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}