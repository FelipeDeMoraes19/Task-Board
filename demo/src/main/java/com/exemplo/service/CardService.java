package com.exemplo.service;

import com.exemplo.domain.Card;
import com.exemplo.domain.Column;
import com.exemplo.repository.CardRepository;
import com.exemplo.repository.ColumnRepository;
import com.exemplo.util.DatabaseConnectionUtil;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;

public class CardService {
    
    private final CardRepository cardRepository;
    private final ColumnRepository columnRepository;

    public CardService(CardRepository cardRepository, ColumnRepository columnRepository) {
        this.cardRepository = cardRepository;
        this.columnRepository = columnRepository;
    }

    public void moveCardToNextColumn(int cardId, int currentColumnId) {
        Card card = cardRepository.findById(cardId);
        if(card == null) throw new IllegalArgumentException("Card não encontrado");
        if(card.isBlocked()) throw new IllegalStateException("Card bloqueado!");
        
        List<Column> columns = columnRepository.findByBoardId(
            columnRepository.findById(currentColumnId).getBoardId()
        );
        
        int currentIndex = -1;
        for(int i = 0; i < columns.size(); i++) {
            if(columns.get(i).getId() == currentColumnId) {
                currentIndex = i;
                break;
            }
        }
        
        if(currentIndex == -1) throw new IllegalStateException("Coluna inválida");
        if(currentIndex >= columns.size() - 1) throw new IllegalStateException("Última coluna alcançada");
        
        Column nextColumn = columns.get(currentIndex + 1);
        if(nextColumn.getType().equals("Cancelamento")) {
            throw new IllegalStateException("Use a opção específica de cancelamento");
        }
        
        card.setColumnId(nextColumn.getId());
        card.setMovedAt(LocalDateTime.now());
        cardRepository.update(card);
        logMovement(cardId, currentColumnId, nextColumn.getId());
    }

    public void cancelCard(int cardId) {
        Card card = cardRepository.findById(cardId);
        if(card == null) throw new IllegalArgumentException("Card não encontrado");
        if(card.isBlocked()) throw new IllegalStateException("Card bloqueado!");
        
        Column currentColumn = columnRepository.findById(card.getColumnId());
        if(currentColumn.getType().equals("Final")) {
            throw new IllegalArgumentException("Não pode cancelar card da coluna final!");
        }
        
        List<Column> columns = columnRepository.findByBoardId(currentColumn.getBoardId());
        Column cancelamento = columns.stream()
            .filter(c -> c.getType().equals("Cancelamento"))
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("Coluna de cancelamento não encontrada"));
        
        card.setColumnId(cancelamento.getId());
        card.setMovedAt(LocalDateTime.now());
        cardRepository.update(card);
        logMovement(cardId, currentColumn.getId(), cancelamento.getId());
    }

    public void toggleBlockStatus(int cardId, String reason, boolean block) {
        Card card = cardRepository.findById(cardId);
        if(card == null) throw new IllegalArgumentException("Card não encontrado");
        
        card.setBlocked(block);
        cardRepository.update(card);
        logBlockStatus(cardId, reason, block);
    }

    private void logMovement(int cardId, int fromColumnId, int toColumnId) {
        String sql = "INSERT INTO card_movement (card_id, from_column_id, to_column_id, moved_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cardId);
            stmt.setInt(2, fromColumnId);
            stmt.setInt(3, toColumnId);
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void logBlockStatus(int cardId, String reason, boolean blocked) {
        String sql = "INSERT INTO card_block_history (card_id, blocked_status, reason, event_time) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cardId);
            stmt.setBoolean(2, blocked);
            stmt.setString(3, reason);
            stmt.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String generateBlockReport(int boardId) {
        StringBuilder report = new StringBuilder();
        String sql = "SELECT "
                   + "  c.title, "
                   + "  h1.event_time AS bloqueio_inicio, "
                   + "  h2.event_time AS bloqueio_fim, "
                   + "  h1.reason AS motivo_bloqueio, "
                   + "  h2.reason AS motivo_desbloqueio, "
                   + "  TIMEDIFF(h2.event_time, h1.event_time) AS duracao "
                   + "FROM card_block_history h1 "
                   + "LEFT JOIN card_block_history h2 "
                   + "  ON h1.card_id = h2.card_id "
                   + "  AND h2.event_time > h1.event_time "
                   + "  AND h2.blocked_status = false "
                   + "JOIN card c ON h1.card_id = c.id "
                   + "JOIN board_column col ON c.column_id = col.id "
                   + "WHERE h1.blocked_status = true "
                   + "AND col.board_id = ?";

        try (Connection conn = DatabaseConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, boardId);
            ResultSet rs = stmt.executeQuery();
            
            while(rs.next()) {
                report.append(String.format(
                    "Card: %s%nBloqueio: %s%nDesbloqueio: %s%nDuração: %s%n"
                    + "Motivo Bloqueio: %s%nMotivo Desbloqueio: %s%n%n",
                    rs.getString("title"),
                    rs.getTimestamp("bloqueio_inicio"),
                    rs.getTimestamp("bloqueio_fim") != null ? 
                    rs.getTimestamp("bloqueio_fim") : "N/A",
                    rs.getString("duracao") != null ? 
                    rs.getString("duracao") : "Em andamento",
                    rs.getString("motivo_bloqueio"),
                    rs.getString("motivo_desbloqueio") != null ? 
                    rs.getString("motivo_desbloqueio") : "N/A"
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return report.toString();
    }
}