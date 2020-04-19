package org.github.tnakamot.jscdg.definition.keyword;

import org.github.tnakamot.jscdg.definition.JSONPrimitiveType;

public class NumberKeyword extends Keyword {
    public NumberKeyword(String keyword) {
        super(keyword, JSONPrimitiveType.NUMBER);
    }
}
