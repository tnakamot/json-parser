# JSON Schema Code and Document Generator

This is a command line tool to generate documents of JSON Schema which explains
how the JSON data should look like to follow the given JSON Schema.

The goal is to generate source code to parse, validate and deserialize JSON 
data based on the JSON Schema, but it has not been implemented yet.

## Usage

The main class is `org.github.tnakamot.jscdg.CLIMain`. The first command line
argument is considered as sub command. To see a list of valid sub commands,
run this tool without any command line arguments.

The sub command typically specifies the type of output format (e.g. doxygen).
There are several options available for each sub command.

(TODO: write more)

## TODO

### High Priority

* Support the following property types.
   * [array](https://json-schema.org/understanding-json-schema/reference/array.html)
   * [null](https://json-schema.org/understanding-json-schema/reference/null.html)
* Support [`definitions`](https://json-schema.org/understanding-json-schema/structuring.html#reuse)
* Support [subschema using `$id` and `$ref`](https://json-schema.org/understanding-json-schema/structuring.html#using-id-with-ref)
* More fancy doxygen output
   * Show value restrictions (e.g. range, enum, const).
   * Show inner objects. 
* Support more outputs.
   * HTML output.
   * C source and header files.
   * Java source files.
* When an error occurs, show the file and line where the error is detected.
* Unit test
* Code documentation
* Packaging this tool for Linux

### Low Priority

* Strict classification of schema versions.
* Support the following attributes of [object](https://json-schema.org/understanding-json-schema/reference/object.html) properties
   * [additionalProperties](https://json-schema.org/understanding-json-schema/reference/object.html#properties)
   * [propertyNames](https://json-schema.org/understanding-json-schema/reference/object.html#property-names)
   * [dependencies](https://json-schema.org/understanding-json-schema/reference/object.html#dependencies)
   * [patternProperties](https://json-schema.org/understanding-json-schema/reference/object.html#pattern-properties)
* Support the following schema combination
   * [allOf](https://json-schema.org/understanding-json-schema/reference/combining.html#allof)
   * [anyOf](https://json-schema.org/understanding-json-schema/reference/combining.html#anyof)
   * [oneOf](https://json-schema.org/understanding-json-schema/reference/combining.html#oneof)
   * [not](https://json-schema.org/understanding-json-schema/reference/combining.html#not)
* Support [string-encoding non-JSON data](https://json-schema.org/understanding-json-schema/reference/non_json_data.html)
* [Conditional subschemas](https://json-schema.org/understanding-json-schema/reference/conditionals.html)  
* Support multi-type property (e.g. `{ "type" : ["number", "string"] }` )



   