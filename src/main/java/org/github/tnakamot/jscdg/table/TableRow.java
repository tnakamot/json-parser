package org.github.tnakamot.jscdg.table;

public class TableRow {
    private final String id;

    public TableRow(String id) {
        if (id == null) {
            throw new NullPointerException("id cannot be null");
        } else if (id.isEmpty()) {
            throw new IllegalArgumentException("id cannot be an empty string");
        }

        this.id = id;
     }

    public String getID() { return id; }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof TableRow) {
            TableRow row = (TableRow) obj;
            return this.id.equals(row.id);
        } else {
            return false;
        }
    }

    @Override
    public String toString() { return id; }

    @Override
    public int hashCode() { return id.hashCode(); }
 }
