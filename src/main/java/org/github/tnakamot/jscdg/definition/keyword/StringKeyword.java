package org.github.tnakamot.jscdg.definition.keyword;

import org.github.tnakamot.jscdg.definition.JSONPrimitiveType;

public class StringKeyword extends Keyword {
    public StringKeyword(String keyword) {
        super(keyword, JSONPrimitiveType.STRING);
    }
}
