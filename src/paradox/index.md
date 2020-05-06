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
        " { \"key1\": true, " +
        "   \"key2\": false," +
        "   \"key3\": null } ");
JSONValue root = jsText.parse();
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
    JSONValue value2 = rootObject.get("key2");
    JSONValue value3 = rootObject.get("key3");
}
```

@@@ index
* [Types](types.md)
* [Mutable and Immutable](immutable.md)
* [Load JSON text from various sources](json_source.md)
* [JSON source information](source_info.md)
* [Output JSON text](output.md)
* [Source Code](source.md)
* [Javadoc](javadoc.md)
@@@
