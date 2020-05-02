# Types

@extref[JSONValue](javadoc:value/JSONValue.html) is an abstract class and
the realization of JSON value is done by child classes. The type of the
JSON value can be obtained by calling 
@extref[type()](javadoc:value/JSONValue.html#type()). Depending on the
returned type, the instance of @extref[JSONValue](javadoc:value/JSONValue.html)
can be safely casted in a dedicated class. The table below shows the correspondence
between the type and the class.

| Type                                                       | Class                                                          |
| ---------------------------------------------------------- | -------------------------------------------------------------- |
| @extref[NULL](javadoc:value/JSONValueType.html#NULL)       | @extref[JSONValueNull](javadoc:value/JSONValueNull.html)       |
| @extref[BOOLEAN](javadoc:value/JSONValueType.html#BOOLEAN) | @extref[JSONValueBoolean](javadoc:value/JSONValueBoolean.html) |
| @extref[STRING](javadoc:value/JSONValueType.html#STRING)   | @extref[JSONValueString](javadoc:value/JSONValueString.html)   |
| @extref[NUMBER](javadoc:value/JSONValueType.html#NUMBER)   | @extref[JSONValueNumber](javadoc:value/JSONValueNumber.html)   |
| @extref[ARRAY](javadoc:value/JSONValueType.html#ARRAY)     | @extref[JSONValueArray](javadoc:value/JSONValueArray.html)     |
| @extref[OBJECT](javadoc:value/JSONValueType.html#OBJECT)   | @extref[JSONValueObject](javadoc:value/JSONValueObject.html)   |

The following sections explain the details of each type.

# null

@extref[JSONValueNull](javadoc:value/JSONValueNull.html) has no value.

# boolean

@extref[JSONValueBoolean](javadoc:value/JSONValueBoolean.html) holds a boolean value.
Call @extref[value()](javadoc:value/JSONValueBoolean.html#value()) to extract the
boolean value.

# string

@extref[JSONValueString](javadoc:value/JSONValueString.html) holds an unescaped
string value. Call @extref[value()](javadoc:value/JSONValueString.html#value())
to extract the String value.

# number

@extref[JSONValueNumber](javadoc:value/JSONValueNumber.html) holds a number value
as String. Because no Java class in the standard Java library cannot represent 
all possible numeric values that the JSON number notation can express. For example,
JSON allows an extremely large value like 1E100000 or an extremely precise value
like 3.14159265358979323846264338327950288. No standard Java class or primitives
cannot represent those values without loosing information. Therefore, 
@extref[JSONValueNumber](javadoc:value/JSONValueNumber.html) holds the text that
represents the number as it appears in the JSON text.

You can call @extref[toDouble()](javadoc:value/JSONValueNumber.html#toDouble()) or
@extref[toLong()](javadoc:value/JSONValueNumber.html#toLong()) to get a corresponding Java
primitive value.

# array

@extref[JSONValueArray](javadoc:value/JSONValueArray.html) holds an ordered sequence
of @extref[JSONValue](javadoc:value/JSONValue.html). It implements
@javadoc[List](java.util.List) interface,
so all values can be iterated using `for` statement, for example.

```java
JSONText jsText = JSONText.fromString(" [ true, false, \"abc\", 1.52, null ] ");
JSONValue root = jsText.parse();

if (root.type() == JSONValueType.ARRAY) {
    JSONValueArray rootArray = (JSONValueArray) root;

    for (JSONValue jsonValue : rootArray) {
        ...
    }
}
```

@@@ note
Instances of @extref[JSONValueArray](javadoc:value/JSONValueArray.html) are immutable.
All methods of @javadoc[List](java.util.List) interface that may change the list
contents like @javadoc[add()](java.util.List#add(E)) result in 
@javadoc[UnsupportedOperationException](java.lang.UnsupportedOperationException).
@@@

# object

@extref[JSONValueObject](javadoc:value/JSONValueObject.html) holds a key-value
map. It implements @javadoc[Map](java.util.Map) interface.
The class of the key is @extref[JSONValueString](javadoc:value/JSONValueString.html)
and that of the value is @extref[JSONValue](javadoc:value/JSONValue.html). All
key-value pairs can be iterated as shown below.

```java
JSONText jsText = JSONText.fromString(
    " { \"key1\": true, " +
    "   \"key2\": false," +
    "   \"key3\": null } ");
JSONValue root = jsText.parse();

if (root.type() == JSONValueType.OBJECT) {
    JSONValueObject rootObj = (JSONValueObject) root;

    for (Map.Entry<JSONValueString, JSONValue> entry: rootObj.entrySet()) {
        JSONValueString key = entry.getKey();
        JSONValue value = entry.getValue();
        ...
    }
}
```

@@@ note
Instances of @extref[JSONValueObject](javadoc:value/JSONValueObject.html) are immutable.
All methods of @javadoc[Map](java.util.Map) interface that may change the 
contents like @javadoc[put()](java.util.Map#put(K,V)) result in 
@javadoc[UnsupportedOperationException](java.lang.UnsupportedOperationException).
@@@