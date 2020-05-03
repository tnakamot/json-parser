/**
 * This package provides classes to parse JSON text based
 * on <a href="https://tools.ietf.org/html/rfc8259">RFC 8259</a>.
 *
 * {@link com.github.tnakamot.json.parser.JSONLexer} tokenizes a given
 * {@link com.github.tnakamot.json.JSONText}.
 * {@link com.github.tnakamot.json.parser.JSONParser} semantically
 * analyzes the sequence of tokens and generate
 * {@link com.github.tnakamot.json.value.JSONValue}.
 *
 * @see <a href="https://tools.ietf.org/html/rfc8259">RFC 8259</a>
 */
package com.github.tnakamot.json.parser;