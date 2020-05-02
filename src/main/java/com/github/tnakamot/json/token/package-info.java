/**
 * This package provides classes to represent tokens in JSON text.
 * All types of tokens except white spaces (ws) defined
 * in <a href="https://tools.ietf.org/html/rfc8259">RFC 8259</a>
 * are supported. All insignificant white spaces are simply ignored
 * in this implementation.
 *
 * <p>
 * All token types are defined as enums of
 * {@link com.github.tnakamot.json.token.JSONTokenType}.
 * {@link com.github.tnakamot.json.token.JSONToken} represents
 * one token of any type.
 *
 * <p>
 * All structural characters ('[', ']', '{', '}', ',', ':') are
 * represented as direct instances of {@link com.github.tnakamot.json.token.JSONToken}.
 *
 * <p>
 * Tokens that represent primitive JSON values (string, number, boolean
 * and null) have corresponding child classes of {@link com.github.tnakamot.json.token.JSONToken}:
 *
 * <ul>
 *     <li>string: {@link com.github.tnakamot.json.token.JSONTokenString}</li>
 *     <li>number: {@link com.github.tnakamot.json.token.JSONTokenNumber}</li>
 *     <li>boolean: {@link com.github.tnakamot.json.token.JSONTokenBoolean}</li>
 *     <li>null: {@link com.github.tnakamot.json.token.JSONTokenNull}</li>
 * </ul>
 *
 * <p>
 * Instantiation of {@link com.github.tnakamot.json.token.JSONToken} and its
 * child classes should be done by JSON lexical analyzes. In order to support
 * possible various implementations of JSON lexical analyzers, all their constructors
 * are defined as "public", but it is highly discouraged by user programs to directly
 * instantiate them.
 *
 * @see <a href="https://tools.ietf.org/html/rfc8259">RFC 8259</a>
 */
package com.github.tnakamot.json.token;
