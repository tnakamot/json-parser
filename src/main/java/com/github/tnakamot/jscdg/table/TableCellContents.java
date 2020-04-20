package com.github.tnakamot.jscdg.table;

public class TableCellContents {
    private final String[] contents;

    public TableCellContents(String contents) {
        this.contents = new String[]{ contents };
    }

    public TableCellContents(String[] contents) {
        this.contents = contents.clone();
    }

    public String[] getContents() {
        return contents.clone();
    }
}
