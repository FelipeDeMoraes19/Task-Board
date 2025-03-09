package com.exemplo.domain;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int id;
    private String name;
    private List<Column> columns;

    public Board() {
        this.columns = new ArrayList<>();
    }

    public Board(int id, String name) {
        this.id = id;
        this.name = name;
        this.columns = new ArrayList<>();
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

    public List<Column> getColumns() {
       return columns;
    }

    public void setColumns(List<Column> columns) {
       this.columns = columns;
    }
}
