# JSON source information

The advantage of this library is that each primitive JSON value hold where it is
originated from. To obtain that information, call 
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
  after the last character of this JSON value within the JSON text.
* @extref[source()](javadoc:token/JSONToken.html#source()) returns an instance of
  @extref[JSONText](javadoc:JSONText.html) from which the parser extracted the JSON value.
   * @extref[JSONText#name()](javadoc:JSONText.html#name()) holds the short name
     of the JSON text. In many cases, it returns a file name.
   * @extref[JSONText#uri()](javadoc:JSONText.html#uri()) holds the URI of the JSON text.

@@@ note
@extref[token()](javadoc:value/JSONValuePrimitive.html#token()) is available only for
JSON primitive values (boolean, string, number and null).
@@@

@@@ warning
@extref[token()](javadoc:value/JSONValuePrimitive.html#token()) returns null if the JSON
value does not originate from an instance of @extref[JSONText](javadoc:JSONText.html).

For example, if your application creates new instances of JSON values to generate a 
new JSON text, they do not hold a source information.
@@@
