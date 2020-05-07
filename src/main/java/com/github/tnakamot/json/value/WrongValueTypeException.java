package com.github.tnakamot.json.value;

/** Thrown when this library gets a JSON value of unexpected type. */
public class WrongValueTypeException extends RuntimeException {
  private final JSONValueType expected;
  private final JSONValueType actual;

  public WrongValueTypeException(String msg, JSONValueType expected, JSONValueType actual) {
    super(msg + " Type " + expected + " was expected, but actual type was " + actual + ".");
    this.expected = expected;
    this.actual = actual;
  }

  public JSONValueType expected() {
    return expected;
  }

  public JSONValueType actual() {
    return actual;
  }
}
