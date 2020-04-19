package org.github.tnakamot.jscdg.table;

public class TableCaption {
    private final String id;
    private final String display;

    public TableCaption(String id, String display) {
        this.id = id;
        this.display = ((display == null) ? "" : display);
    }

    public String getID() { return id; }
    public String getDisplay() { return display; }

    public boolean exist() {
        return (id != null && !id.isEmpty()) || !display.isEmpty();
    }
}
