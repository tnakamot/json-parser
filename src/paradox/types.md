# Types

@extref[JSONValue](javadoc:value/JSONValue.html) is an abstract class, and
child classes represent actual JSON values. Call
@extref[type()](javadoc:value/JSONValue.html#type()) to get the type of the
JSON value you have. The returned type tells you to which dedicated class 
you can cast the instance of @extref[JSONValue](javadoc:value/JSONValue.html)
safely. The table below shows the correspondence between the type returned
by @extref[type()](javadoc:value/JSONValue.html#type()) and the class which
you can cast to.

| Type                                                       | Class                                                          |
| ---------------------------------------------------------- | -------------------------------------------------------------- |
| @extref[NULL](javadoc:value/JSONValueType.html#NULL)       | @extref[JSONValueNull](javadoc:value/JSONValueNull.html)       |
| @extref[BOOLEAN](javadoc:value/JSONValueType.html#BOOLEAN) | @extref[JSONValueBoolean](javadoc:value/JSONValueBoolean.html) |
| @extref[STRING](javadoc:value/JSONValueType.html#STRING)   | @extref[JSONValueString](javadoc:value/JSONValueString.html)   |
| @extref[NUMBER](javadoc:value/JSONValueType.html#NUMBER)   | @extref[JSONValueNumber](javadoc:value/JSONValueNumber.html)   |
| @extref[ARRAY](javadoc:value/JSONValueType.html#ARRAY)     | @extref[JSONValueArray](javadoc:value/JSONValueArray.html)     |
| @extref[OBJECT](javadoc:value/JSONValueType.html#OBJECT)   | @extref[JSONValueObject](javadoc:value/JSONValueObject.html)   |

Instead of checking the value returned by
@extref[type()](javadoc:value/JSONValue.html#type()), you can also use `instanceof`
operator.

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
to extract the string value.

# number

@extref[JSONValueNumber](javadoc:value/JSONValueNumber.html) holds a number value
as String. Unfortunately, no Java class in the standard Java library can represent 
all possible numeric values that the JSON number notation can express without
information loss.
 
[RFC 8259 - 6. Numbers](https://tools.ietf.org/html/rfc8259#section-6) explains
all possible numeric expressions in JSON. It allows an extremely large value
like 1E100000, and an extremely precise value like 3.14159265358979323846264338327950288.
Although the [RFC 8259](https://tools.ietf.org/html/rfc8259#section-6) allows libraries
and applications to set limits on the range and precision of numbers accepted,
this library does not set such limits to maximize interoperability. 
@extref[JSONValueNumber](javadoc:value/JSONValueNumber.html) of this library
holds each numeric value in a string as it appears in your original JSON text.

For your convenience, you can call
@extref[toDouble()](javadoc:value/JSONValueNumber.html#toDouble()) or
@extref[toLong()](javadoc:value/JSONValueNumber.html#toLong()) to get a corresponding
Java primitive value.

@extref[toDouble()](javadoc:value/JSONValueNumber.html#toDouble()) internally
calls @javadoc[Double.parseDouble(String)](java.lang.Double#parseDouble(java.lang.String)).
As far as the library author knows, this method does not throw an exception as long
as you pass a valid JSON numeric expression. However, you loose information if you
pass an extremely precise value, and you will get
@javadoc[POSITIVE_INFINITY](java.lang.Double#POSITIVE_INFINITY) or 
@javadoc[NEGATIVE_INFINITY](java.lang.Double#NEGATIVE_INFINITY) if you pass an extremely
large value.

@extref[toLong()](javadoc:value/JSONValueNumber.html#toLong()) tries to convert
the String representation of the JSON numeric value into long, but if it cannot
convert without loosing information, it throws
@javadoc[NumberFormatException](java.lang.NumberFormatException).
For example, `1.52e2` appears to be a floating point value, but it is actually
an integer `152`. Therefore, @extref[toLong()](javadoc:value/JSONValueNumber.html#toLong())
returns `152`. However, if there is a fractional part (e.g. `1.523e2`), or if 
the value is too large (e.g. `9223372036854775808`), this method throws 
@javadoc[NumberFormatException](java.lang.NumberFormatException).

# array

@extref[JSONValueArray](javadoc:value/JSONValueArray.html) holds an ordered sequence
of @extref[JSONValue](javadoc:value/JSONValue.html). It implements
@javadoc[List](java.util.List) interface,
so all values can be iterated using `for` statement, for example.

```java
JSONText jsText = JSONText.fromString(" [ true, false, \"abc\", 1.52, null ] ");
JSONValue root = jsText.parse().root();

if (root.type() == JSONValueType.ARRAY) {
    JSONValueArray rootArray = (JSONValueArray) root;

    for (JSONValue jsonValue : rootArray) {
        ...
    }
}
```

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
