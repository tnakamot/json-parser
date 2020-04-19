package org.github.tnakamot.jscdg.definition.keyword;

import org.github.tnakamot.jscdg.definition.JSONPrimitiveType;

public class Keyword {
    private final String keyword;
    private final JSONPrimitiveType type;
    private final boolean allowArray;

    public Keyword(String keyword, JSONPrimitiveType type) {
        this(keyword, type, false);
    }

    public Keyword(String keyword, JSONPrimitiveType type, boolean allowArray) {
        if (keyword == null) {
            throw new NullPointerException("keyword cannot be null");
        } else if (keyword.isEmpty()) {
            throw new IllegalArgumentException("keyword cannot be empty");
        } else if (type == null) {
            throw new NullPointerException("type cannot be null");
        }

        this.keyword = keyword;
        this.type = type;
        this.allowArray = allowArray;
    }

    public String getKeyword() { return keyword; }
    public JSONPrimitiveType getType() { return type; }
    public boolean getAllowArray() { return allowArray; }

    @Override
    public String toString() { return keyword; }
}
