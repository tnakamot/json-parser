# Java JSON Parser

This Java library provides a JSON parser. The parser reads a JSON text,
parses it in accordance with [RFC 8259](https://tools.ietf.org/html/rfc8259)
and creates a corresponding data structure with Java objects.

The main feature of this library is that it keeps the original JSON text information
in the Java objects. Therefore, for example, your application can show where
exactly (line number & column number) the JSON text has a problem if there is 
a semantic error. 

## Setup

Add the following dependency to your build file:

@@dependency[Maven,Gradle,sbt] {
  group="com.github.tnakamot"
  artifact="json-parser"
  version="$project.version$"
}

## Usage

Import the following packages in your Java source code.

```java
import com.github.tnakamot.json.JSONText;
import com.github.tnakamot.json.value.*;
```

Then, instantiate @extref[JSONText](javadoc:JSONText.html) and call
@extref[parse()](javadoc:JSONText.html#parse()).

```java
JSONText jsText = JSONText.fromString(
        " { \"key1\": \"value\", " +
        "   \"key2\": 1.53, " +
        "   \"key3\": true," +
        "   \"key4\": null } ");
JSONValue root = jsText.parse().root();
```

The root JSON value can be obtained as an instance of
@extref[JSONValue](javadoc:value/JSONValue.html).

Typically, the type of the root JSON value is "object". Check the type of the
root value, and cast it to @extref[JSONValueObject](javadoc:value/JSONValueObject.html).
Then, you will be able to get values as shown below.

```java
if (root.type() == JSONValueType.OBJECT) {
    JSONValueObject rootObject = (JSONValueObject) root;

    JSONValue value1 = rootObject.get("key1");
    if (value1.type() == JSONValueType.STRING) { 
        String value1Str = ((JSONValueString) value1).value();
        System.out.println(value1Str);
    }

    JSONValue value2 = rootObject.get("key2");
    if (value2.type() == JSONValueType.NUMBER) { 
        double value2Dbl = ((JSONValueNumber) value2).toDouble();
        System.out.println(value2Dbl);
    }

    JSONValue value3 = rootObject.get("key3");
    if (value3.type() == JSONValueType.BOOLEAN) {
        boolean value3Bool = ((JSONValueBoolean) value3).value();
        System.out.println(value3Bool);
    }

    JSONValue value4 = rootObject.get("key4");
    if (value4.type() == JSONValueType.NULL) {
        System.out.println("null");
    }
}
```

## More simple usage

If you already know the data structure and data types exactly, your Java program
can be concise using convenience getXxxx() methods like shown below.

```java
JSONText jsText = JSONText.fromString(
        " { \"key1\": \"value\", " +
        "   \"key2\": 3.14, " +
        "   \"key3\": false," +
        "   \"key4\": 1024," +
        "   \"key5\": [5, 1, 2], "+
        "   \"key6\": {\"key6-1\": 0} }");
JSONValueObject root = (JSONValueObject) jsText.parse().root();

String  value1Str = root.getString("key1");
System.out.println(value1Str);

double value2Dbl = root.getDouble("key2");
System.out.println(value2Dbl);

boolean value3Bool = root.getBoolean("key3");
System.out.println(value3Bool);

long value4Lng = root.getLong("key4");
System.out.println(value4Lng);

JSONValueArray value5Arr = root.getArray("key5");
System.out.println(value5Arr.getLong(0));
System.out.println(value5Arr.getLong(1));
System.out.println(value5Arr.getLong(2));

JSONValueObject value6Obj = root.getObject("key6");
System.out.println(value6Obj.getLong("key6-1"));
```

@@@ index
* [Types](types.md)
* [Mutable and Immutable](immutable.md)
* [Load JSON text from various sources](json_source.md)
* [JSON source information](source_info.md)
* [Output JSON text](output.md)
* [JSON Pointer](json_pointer.md)
* [Source Code](source.md)
* [Javadoc](javadoc.md)
@@@
