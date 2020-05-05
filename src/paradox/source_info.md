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
  @extref[JSONText](javadoc:JSONText.html) from which this JSON value was extracted.
   * @extref[JSONText#name()](javadoc:JSONText.html#name()) holds the short name
     (i.e. file name) of the JSON text.
   * @extref[JSONText#fullName()](javadoc:JSONText.html#fullName()) holds the full
     name (i.e. URL or full path) of the JSON text.
   * The above methods do not make sense if the JSON text was loaded from String.


