/**
 * This package provides classes for <a href="https://json-schema.org/specification.html">JSON
 * Schema</a> 2019-09.
 *
 * <p>Note that this library internally uses {@link java.net.URI} in the standard Java library to
 * handel URIs. According to javadoc of Java 11, it conforms to <a
 * href="http://www.ietf.org/rfc/rfc2396.txt">RFC 2396</a> and <a
 * href="http://www.ietf.org/rfc/rfc2732.txt">RFC 2732</a>. However, the specification of JSON
 * Schema 2019-09 refers to <a href="https://tools.ietf.org/html/rfc3986">RFC 3986</a> for the use
 * of URIs. Therefore, this package is not fully compliant with the JSON Schema specification in
 * this sense.
 *
 * @see <a href="https://json-schema.org/">JSON Schema</a>
 */
package com.github.tnakamot.json.schema;
