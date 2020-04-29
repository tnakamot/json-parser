package com.github.tnakamot.jscdg.json.parser;

import com.github.tnakamot.jscdg.json.token.*;
import com.github.tnakamot.jscdg.json.value.*;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import static com.github.tnakamot.jscdg.json.token.JSONToken.*;

/**
 * An implementation of a parser of JSON texts. This implementation complies with
 * <a href="https://tools.ietf.org/html/rfc8259">RFC 8259</a>.
 *
 * <p>
 * Instances of this class are disposal. A new instance must be created
 * to parse one sequence of JSON tokens.
 *
 * @see <a href="https://tools.ietf.org/html/rfc8259">RFC 8259</a>
 */
public class JSONParser {
    private final List<JSONToken> tokens;
    private final JSONParserErrorMessageFormat errMsgFmt;
    private int position;

    private static final String stringToken = "A string";
    private static final String valueToken
            = "A JSON value (object, array, number, string, boolean or null)";
    private static final String valueOrEndArrayToken
            = String.format("%s or '%s'", valueToken, JSON_END_ARRAY);
    private static final String valueSepOrEndArrayToken
            = String.format("'%s' or '%s'", JSON_VALUE_SEPARATOR, JSON_END_ARRAY);
    private static final String stringOrEndObjectToken
            = String.format("%s or '%s'", stringToken, JSON_END_OBJECT);
    private static final String valueSepOrEndObjectToken
            = String.format("'%s' or '%s'", JSON_VALUE_SEPARATOR, JSON_END_OBJECT);
    private static final String nameSepToken
            = String.format("'%s'", JSON_NAME_SEPARATOR);

    /**
     * Create an instance of JSON parse for the given sequence of JSON tokens.
     *
     * @param tokens a sequence of JSON tokens to parse
     * @param errMsgFmt settings of error message format of {@link JSONParserException}
     */
    public JSONParser(List<JSONToken> tokens, JSONParserErrorMessageFormat errMsgFmt) {
        if (tokens == null) {
            throw new NullPointerException("tokens cannot be null");
        } else if (errMsgFmt == null) {
            throw new NullPointerException("errMsgConfig cannot be null");
        }

        this.tokens    = new ArrayList<>(tokens);
        this.errMsgFmt = errMsgFmt;
        this.position  = 0;
    }

    /**
     * Parse the given sequence of JSON tokens and return the root JSON value.
     *
     * @return the root JSON value, or null if there is no value.
     * @throws JSONParserException if there is a semantic error in the sequence of JSON tokens
     * @see <a href="https://tools.ietf.org/html/rfc8259#section-2">RFC 8259 - 2. JSON Grammer</a>
     */
    public JSONValue parse() throws JSONParserException {
        if (tokens == null || tokens.size() == 0) {
            return null;
        }

        return readValue();
    }

    private JSONToken popToken() {
        JSONToken token = tokens.get(position);
        position += 1;
        return token;
    }

    private JSONToken lastToken() {
        JSONToken token = tokens.get(tokens.size() - 1);
        return token;
    }

    private void pushBack() {
        position -= 1;
    }

    private void unexpectedEof(String expectedToken) throws JSONParserException {
        String msg = String.format("Reached EOF unexpectedly. %s was expected.", expectedToken);
        JSONToken lastToken = lastToken();

        throw new JSONParserException(
                lastToken.source(), lastToken.endLocation(), errMsgFmt, msg);
    }

    private void unexpectedToken(JSONToken token, String expectedToken)
            throws JSONParserException
    {
        String msg = String.format("Unexpected token '%s'. %s was expected.",
                token.text(), expectedToken);
        throw new JSONParserException(
                token.source(), token.beginningLocation(), errMsgFmt, msg);
    }

    private JSONValue readValue() throws JSONParserException {
        try {
            JSONToken token = popToken();

            switch (token.type()) {
                case BEGIN_ARRAY:
                    return readArray();
                case BEGIN_OBJECT:
                    return readObject();
                case NULL:
                    return new JSONValueNull(token);
                case BOOLEAN:
                    return new JSONValueBoolean((JSONTokenBoolean)token);
                case NUMBER:
                    return new JSONValueNumber((JSONTokenNumber) token);
                case STRING:
                    return new JSONValueString((JSONTokenString) token);
                default:
                    unexpectedToken(token, valueToken);
            }
        } catch (IndexOutOfBoundsException ex) {
            unexpectedEof(valueToken);
        }

        throw new RuntimeException("never reach here");
    }

    private JSONValueArray readArray() throws JSONParserException {
        List<JSONValue> array = new LinkedList<>();

        // read the first value (or it can be an empty array)
        try {
            JSONToken token = popToken();

            switch(token.type()) {
                case END_ARRAY:
                    // an empty array
                    return new JSONValueArray(array);
                case NULL:
                case BOOLEAN:
                case NUMBER:
                case STRING:
                case BEGIN_ARRAY:
                case BEGIN_OBJECT:
                    pushBack();
                    array.add(readValue());
                    break;
                default:
                    unexpectedToken(token, valueOrEndArrayToken);
            }
        } catch (IndexOutOfBoundsException ex) {
            unexpectedEof(valueOrEndArrayToken);
        }

        while(true) {
            // read a next value or an end array
            try {
                JSONToken token = popToken();

                switch (token.type()) {
                    case END_ARRAY:
                        return new JSONValueArray(array);
                    case VALUE_SEPARATOR:
                        array.add(readValue());
                        break;
                    default:
                        unexpectedToken(token, valueSepOrEndArrayToken);
                }
            } catch (IndexOutOfBoundsException ex) {
                unexpectedEof(valueSepOrEndArrayToken);
            }
        }
    }

    private Pair<JSONValueString, JSONValue> readMember() throws JSONParserException {
        JSONValueString key;

        // read a key
        try {
            JSONToken token = popToken();

            switch(token.type()) {
                case STRING:
                    key = new JSONValueString((JSONTokenString) token);
                    break;
                default:
                    unexpectedToken(token, stringToken);
                    key = null;
            }
        } catch (IndexOutOfBoundsException ex) {
            unexpectedEof(stringToken);
            key = null;
        }

        // read a name separator
        try {
            JSONToken token = popToken();

            switch(token.type()) {
                case NAME_SEPARATOR:
                    break;
                default:
                    unexpectedToken(token, nameSepToken);
            }
        } catch (IndexOutOfBoundsException ex) {
            unexpectedEof(nameSepToken);
        }

        JSONValue value = readValue();
        JSONValueString rKey = key;

        return new Pair<JSONValueString, JSONValue>() {
            @Override
            public JSONValueString getLeft() { return rKey; }

            @Override
            public JSONValue getRight() { return value; }

            @Override
            public JSONValue setValue(JSONValue jsonValue) {
                throw new UnsupportedOperationException();
            }
        };
    }

    private JSONValueObject readObject() throws JSONParserException {
        LinkedHashMap<JSONValueString, JSONValue> object
                = new LinkedHashMap<JSONValueString, JSONValue>();

        // read the first member (or it can be an empty object)
        try {
            JSONToken token = popToken();

            switch(token.type()) {
                case END_ARRAY:
                    // empty object
                    return new JSONValueObject(object);
                case STRING:
                    pushBack();
                    Pair<JSONValueString, JSONValue> member = readMember();
                    object.put(member.getLeft(), member.getRight());
                    break;
                default:
                    unexpectedToken(token, stringOrEndObjectToken);
            }
        } catch (IndexOutOfBoundsException ex) {
            unexpectedEof(stringOrEndObjectToken);
        }

        while(true) {
            // next a next member or an end object
            try {
                JSONToken token = popToken();

                switch (token.type()) {
                    case END_OBJECT:
                        return new JSONValueObject(object);
                    case VALUE_SEPARATOR:
                        Pair<JSONValueString, JSONValue> member = readMember();
                        object.put(member.getKey(), member.getValue());
                        break;
                    default:
                        unexpectedToken(token, valueSepOrEndObjectToken);
                }
            } catch (IndexOutOfBoundsException ex) {
                unexpectedEof(valueSepOrEndObjectToken);
            }
        }
    }
}
