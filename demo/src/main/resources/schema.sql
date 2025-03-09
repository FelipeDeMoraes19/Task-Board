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
    DECLARE initial_count INT DEFAULT 0;
    DECLARE final_count INT DEFAULT 0;
    DECLARE cancel_count INT DEFAULT 0;
    DECLARE max_order INT DEFAULT 0;
    
    SELECT COUNT(*) INTO initial_count 
    FROM board_column 
    WHERE board_id = board_id 
    AND type = 'Inicial' 
    AND column_order = 1;

    SELECT MAX(column_order) INTO max_order 
    FROM board_column 
    WHERE board_id = board_id;

    SELECT COUNT(*) INTO final_count 
    FROM board_column 
    WHERE board_id = board_id 
    AND type = 'Final' 
    AND column_order = max_order - 1;

    SELECT COUNT(*) INTO cancel_count 
    FROM board_column 
    WHERE board_id = board_id 
    AND type = 'Cancelamento' 
    AND column_order = max_order;

    IF initial_count != 1 OR final_count != 1 OR cancel_count != 1 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Configuração inválida de colunas especiais: 
                            Deve haver 1 coluna Inicial (posição 1), 
                            1 coluna Final (penúltima posição) e 
                            1 coluna Cancelamento (última posição)';
    END IF;
END//
DELIMITER ;