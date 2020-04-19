package org.github.tnakamot.jscdg.definition.keyword;

import org.github.tnakamot.jscdg.definition.JSONPrimitiveType;

public class BooleanKeyword extends Keyword {
    public BooleanKeyword(String keyword) {
        super(keyword, JSONPrimitiveType.BOOLEAN);
    }
}
