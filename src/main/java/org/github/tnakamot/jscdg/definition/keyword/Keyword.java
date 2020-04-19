package org.github.tnakamot.jscdg.definition.keyword;

import org.github.tnakamot.jscdg.definition.JSONDataType;

public class Keyword {
    private final String keyword;
    private final JSONDataType type;

    public Keyword(String keyword, JSONDataType type) {
        if (keyword == null) {
            throw new NullPointerException("keyword cannot be null");
        } else if (keyword.isEmpty()) {
            throw new IllegalArgumentException("keyword cannot be empty");
        } else if (type == null) {
            throw new NullPointerException("type cannot be null");
        }

        this.keyword = keyword;
        this.type = type;
    }

    public String getKeyword() { return keyword; }
    public JSONDataType getType() { return type; }

    @Override
    public String toString() { return keyword; }
}
