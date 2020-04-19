package org.github.tnakamot.jscdg.definition.keyword;

import org.github.tnakamot.jscdg.definition.JSONPrimitiveType;

public class IntegerArrayKeyword extends Keyword {
    public IntegerArrayKeyword(String keyword) {
        super(keyword, JSONPrimitiveType.INTEGER, true);
    }
}
