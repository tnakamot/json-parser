package org.github.tnakamot.jscdg.table;

public class TableCaption {
    private final String id;
    private final String display;

    public TableCaption(String id, String display) {
        if (id == null) {
            throw new NullPointerException("id cannot be null");
        } else if (id.isEmpty()) {
            throw new IllegalArgumentException("id cannot be an empty string");
        }

        this.id = id;
        this.display = ((display == null) ? "" : display);
    }

    public String getID() { return id; }
    public String getDisplay() { return display; }
}
