# 🚀 Kanban Board Management System

Um sistema completo para gerenciamento de boards Kanban com persistência em banco de dados MySQL, desenvolvido em Java seguindo princípios de arquitetura limpa.

![Java](https://img.shields.io/badge/Java-17-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.0-orange)
![Maven](https://img.shields.io/badge/Maven-3.8.1-red)

## ✨ Funcionalidades Principais

- ✅ Criação e gestão de múltiplos boards
- 🃏 CRUD completo para cards
- 🚦 Movimentação de cards entre colunas
- 🔒 Sistema de bloqueio/desbloqueio de cards com histórico
- 📊 Geração de relatórios de bloqueios
- 🗄️ Validação de fluxo via triggers e stored procedures
- 🔄 Gestão inteligente de colunas especiais (Inicial, Final e Cancelamento)

## 🛠 Tecnologias Utilizadas

- **Java 17**
- **MySQL 8.0**
- **JDBC** para conexão com banco de dados
- **Maven** para gerenciamento de dependências
- **Design Pattern** Repository e Service Layer

## 📋 Pré-requisitos

- Java JDK 17+
- MySQL Server 8.0+
- Maven 3.8+
- Git

## 🚀 Instalação e Configuração

### 1. Clonar repositório
```bash
git clone https://github.com/FelipeDeMoraes19/Task-Board.git
cd Task-Board
```

### 2. Configurar Banco de Dados
```sql
CREATE DATABASE felipedb;
CREATE USER 'root'@'localhost' IDENTIFIED BY 'root';
GRANT ALL PRIVILEGES ON felipedb.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Executar Script SQL
```bash
mysql -u root -p felipedb < src/main/resources/schema.sql
```

### 4. Compilar e Executar
```bash
mvn clean install
mvn exec:java
```
