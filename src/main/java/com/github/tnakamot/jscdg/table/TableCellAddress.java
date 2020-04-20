package com.github.tnakamot.jscdg.table;

public class TableCellAddress {
    private final TableColumn column;
    private final TableRow row;

    public TableCellAddress(TableRow row, TableColumn column) {
        if (row == null) {
            throw new NullPointerException("row cannot be null");
        }
        if (column == null) {
            throw new NullPointerException("column cannot be null");
        }

        this.row = row;
        this.column = column;
    }

    public TableRow getRow() { return row; }
    public TableColumn getColumn() { return column; }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof TableCellAddress) {
            TableCellAddress addr = (TableCellAddress) obj;
            return this.row.equals(addr.row) &&
                    this.column.equals(addr.column);

        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "(" + row.toString() + ", " + column.toString() + ")";
    }

    @Override
    public int hashCode() {
        return row.hashCode() + column.hashCode();
    }
}
