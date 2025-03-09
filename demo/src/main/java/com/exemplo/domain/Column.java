package com.exemplo.domain;

import java.util.ArrayList;
import java.util.List;

public class Column {
    private int id;
    private String name;
    private String type; 
    private int columnOrder;
    private List<Card> cards;
    private int boardId;

    public Column() {
        this.cards = new ArrayList<>();
    }

    public Column(int id, String name, String type, int order) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.order = order;
        this.cards = new ArrayList<>();
    }

    public int getColumnOrder() {
        return columnOrder;
    }

    public void setColumnOrder(int columnOrder) {
        this.columnOrder = columnOrder;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
       this.id = id;
    }

    public String getName() {
       return name;
    }

    public void setName(String name) {
       this.name = name;
    }

    public String getType() {
       return type;
    }

    public void setType(String type) {
       this.type = type;
    }

    public List<Card> getCards() {
       return cards;
    }

    public void setCards(List<Card> cards) {
       this.cards = cards;
    }
}
