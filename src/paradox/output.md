# Output JSON text

@extref[JSONValue](javadoc:value/JSONValue.html) has the following methods to output
JSON text.

* @extref[toTokenBytes](javadoc:value/JSONValue.html#toTokenBytes())
* @extref[toTokenBytes(String newline, String indent)](javadoc:value/JSONValue.html#toTokenBytes(java.lang.String,java.lang.String))
* @extref[toTokenString](javadoc:value/JSONValue.html#toTokenString())
* @extref[toTokenString(String newline, String indent)](javadoc:value/JSONValue.html#toTokenString(java.lang.String,java.lang.String))

There are two versions for each method; one with no argument outputs a JSON text
which does not include any white spaces or new line characters. It is suitable
if the output will be processed by another program or library.

The other version has two arguments. This method is suitable for human-reading
because it adds new lines and indent accordingly. You need to specify the 
new line character(s) and indent.

`toTokenString` methods return a String. It is the caller's responsibility to
encode the returned String. 
According to [RFC 8259 - 8.1 Character Encoding](https://tools.ietf.org/html/rfc8259#section-8.1),
your application must encode it using UTF-8 without BOM before saving them to
a file or transmitted to a network.

`toTokenBytes` methods return a byte array. The returned byte array contains
the JSON text encoded in UTF-8 without BOM. 

## Examples

Let's assume you constructed a JSON data structure with the code below
and output the text with the last line where you specified '\n' as the
new line character and two white spaces as indent.

```java
JSONValueObjectMutable obj = new JSONValueObjectMutable();
obj.put("key1", new JSONValueNumber("123"));
obj.put(new JSONValueString("key2"), new JSONValueNumber("789"));
System.out.println(obj.toTokenString("\n", "  "));
```

Then, you will get this result.

```json
{
  "key1": 123,
  "key2": 789
}
```

If you do not specify new line or indent like this code,

```java
System.out.println(obj.toTokenString());
```
then you will get this one line result.

```json
{"key1":123,"key2":789}
```