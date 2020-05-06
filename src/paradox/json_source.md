# Load JSON text from various sources

JSON text can be loaded from various sources.

## Local file 

```java
JSONText jsText = JSONText.fromFile(new File("/path/to/file.json"));
```

## URL

```java
JSONText jsText = JSONText.fromURL(new URL("http://example.jp/test.json"))
```

## String

```java
JSONText jsText = JSONText.fromString(
        " { \"key1\": true, " +
        "   \"key2\": false," +
        "   \"key3\": null } ");
```

The above example does not specify the source name. For better user experience,
it is recommended to specify the source name of the JSON text by calling
@extref[fromString(String, String)](javadoc:JSONText.html#fromString(java.lang.String,java.lang.String)).
The second string parameter specifies the name. This name will be shown when
the parser encounters a syntax error. For example, the code below results
in @extref[JSONParserException](javadoc:parser/JSONParserException.html)
(because the string `value` is not enclosed by double quotes).

```java
String text = "{ \"key\": value }";
JSONText source = JSONText.fromString(text, "text_in_memory.json");
JSONValue value root = source.parse()
```

As this example sets the source name to "text_in_memory.json", the error message
of the exception is shown as follows.

```
text_in_memory.json:1:10: unknown token starting with 'v'
```

From this error message, the user can quickly know the error is in the 10th
character of line 1 in text_in_memory.json. Therefore, it is highly recommended
specifying the name that the user of your application can understand.

