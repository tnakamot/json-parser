package com.github.tnakamot.jscdg.definition.keyword;

import com.github.tnakamot.jscdg.definition.JSONDataType;

public class StringKeyword extends Keyword {
    public StringKeyword(String keyword) {
        super(keyword, JSONDataType.STRING);
    }
}
