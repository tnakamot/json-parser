package org.github.tnakamot.jscdg.definition.keyword;

import org.github.tnakamot.jscdg.definition.JSONPrimitiveType;

public class NumberArrayKeyword extends Keyword {
    public NumberArrayKeyword(String keyword) {
        super(keyword, JSONPrimitiveType.NUMBER, true);
    }
}
