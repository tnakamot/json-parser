package org.github.tnakamot.jscdg.definition.property;

import org.github.tnakamot.jscdg.definition.keyword.Keyword;

public class InvalidKeywordException extends RuntimeException {
    private final JSONProperty property;
    private final Keyword keyword;

    public InvalidKeywordException(JSONProperty property,
                                   Keyword keyword) {
        super("Keyword '" + keyword.getKeyword() + "' is not defined " +
                "for properties of '" + property.get(JSONProperty.TYPE) +
                "' type.");

        this.property = property;
        this.keyword = keyword;
    }

    public JSONProperty getProperty() { return property; }
    public Keyword getKeywor() { return keyword; }
}
