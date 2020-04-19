package org.github.tnakamot.jscdg.table;

import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableBuilder {
    private TableCaption caption = null;
    private List<TableColumn> columns = new ArrayList<>();
    private List<TableRow> rows = new ArrayList<>();
    private Map<TableCellAddress, String> cells = new HashMap<>();

    public TableBuilder() {}

    public synchronized TableBuilder setCaption(TableCaption caption) {
        this.caption = caption;
        return this;
    }

    synchronized public TableBuilder addColumn(TableColumn column) {
        // Check if the column with the same ID exits.
        for (TableColumn c: columns) {
            if (c.getID().equals(column.getID())) {
                throw new IllegalArgumentException("Duplicated column ID: " + column.getID());
            }
        }

        columns.add(column);
        return this;
    }

    synchronized public TableBuilder addColumn(String id, String display) {
        return addColumn(new TableColumn(id, display));
    }

    synchronized public TableBuilder addRow(TableRow row) {
        // Check if the row with the same ID exits.
        for (TableRow r: rows) {
            if (r.getID().equals(row.getID())) {
                throw new IllegalArgumentException("Duplicated row ID: " + row.getID());
            }
        }

        rows.add(row);
        return this;
    }

    synchronized public TableBuilder addRow(String id) {
        return addRow(new TableRow(id));
    }

    synchronized public TableColumn findColumn(String columnId) {
        for (TableColumn column: columns) {
            if (column.getID().equals(columnId)) {
                return column;
            }
        }
        throw new IllegalArgumentException("Column ID " + columnId + " does not exist.");
    }

    synchronized public TableRow findRow(String rowId) {
        for (TableRow row: rows) {
            if (row.getID().equals(rowId)) {
                return row;
            }
        }
        throw new IllegalArgumentException("Row ID " + rowId + " does not exist.");
    }

    synchronized public TableBuilder addCell(String rowId, String columnId, String contents) {
        TableRow row = findRow(rowId);
        TableColumn column = findColumn(columnId);

        TableCellAddress address = new TableCellAddress(row, column);
        cells.put(address, contents);

        return this;
    }

    synchronized private void writeHTMLCaption(Writer w)
            throws IOException {
        if (caption != null) {
            w.append("<caption id=\"");
            w.append(caption.getID()); // TODO: validate or escape as ID
            w.append("\">");
            w.append(StringEscapeUtils.escapeHtml4(caption.getDisplay()));
            w.append("</caption>\n");
        }
    }

    synchronized private void writeHTMLHeaders(Writer w)
            throws IOException {
        w.append("<tr>\n");
        for (TableColumn column: columns) {
            w.append("  <th>");
            w.append(StringEscapeUtils.escapeHtml4(column.getHeader()));
            w.append("</th>");
        }
        w.append("</tr>\n");
    }

    synchronized public void writeHTML(Writer w)
            throws IOException {
        w.append("<table>\n");
        writeHTMLCaption(w);
        writeHTMLHeaders(w);

        w.append("</table>\n");
    }
}
