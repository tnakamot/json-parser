# JSON Schema Code and Document Generator

This is a command line tool to generate documents of JSON Schema which explains
how the JSON data should look like to follow the given JSON Schema.

The goal is to generate source code to parse, validate and deserialize JSON 
data based on the JSON Schema, but it has not been implemented yet.

## Pre-requisite

Java 11 (or higher) is required to run this tool. 

The command `java` needs to be in PATH.

## Install

Download the latest zip package `jscdg-x.y.z.zip` in
[Releases](https://github.com/tnakamot/jscdg/releases),
extract it in an appropriate place. This package includes the following files:

 * `bin/jscdg`: Command for Unix-like systems including Linux and Mac OS X.
 * `bin/jscdg.bat`: Batch script for Windows.
 * `lib/*.jar`: Java libraries of this tool and dependent libraries.

Add `bin/` directory to your PATH.

## Usage

The first command line argument is considered as sub command. To see a list of
valid sub commands, run this tool without any command line arguments.

    $ jscdg

The sub command typically specifies the type of output format (e.g. doxygen).
There are several options available for each sub command. To see what kind
of options are supported, run the sub command with `--help` option.

    $ jscdg doxygen --help

### Example: generate tables for Doxygen

The command below generates `$HOME/tmp/test/your_json_schema.dox` based on
the JSON Schema file `your_json_schema.json`.

    $ jscdg doxygen --output $HOME/tmp/test --extension dox your_json_schema.json

The generated file `your_json_schema.dox` can be used as a part of your
documents for Doxygen using, for example, [\include](http://www.doxygen.nl/manual/commands.html#cmdinclude)
command.

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
* More javadoc.

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
* Package this tool as RPM and DEB.
* Package this tool as a library and upload to [Maven Central](https://mvnrepository.com/repos/central)
  so that sbt or some other build tools can use this tool as a part of the build procedure.


   