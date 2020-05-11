# Parser error handling options

You can control some error handling behavior of the parser by calling 
@extref[JSONText#parse(JSONParserErrorHandlingOptions)](javadoc:JSONText.html#parse(com.github.tnakamot.json.parser.JSONParserErrorHandlingOptions)).
@extref[JSONParserErrorHandlingOptions](javadoc:parser/JSONParserErrorHandlingOptions.html)
is a class to hold options to control the error handling behavior. It follows Builder
Pattern. Below is an example to set options. 

```java
JSONParserErrorHandlingOptions opt =
    JSONParserErrorHandlingOptions.builder()
         .showURI(false)
         .showLineAndColumnNumber(true)
         .showErrorLine(false)
         .failOnDuplicateKey(false)
         .failOnTooBigNumber(false)
         .warningStream(System.err)
         .build();
```

Following sections explais each option.

## showURI

If `showURI` option is true, the error message shows the URI of the JSON text
which has an error. If it is false, it shows only a short name, which is typically
a file name.

Here is an example code.

```java
JSONParserErrorHandlingOptions options =
     JSONParserErrorHandlingOptions.builder().showURI(true).build();
String url = "http://localhost:8215/json/invalid1.json";
JSONText jsText = JSONText.fromURL(new URL(url));
jsText.parse(options);
```

Given that `http://localhost:8215/json/invalid1.json` has an error at line 3 and
column 12, the above code results in this error message.

```
http://localhost:8215/json/invalid1.json:3:12: unknown token starting with 'x'
```

If `showURI` option is false, the error message becomes.

```
invalid1.json:3:12: unknown token starting with 'x'
```

## showLineAndColumnNumber

If `showLineAndColumnNumber` option is true, the error message shows the line
and column numbers. If it is false, it shows the character position instead.

Here is an example code.

```java
String jsonStr = 
    "[\n"+
    "  123,\n" +
    "  ff,\n" +
    "]";

JSONParserErrorHandlingOptions options =
    JSONParserErrorHandlingOptions.builder()
        .showLineAndColumnNumber(true)
        .build();

JSONText jsText = JSONText.fromString(jsonStr, "test.json");
jsText.parse(options);
```

This example code results in this error message.

```
test.json:3:3: unknown token starting with 'ff'
```

If `showLineAndColumnNumber` option is false, the error message becomes:

```
test.json:11: unknown token starting with 'ff'
```

@@@ note
The line and column numbers start with 1 while the character position starts with 0.
@@@

## showErrorLine

If `showErrorLine` option is true, the error message shows the line that has
an error with markers to indicate where is the problem.

Here is an example.

```java
String jsonStr =
    "{\n" +
    "  \"key1\" true,\n" +
    "  \"key2\": false\n" +
    "}";

JSONParserErrorHandlingOptions options =
    JSONParserErrorHandlingOptions.builder()
        .showErrorLine(true)
        .build();

JSONText jsText = JSONText.fromString(jsonStr, "test.json");
jsText.parse(options);
```

This example code result in this error message.

```
test.json:2:10: Unexpected token 'true'. ':' was expected.
  "key1" true,
         ^^^^
```

If `showErrorLine` is false, it shows only the error message like below:

```
test.json:2:10: Unexpected token 'true'. ':' was expected.
```

## failOnDuplicateKey

If `failOnDuplicateKey` option is true and there are duplicate keys in the same
JSON object, the parser results in
@extref[JSONParserException](javadoc:parser/JSONParserException.html).

Here is an example.

```java
JSONParserErrorHandlingOptions opt =
    JSONParserErrorHandlingOptions.builder()
        .showErrorLine(true)
        .failOnDuplicateKey(true)
        .build();

JSONText jsText =
    JSONText.fromString(
        "{" +
        " \"key1\": true,\n" +
        " \"key2\": false,\n" +
        " \"key1\": null,\n" +
        " \"key1\": false,\n" +
        " \"key2\": null\n" +
        "}", "test.json");

jsText.parse(opt);
```

This example code ends up with
@extref[JSONParserException](javadoc:parser/JSONParserException.html)
and the following error message.

```
test.json:3:2: Found duplicate key 'key1' in the same JSON object.
 "key1": null,
 ^^^^^^
```

If `failOnDuplicateKey` option is false, this library retains only the last value of
the same key. In the above example but with `failOnDuplicateKey` being false, the
resultant JSON object returns `false` for "key1" and `null` for "key2".

If `failOnDuplicateKey` option is false, this library warns of duplicate keys in
the standard error by default. The destination of the warning message can be changed
by [warningStream](#warningstream) option.

## failOnTooBigNumber

If `failOnTooBigNumber` option is true and there is one or more 
numbers that are not in the range between (-2<sup>53</sup> + 1) and (2<sup>53</sup> - 1),
the parser results in
@extref[JSONParserException](javadoc:parser/JSONParserException.html).

Here is an example.

```java
JSONParserErrorHandlingOptions opt =
    JSONParserErrorHandlingOptions.builder()
        .showErrorLine(true)
        .failOnTooBigNumber(true)
        .build();

JSONText jsText =
    JSONText.fromString(
        "{\"key1\": 1.52, \"key2\": 9007199254740992, \"key3\": null}",
        "test_double.json");

jsText.parse(opt);
```
This example code ends up with
@extref[JSONParserException](javadoc:parser/JSONParserException.html)
and the following error message.

```
test_double.json:1:24: '9007199254740992' is not in the range [-(2^53)+1, 2^53-1]
{"key1": 1.52, "key2": 9007199254740992, "key3": null}
                       ^^^^^^^^^^^^^^^^
```

If `failOnDuplicateKey` option is false, this library warns of too big numbers in
the standard error by default. The destination of the warning message can be changed
by [warningStream](#warningstream) option.
  
@@@ note
This option is to enforce the users not to use integer numbers that cannot be handled by a 
double-precision floating point value without information loss.

This library itself handles JSON numbers in a string, so no information loss will occur
unless your application converts them to another numeric type like 'double'. In many cases,
application programs convert a JSON number in a double-precisiong floating point value.
If your application program wants to do so, this option helps.
@@@
  
  
## warningStream

This option changes the output destination of warning messages. If you want to
show the warning messages to a standard output, build the option with the following
example code.

```java
JSONParserErrorHandlingOptions opt =
    JSONParserErrorHandlingOptions.builder()
         .warningStream(System.out)
         .build();
```

Below is an example warning output which is shown if `failOnDuplicateKey` is false
in the example code in [failOnDuplicateKey](#failonduplicatekey).

```
test.json: duplicate key 'key1': 
  At line 1, column 3 - 8
    { "key1": true,
      ^^^^^^
  At line 3, column 2 - 7
     "key1": null,
     ^^^^^^
  At line 4, column 2 - 7
     "key1": false,
     ^^^^^^
test.json: duplicate key 'key2': 
  At line 2, column 2 - 7
     "key2": false,
     ^^^^^^
  At line 5, column 2 - 7
     "key2": null
     ^^^^^^
```