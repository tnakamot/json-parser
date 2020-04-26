package com.github.tnakamot.jscdg.table;

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
    private Map<TableCellAddress, TableCellContents> cells = new HashMap<>();

    /*
     * Standard column IDs.
     */
    public static final String NAME_COLUMN = "name";
    public static final String TYPE_COLUMN = "type";
    public static final String DESC_COLUMN = "description";
    public static final String EXAMPLE_COLUMN = "example";
    public static final String DEFAULT_COLUMN = "default";
    public static final String REQUIRE_COLUMN = "require";

    public TableBuilder() {}

    public synchronized TableBuilder setCaption(TableCaption caption) {
        this.caption = caption;
        return this;
    }

    public synchronized TableBuilder setCaption(String id, String display) {
        return setCaption(new TableCaption(id, display));
    }

    synchronized public TableBuilder addColumn(TableColumn column) {
        // Check if the column with the same ID exits.
        for (TableColumn c: columns) {
            if (c.getID().equals(column.getID())) {
                throw new IllegalArgumentException("Duplicate column ID: " + column.getID());
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
                throw new IllegalArgumentException("Duplicate row ID: " + row.getID());
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
        cells.put(address, new TableCellContents(contents));

        return this;
    }

    synchronized public TableBuilder addCell(String rowId, String columnId, String[] contents) {
        TableRow row = findRow(rowId);
        TableColumn column = findColumn(columnId);

        TableCellAddress address = new TableCellAddress(row, column);
        cells.put(address, new TableCellContents(contents));

        return this;
    }

    /**
     * Convert the given ID string to something that follow the
     * requirements of ID string for HTML4. The requirements are
     *
     *      must begin with a letter ([A-Za-z]) and may be followed by
     *      any number of letters, digits ([0-9]), hyphens ("-"),
     *      underscores ("_"), colons (":"), and periods (".").
     *
     *       https://www.w3.org/TR/REC-html40/types.html#type-name
     *
     * This method does not guarantee the uniqueness after the conversion
     * because the number of characters that can be used by HTML4 is very
     * minimal. In principle, prohibited characters are simply removed.
     *
     *
     * @param id ID string of JSON object.
     * @return ID string which fulfills the HTML4 specification
     */
    private static String convertToValidHTML4ID(String id) {
        String ret;

        // JSON Object ID typically contains URL. Because slash '/'
        // is used as the path separator of the URL and it has
        // a meaning, replace it first.
        ret = id.replace("/", "_");

        // Remove all prohibited characters.
        ret = ret.replaceAll("[^A-Za-z:-_.]", "");

        // Prepend "json_" character if the string does not
        // start with a letter [A-Za-z].
        if (!ret.matches("^[A-Za-z]")) {
            ret = "json_" + ret;
        }

        // TODO: this is not good for doxygen. Reconsider another conversion.

        return ret;
    }

    synchronized private void writeHTMLCaption(Writer w)
            throws IOException {
        if (caption != null && caption.exist()) {
            w.append("<caption");
            if (caption.getID() != null && !caption.getID().isEmpty()) {
                w.append(" id=\"");
                w.append(convertToValidHTML4ID(caption.getID()));
                w.append("\"");
            }
            w.append(">");
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
            w.append("</th>\n");
        }
        w.append("</tr>\n");
    }

    synchronized private void writeHTMLRows(Writer w)
        throws IOException {
        for (TableRow row: rows) {
            w.append("<tr>\n");

            for (TableColumn column: columns) {
                w.append("  <td>");

                TableCellAddress address = new TableCellAddress(row, column);
                TableCellContents tcc = cells.get(address);
                if (tcc != null) {
                    String[] contents = tcc.getContents();

                    if (contents.length == 1 && contents[0] != null) {
                        w.append(StringEscapeUtils.escapeHtml4(contents[0]));
                    } else if (contents.length > 1) {
                        w.append("    <ul>\n");
                        for (String part : contents) {
                            w.append("      <li>");
                            w.append(part);
                            w.append("</li>\n");
                        }
                        w.append("</ul>");
                    }
                }

                w.append("</td>\n");
            }
            w.append("</tr>\n");
        }
    }

    synchronized public void writeHTML(Writer w)
            throws IOException {
        w.append("<table>\n");
        writeHTMLCaption(w);
        writeHTMLHeaders(w);
        writeHTMLRows(w);
        w.append("</table>\n");
    }
}
