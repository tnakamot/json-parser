package org.github.tnakamot.jscdg.definition.keyword;

import org.github.tnakamot.jscdg.definition.JSONPrimitiveType;

public class StringArrayKeyword extends Keyword {
    public StringArrayKeyword(String keyword) {
        super(keyword, JSONPrimitiveType.STRING, true);
    }
}
