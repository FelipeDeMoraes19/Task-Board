-- Tabelas
CREATE TABLE IF NOT EXISTS board (
  id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS board_column (
  id INT AUTO_INCREMENT PRIMARY KEY,
  board_id INT NOT NULL,
  name VARCHAR(100) NOT NULL,
  type VARCHAR(50) NOT NULL,
  column_order INT NOT NULL,
  FOREIGN KEY (board_id) REFERENCES board(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS card (
  id INT AUTO_INCREMENT PRIMARY KEY,
  column_id INT NOT NULL,
  title VARCHAR(100) NOT NULL,
  description TEXT,
  created_at DATETIME NOT NULL,
  moved_at DATETIME,
  blocked TINYINT(1) NOT NULL DEFAULT 0,
  FOREIGN KEY (column_id) REFERENCES board_column(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS card_movement (
    id INT AUTO_INCREMENT PRIMARY KEY,
    card_id INT NOT NULL,
    from_column_id INT NOT NULL,
    to_column_id INT NOT NULL,
    moved_at DATETIME NOT NULL,
    FOREIGN KEY (card_id) REFERENCES card(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS card_block_history (
    id INT AUTO_INCREMENT PRIMARY KEY,
    card_id INT NOT NULL,
    blocked_status TINYINT(1) NOT NULL,
    reason TEXT NOT NULL,
    event_time DATETIME NOT NULL,
    FOREIGN KEY (card_id) REFERENCES card(id) ON DELETE CASCADE
);

DELIMITER //

CREATE PROCEDURE ValidateColumnOrder(IN board_id INT)
BEGIN
    DECLARE final_order INT;
    DECLARE cancel_order INT;
    
    SELECT column_order INTO final_order 
    FROM board_column 
    WHERE board_id = board_id 
    AND type = 'Final';
    
    SELECT column_order INTO cancel_order 
    FROM board_column 
    WHERE board_id = board_id 
    AND type = 'Cancelamento';
    
    IF final_order >= cancel_order THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Ordem inválida: Coluna Final deve vir antes do Cancelamento';
    END IF;
    
    IF (SELECT COUNT(*) FROM board_column WHERE board_id = board_id AND type = 'Final') > 1 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Apenas uma coluna Final permitida';
    END IF;
    
    IF (SELECT COUNT(*) FROM board_column WHERE board_id = board_id AND type = 'Cancelamento') > 1 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Apenas uma coluna Cancelamento permitida';
    END IF;
END//

CREATE TRIGGER AfterColumnInsert
AFTER INSERT ON board_column
FOR EACH ROW
BEGIN
    CALL ValidateColumnOrder(NEW.board_id);
END//

CREATE TRIGGER AfterColumnUpdate
AFTER UPDATE ON board_column
FOR EACH ROW
BEGIN
    CALL ValidateColumnOrder(NEW.board_id);
END//

DELIMITER ;