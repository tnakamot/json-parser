package org.github.tnakamot.jscdg.definition.keyword;

import org.github.tnakamot.jscdg.definition.JSONPrimitiveType;

public class BooleanArrayKeyword extends Keyword {
    public BooleanArrayKeyword(String keyword) {
        super(keyword, JSONPrimitiveType.BOOLEAN, true);
    }
}
