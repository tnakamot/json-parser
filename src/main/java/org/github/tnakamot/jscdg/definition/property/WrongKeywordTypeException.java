package org.github.tnakamot.jscdg.definition.property;

import org.github.tnakamot.jscdg.definition.keyword.Keyword;
import org.github.tnakamot.jscdg.definition.value.JSONValue;

public class WrongKeywordTypeException extends RuntimeException {
    public WrongKeywordTypeException(JSONProperty property,
                                     Keyword keyword,
                                     Object valObj) {
        super(
                "The type of the attribute value for keyword '" +
                        keyword.getKeyword() +
                        "' is supposed be " +
                        keyword.getType().getTypeName() +
                        ", but the actual type of the value is " +
                        valObj.getClass().getSimpleName() + "."
        );
    }

    public WrongKeywordTypeException(JSONProperty property,
                                     Keyword keyword,
                                     Object valObj,
                                     Throwable ex) {
        super(
                "The type of the attribute value for keyword '" +
                        keyword.getKeyword() +
                        "' is supposed be " +
                        keyword.getType().getTypeName() +
                        ", but the actual type of the value is " +
                        valObj.getClass().getSimpleName() + ".",
                ex
        );
    }
}
