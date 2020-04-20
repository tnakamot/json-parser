package com.github.tnakamot.jscdg.definition.keyword;

import com.github.tnakamot.jscdg.definition.JSONDataType;

public class NumberKeyword extends Keyword {
    public NumberKeyword(String keyword) {
        super(keyword, JSONDataType.NUMBER);
    }
}
