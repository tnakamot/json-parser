package com.github.tnakamot.jscdg.table;

public class TableColumn {
    private final String id;
    private final String header;

    public TableColumn(String id, String header) {
        if (id == null) {
            throw new NullPointerException("id cannot be null");
        } else if (id.isEmpty()) {
            throw new IllegalArgumentException("id cannot be an empty string");
        }

        this.id = id;
        this.header = ((header == null) ? "" : header);
    }

    public String getID() { return id; }
    public String getHeader() { return header; }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof TableColumn) {
            TableColumn column = (TableColumn) obj;
            return this.id.equals(column.id);
        } else {
            return false;
        }
    }

    @Override
    public String toString() { return id; }

    @Override
    public int hashCode() { return id.hashCode(); }
}
