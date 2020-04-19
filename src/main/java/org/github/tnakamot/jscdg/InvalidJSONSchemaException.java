package org.github.tnakamot.jscdg;

public class InvalidJSONSchemaException extends Exception {
    public InvalidJSONSchemaException(String msg) {
        super(msg);

        // TODO: add URL of the JSON Schema and line number
        //       to indicate where the invalid JSON syntax
        //       exists.
    }
}
