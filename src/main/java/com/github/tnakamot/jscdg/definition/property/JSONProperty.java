package com.github.tnakamot.jscdg.definition.property;

import com.github.tnakamot.jscdg.definition.keyword.*;
import com.github.tnakamot.jscdg.definition.value.*;
import org.json.simple.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public abstract class JSONProperty {
    protected final Map<Keyword, JSONValue> attributes;

    public static final StringKeyword TYPE  = new StringKeyword("type");
    public static final StringKeyword TITLE = new StringKeyword("title");
    public static final StringKeyword DESCRIPTION = new StringKeyword("description");

    private final String name;
    private final JSONPropertyType type;

    public JSONProperty(String name, JSONObject obj)
            throws UnsupportedPropertyTypeException {
        this.name = name;
        this.type = readType(obj);
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

    /**
     * Supposed to be called at the end of the constructor of
     * child classes.
     *
     * @param obj
     * @throws UnsupportedPropertyTypeException
     */
    protected void readAttributes(JSONObject obj) {
        try {
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

    private JSONPropertyType readType(JSONObject obj)
            throws UnsupportedPropertyTypeException {
        Object valObj = obj.get("type");
        if (valObj == null) {
            throw new UnsupportedPropertyTypeException(name);
        } else if (!(valObj instanceof String)) {
            throw new UnsupportedPropertyTypeException(name, valObj.getClass());
        } else {
            String typeName = (String) valObj;
            try {
                return JSONPropertyType.fromName(typeName);
            } catch (IllegalArgumentException ex) {
                throw new UnsupportedPropertyTypeException(name, typeName);
            }
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

    public Long[] get(IntegerArrayKeyword keyword) {
        if (!attributes.containsKey(keyword)) {
            throw new InvalidKeywordException(this, keyword);
        }

        JSONValue val = attributes.get(keyword);
        if (val == null)
            return null;

        if (val instanceof JSONIntegerArrayValue) {
            JSONIntegerArrayValue array = (JSONIntegerArrayValue) val;
            return array.getValue();
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

    public Double[] get(NumberArrayKeyword keyword) {
        if (!attributes.containsKey(keyword)) {
            throw new InvalidKeywordException(this, keyword);
        }

        JSONValue val = attributes.get(keyword);
        if (val == null)
            return null;

        if (val instanceof JSONNumberArrayValue) {
            JSONNumberArrayValue array = (JSONNumberArrayValue) val;
            return array.getValue();
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

    public String[] get(StringArrayKeyword keyword) {
        if (!attributes.containsKey(keyword)) {
            throw new InvalidKeywordException(this, keyword);
        }

        JSONValue val = attributes.get(keyword);
        if (val == null)
            return null;

        if (val instanceof JSONStringArrayValue) {
            JSONStringArrayValue array = (JSONStringArrayValue) val;
            return array.getValue();
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

    public Boolean[] get(BooleanArrayKeyword keyword) {
        if (!attributes.containsKey(keyword)) {
            throw new InvalidKeywordException(this, keyword);
        }

        JSONValue val = attributes.get(keyword);
        if (val == null)
            return null;

        if (val instanceof JSONBooleanArrayValue) {
            JSONBooleanArrayValue array = (JSONBooleanArrayValue) val;
            return array.getValue();
        } else {
            throw new WrongKeywordTypeException(this, keyword, val);
        }
    }

    public static JSONProperty convert(String propertyName,
                                       JSONObject propertyObj) {
        Object typeObj = propertyObj.get("type");
        if (typeObj instanceof String) {
            String typeName = (String) typeObj;

            JSONPropertyType type = JSONPropertyType.fromName(typeName);
            try{
                return (JSONProperty) type.getPropertyClass()
                        .getConstructor(String.class, JSONObject.class)
                        .newInstance(propertyName, propertyObj);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }
}
