package org.github.tnakamot.jscdg.definition.property;

import org.github.tnakamot.jscdg.definition.keyword.Keyword;

/**
 * This exception represents the case where a developer who uses
 * this library is trying to use the keyword that is not defined
 * for a specific property type.
 *
 * For example, "minimum" keyword is defined for the properties
 * of "integer" and "number" types, but it is not for "string"
 * type. If a developer tries to get an attribute of "minimum"
 * keyword, this library throws this exception.
 *
 * This exception does NOT indicate that the given JSON Schema
 * misses a certain keyword.
 */
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
