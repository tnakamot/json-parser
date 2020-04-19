package org.github.tnakamot.jscdg.definition.property;

import org.github.tnakamot.jscdg.definition.JSONPrimitiveType;
import org.github.tnakamot.jscdg.definition.keyword.*;
import org.github.tnakamot.jscdg.definition.value.*;
import org.json.simple.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public abstract class JSONProperty {
    protected final Map<Keyword, JSONValue> attributes;

    public static final StringKeyword TYPE  = new StringKeyword("type");
    public static final StringKeyword TITLE = new StringKeyword("title");
    public static final StringKeyword DESCRIPTION = new StringKeyword("description");

    private final String name;

    public JSONProperty(String name, JSONObject obj) {
        this.name = name;

        this.attributes = new HashMap<>();
        registerKeyword(TYPE);
        registerKeyword(TITLE);
        registerKeyword(DESCRIPTION);

        // Note: readAttributes(obj) is expected to be called at the end of
        //       the constructor of child classes.
        // readAttributes(obj);
    }

    public String getName() {
        return name;
    }

    protected void registerKeyword(Keyword keyword) {
        attributes.put(keyword, null);
    }

    protected void readAttributes(JSONObject obj) {
        try {
            // TODO: support array
            for (Keyword keyword: attributes.keySet()) {
                Object valObj = obj.get(keyword.getKeyword());

                try {
                    JSONValue val = (JSONValue) keyword.getType().getValueClass()
                            .getConstructor(Object.class)
                            .newInstance(valObj);
                    attributes.put(keyword, val);
                } catch (InvocationTargetException e) {
                    throw new WrongKeywordTypeException(this, keyword, valObj, e);
                }
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public Long get(IntegerKeyword keyword) {
        if (!attributes.containsKey(keyword)) {
            throw new InvalidKeywordException(this, keyword);
        }

        JSONValue val = attributes.get(keyword);
        if (val == null)
            return null;

        if (val instanceof JSONIntegerValue) {
            JSONIntegerValue intVal = (JSONIntegerValue) val;
            return intVal.getValue();
        } else {
            throw new WrongKeywordTypeException(this, keyword, val);
        }
    }

    public String get(StringKeyword keyword) {
        if (!attributes.containsKey(keyword)) {
            throw new InvalidKeywordException(this, keyword);
        }

        JSONValue val = attributes.get(keyword);
        if (val == null)
            return null;

        if (val instanceof JSONStringValue) {
            JSONStringValue strVal = (JSONStringValue) val;
            return strVal.getValue();
        } else {
            throw new WrongKeywordTypeException(this, keyword, val);
        }
    }

    public Boolean get(BooleanKeyword keyword) {
        if (!attributes.containsKey(keyword)) {
            throw new InvalidKeywordException(this, keyword);
        }

        JSONValue val = attributes.get(keyword);
        if (val == null)
            return null;

        if (val instanceof JSONBooleanValue) {
            JSONBooleanValue boolVal = (JSONBooleanValue) val;
            return boolVal.getValue();
        } else {
            throw new WrongKeywordTypeException(this, keyword, val);
        }
    }

    public Double get(NumberKeyword keyword) {
        if (!attributes.containsKey(keyword)) {
            throw new InvalidKeywordException(this, keyword);
        }

        JSONValue val = attributes.get(keyword);
        if (val == null)
            return null;

        if (val instanceof JSONNumberValue) {
            JSONNumberValue numVal = (JSONNumberValue) val;
            return numVal.getValue();
        } else {
            throw new WrongKeywordTypeException(this, keyword, val);
        }
    }
}
