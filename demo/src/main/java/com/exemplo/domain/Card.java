package com.exemplo.domain;

import java.time.LocalDateTime;

public class Card {
    private int id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private boolean blocked;
    private int columnId;
    private LocalDateTime movedAt

    public Card() {
    }

    public Card(int id, String title, String description, LocalDateTime createdAt, boolean blocked) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.blocked = blocked;
    }

    public LocalDateTime getMovedAt() {
        return movedAt;
    }

    public void setMovedAt(LocalDateTime movedAt) {
        this.movedAt = movedAt;
    }

    public int getColumnId() {
        return columnId;
    }

    public void setColumnId(int columnId) {
        this.columnId = columnId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
       this.id = id;
    }

    public String getTitle() {
       return title;
    }

    public void setTitle(String title) {
       this.title = title;
    }

    public String getDescription() {
       return description;
    }

    public void setDescription(String description) {
       this.description = description;
    }

    public LocalDateTime getCreatedAt() {
       return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
       this.createdAt = createdAt;
    }

    public boolean isBlocked() {
       return blocked;
    }

    public void setBlocked(boolean blocked) {
       this.blocked = blocked;
    }
}
