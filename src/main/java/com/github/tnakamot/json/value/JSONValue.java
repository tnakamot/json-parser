

package com.github.tnakamot.json.value;

import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

/**
 * Represents one JSON value.
 *
 * <p>
 * Instances of this class are immutable.
 */
public abstract class JSONValue {
    private final JSONValueType type;

    /**
     * Create an instance of a Java representation of a JSON value.
     *
     * @param type type of this JSON value
     */
    JSONValue(JSONValueType type) {
        this.type = type;
    }

    /**
     * Type of this JSON value.
     *
     * @return type of this JSON value
     */
    public JSONValueType type() {
        return type;
    }

    /**
     * Convert this JSON value to String which can be saved as a JSON text to a file
     * or transmitted to network. The output text is optimized for machine, so no
     * indent or new lines will be inserted.
     *
     * <p>
     * Note that, according to
     * <a href="https://tools.ietf.org/html/rfc8259#section-8.1">RFC 8259 - 8.1 Character Encoding</a>,
     * your application program must encode the returned String using UTF-8 without BOM. It is caller's
     * responsibility to correctly encode the returned String.
     *
     * @return a string representation of this JSON value
     */
    @NotNull
    public abstract String toTokenString();

    /**
     * Convert this JSON value to String which can be saved as a JSON text to a file
     * or transmitted to network. The output text is optimized for machine, so no
     * indent or new lines will be inserted.
     *
     * <p>
     * The returned byte array is encoded using UTF-8 without BOM in accordance with
     * <a href="https://tools.ietf.org/html/rfc8259#section-8.1">RFC 8259 - 8.1 Character Encoding</a>.
     *
     * @return a byte array representation of this JSON value
     */
    @NotNull
    public byte[] toTokenBytes(boolean prettyPrint) {
        String tokenStr = toTokenString();
        return tokenStr.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * Convert this JSON value to String which can be saved as a JSON text to a file
     * or transmitted to network. The output text is optimized for human.
     *
     * <p>
     * Note that, according to
     * <a href="https://tools.ietf.org/html/rfc8259#section-8.1">RFC 8259 - 8.1 Character Encoding</a>,
     * your application program must encode the returned String using UTF-8 without BOM. It is caller's
     * responsibility to correctly encode the returned String.
     *
     * @param newline line separator; "\r", "\n" or "\r\n"
     * @param indent  indent string. Must consist of " " and "\t".
     * @return a string representation of this JSON value
     */
    @NotNull
    public abstract String toTokenString(String newline, String indent);

    /**
     * Convert this JSON value to String which can be saved as a JSON text to a file
     * or transmitted to network. The output text is optimized for human.
     *
     * <p>
     * The returned byte array is encoded using UTF-8 without BOM in accordance with
     * <a href="https://tools.ietf.org/html/rfc8259#section-8.1">RFC 8259 - 8.1 Character Encoding</a>.
     *
     * @param newline line separator; "\r", "\n" or "\r\n"
     * @param indent  indent string. Must consist of " " and "\t".
     * @return a byte array representation of this JSON value
     */
    @NotNull
    public byte[] toTokenBytes(String newline, String indent) {
        String tokenStr = toTokenString(newline, indent);
        return tokenStr.getBytes(StandardCharsets.UTF_8);
    }

    void validateNewline(String newline) {
        if (! (newline.equals("\n") || newline.equals("\r") || newline.equals("\r\n"))) {
            throw new IllegalArgumentException("newline must be \\n, \\r, or \\r\\n.");
        }
    }

    void validateIndent(String indent) {
        if (! indent.matches("[ \t]*")) {
            throw new IllegalArgumentException("indent must include only white spaces ' ' and horizontal tabs '\\t'");
        }
    }
}
