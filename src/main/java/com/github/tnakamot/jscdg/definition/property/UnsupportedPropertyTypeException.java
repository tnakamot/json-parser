package com.github.tnakamot.jscdg.definition.property;

/**
 * Thrown when the given property type is not supported,
 * or the property type is missing.
 */
public class UnsupportedPropertyTypeException extends Exception {

    public UnsupportedPropertyTypeException(String propertyName,
                                            Class typeNameClass) {
        super("The type of the 'type' keyword of the property '" +
                propertyName + "' is supposed to be string, but it is " +
                typeNameClass.getName() + ".");
    }

    public UnsupportedPropertyTypeException(String propertyName) {
        super("Type is not defined for property '" + propertyName + "'.");
    }

    public UnsupportedPropertyTypeException(String propertyName,
                                            String typeName) {
        super("The type '" + typeName + "' (property: " + propertyName + ")" +
              " is not supported.");
    }
}
