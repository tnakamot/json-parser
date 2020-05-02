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

The advantage of this library is to keep the information about where the JSON
was obtained from like URL. Therefore, it is not recommended to load JSON
text from String because the library cannot know where the JSON text comes from.



