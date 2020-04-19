package org.github.tnakamot.jscdg.definition.keyword;

import org.github.tnakamot.jscdg.definition.JSONPrimitiveType;

public class IntegerKeyword extends Keyword {
    public IntegerKeyword(String keyword) {
        super(keyword, JSONPrimitiveType.INTEGER);
    }
}
