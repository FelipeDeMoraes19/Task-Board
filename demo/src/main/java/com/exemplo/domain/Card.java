package com.exemplo.domain;

import java.time.LocalDateTime;

public class Card {
    private int id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private boolean blocked;

    public Card() {
    }

    public Card(int id, String title, String description, LocalDateTime createdAt, boolean blocked) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.blocked = blocked;
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
