# JSON source information

## Primitive Types

The advantage of this library is that each primitive JSON value (string, number,
boolean, null) hold where it originates from. To obtain that information, call 
@extref[token()](javadoc:value/JSONValuePrimitive.html#token()).

```java
JSONValueString jsStr = ...;
JSONToken token = jsStr.token();
```

An instance of @extref[JSONToken](javadoc:value/JSONToken.html) holds following
information. 

* @extref[beginningLocation()](javadoc:token/JSONToken.html#beginningLocation())
  returns an object which holds the line and column number of the first character
  of this JSON value within the JSON text.
* @extref[endLocation()](javadoc:token/JSONToken.html#endLocation())
  returns an object which holds the line and column number of the character
  of the last character of this JSON value within the JSON text.
* @extref[source()](javadoc:token/JSONToken.html#source()) returns an instance of
  @extref[JSONText](javadoc:JSONText.html) from which the parser extracted the JSON value.
   * @extref[JSONText#name()](javadoc:JSONText.html#name()) holds the short name
     of the JSON text. In many cases, it returns a file name.
   * @extref[JSONText#uri()](javadoc:JSONText.html#uri()) holds the URI of the JSON text.

```java
JSONValue root =
    JSONText.fromString("{\"key1\": 1.23, \"key2\": \"te\\nst\" }").parse().root();

JSONValueObject rootObj = (JSONValueObject) root;
JSONValueString str = (JSONValueString) rootObj.get("key2");
JSONToken token = str.token();

System.out.println("Token: " + token.text());
System.out.println(
    String.format(
        "Start: line %d, column %d",
        token.beginningLocation().line(),
        token.beginningLocation().column()));
System.out.println(
    String.format(
        "End  : line %d, column %d",
        token.endLocation().line(),
        token.endLocation().column()));
```

## Structured Types

Structured values (array and object) also hold the source information. by calling
@extref[begin()](javadoc:value/JSONValueStructured.html#begin()) and 
@extref[end()](javadoc:value/JSONValueStructured.html#end()).
Those methods basically return the token informaiton about `{`, `}`, `[` and `]`.

```java
JSONValue root =
    JSONText.fromString("{\"key1\": [true, 123]}").parse().root();
JSONValueObject rootObj = (JSONValueObject) root;

JSONToken begin = rootObj.begin();
JSONToken end = rootObj.end();

String msgBegin =
    String.format(
        "%s at line %d, column %d",
        begin.text(),
        begin.beginningLocation().line(),
        begin.beginningLocation().column());
System.out.println(msgBegin);

String msgEnd =
    String.format(
        "%s at line %d, column %d",
        end.text(),
        end.beginningLocation().line(),
        end.beginningLocation().column());
System.out.println(msgEnd);
```

@@@ warning
@extref[token()](javadoc:value/JSONValuePrimitive.html#token()),
@extref[begin()](javadoc:value/JSONValueStructured.html#begin()) and
@extref[end()](javadoc:value/JSONValueStructured.html#end())
return null if the JSON value does not originate from an instance of
@extref[JSONText](javadoc:JSONText.html).

For example, if your application creates new instances of JSON values to generate a 
new JSON text, they do not hold a source information.
@@@