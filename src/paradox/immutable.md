# Immutable and Mutable

All instances of @extref[JSONValue](javadoc:value/JSONValue.html) that correspond
to JSON primitive values (null, boolean, string and number) are immutable. In other
words, instances of @extref[JSONValueNull](javadoc:value/JSONValueNull.html),
@extref[JSONValueBoolean](javadoc:value/JSONValueBoolean.html),
@extref[JSONValueString](javadoc:value/JSONValueString.html) and
@extref[JSONValueNumber](javadoc:value/JSONValueNumber.html) are immutable. Multiple
threads can safely access those instance at a time.

On the other hands, the structures types (array and object) have immutable and
mutable versions. @extref[JSONValueArray](javadoc:value/JSONValueArray.html) and 
@extref[JSONValueObject](javadoc:value/JSONValueObject.html) are abstract classes.
The actual implementations of these abstract classes are
@extref[JSONValueArrayImmutable](javadoc:value/JSONValueArrayImmutable.html),
@extref[JSONValueArrayMutable](javadoc:value/JSONValueArrayMutable.html),
@extref[JSONValueObjectImmutable](javadoc:value/JSONValueObjectImmutable.html) and
@extref[JSONValueObjectMutable](javadoc:value/JSONValueObjectMutable.html).

Invocation of any method that may change the contents (e.g.
@javadoc[add()](java.util.List#add(E)) and @javadoc[put()](java.util.Map#put(K,V)))
result in @javadoc[UnsupportedOperationException](java.lang.UnsupportedOperationException)
if it is immutable.

## Parser

When you parse the JSON text, you can specify whether the parser should return
immutable or mutable version. @extref[parse(boolean)](javadoc:JSONText.html#parse(boolean))
method of @extref[JSONText](javadoc:JSONText.html) takes a boolean value.

If it is `true`, the method returns the immutable version, which is suitable to
load configuration in JSON text in memory, for example.

If it is `false`, the method returns the mutable version. The mutable version
allows you to add, remove and modify the contents in JSON objects and arrays.
The mutable version is suitable, for example, if your application needs to read
a JSON text from a file, modify the contents and write back to another file.

## Conversion

You can get a mutable copy of an immutable JSON object or array and get an
immutable copy of a mutable version with the following methods 

* @extref[JSONValueArrayImmutable#toMutable()](javadoc:value/JSONValueArrayImmutable.html#toMutable())
* @extref[JSONValueObjectImmutable#toMutable()](javadoc:value/JSONValueObjectImmutable.html#toMutable())
* @extref[JSONValueArrayMutable#toImutable()](javadoc:value/JSONValueArrayMutable.html#toImmutable())
* @extref[JSONValueObjectMutable#toImutable()](javadoc:value/JSONValueObjectMutable.html#toImmutable())

These methods turn the inner JSON objects and arrays to immutable or mutable.
For example, if there is a mutable JSON object which contains two mutable JSON
arrays,
@extref[JSONValueObjectImmutable#toMutable()](javadoc:value/JSONValueObjectImmutable.html#toMutable())
turns the given JSON object and two JSON arrays to immutable.


