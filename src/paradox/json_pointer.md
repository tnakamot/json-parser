# JSON Pointer

This library also implements JSON Pointer in accorance with 
[RFC 6901](https://tools.ietf.org/html/rfc6901).

To get a JSON value using a JSON Pointer, call
@extref[JSONText#evaluate(String)](javadoc:JSONText.html#evaluate(java.lang.String)).
Below is an example.

```java
    JSONText jsText = JSONText.fromString(
        "{" +
        "  \"key1\": \"value\", " +
        "  \"key2\": [3, 1, 4], " +
        "  \"key3\": true," +
        "  \"key4\": {\"key5\": \"hello!\" }" +
        "} ");
    JSONValue root = jsText.parse();

    JSONValueString val1 = (JSONValueString) jsText.evaluate("/key1");
    System.out.println(val1.value());

    JSONValueNumber val2 = (JSONValueNumber) jsText.evaluate("/key2/1");
    System.out.println(val2.toLong());

    JSONValueString val3 = (JSONValueString) jsText.evaluate("/key4/key5");
    System.out.println(val3.value());
```

@@@ note
Before your application calls
@extref[evaluate(String)](javadoc:JSONText.html#evaluate(java.lang.String))
or its variant, it needs to explicitly call @extref[parse()](javadoc:JSONText.html#parse())
or its variant and the JSON test must be parsed successfully.
 
This is the design choice to allow your application
code to separately handle possible exceptions caused by the JSON parser. Your application
typically needs to call @extref[parse()](javadoc:JSONText.html#parse()) only once and
may evaluate JSON Pointers many times after that. You do not want to handle exceptions
that may be caused by the JSON parser every time this library evaluates a JSON Pointer.

If you want to make sure the JSON text has been parsed, your application may call
@extref[isParsed()](javadoc:JSONText.html#isParsed()) and call
@extref[parse()](javadoc:JSONText.html#parse()) if it has not been parsed.
@@@

## URI Fragment Identifier Representation

[RFC 6901 - 6. URI Fragment Identifier Representation](https://tools.ietf.org/html/rfc6901#section-6)
allows us to represent a JSON Pointer in a URI fragment identifier, which is a text
starting from '#' (0x23). Normally, JSON Pointer looks like `/key1/5/key2`.
In URI fragment identifier, it turns to be `#/key1/5/key2`. However, RFC 6901 asks
the application programs and libraries to check the media type (e.g. `application/json`)
to judge if the JSON Pointer should be handled as a URI fragment identifier.

This library does not obtain the media type from the JSON text source because 
local file system does not typically provide it. Instead, your application programs
to judge if JSON Pointers should be handled a URI fragment identifier. Once judged,
pass `true` to the second argument of  
@extref[evaluate(String, boolean)](javadoc:JSONText.html#evaluate(java.lang.String,boolean)).
Then, this library considers the given JSON Pointer as a URI fragument identifier,
strips the first "#" (0x23) and evaluates the JSON Pointer in the normal way. 
