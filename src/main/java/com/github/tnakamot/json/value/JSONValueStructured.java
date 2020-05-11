package com.github.tnakamot.json.value;

import com.github.tnakamot.json.token.JSONToken;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents one JSON structured value.
 *
 * <p>Instances of this class are immutable.
 */
public abstract class JSONValueStructured extends JSONValue {
  private final JSONToken begin;
  private final JSONToken end;

  /**
   * Create an instance of a Java representation of a JSON structured value.
   *
   * @param type type of this JSON value
   */
  JSONValueStructured(@NotNull JSONValueType type) {
    this(type, null, null);
  }

  /**
   * Create an instance of a Java representation of a JSON primitive value with source JSON text
   * information.
   *
   * @param type type of this JSON value
   * @param begin the beginning token of this JSON value. Null if this JSON value is not originated
   *     from an exiting JSON text.
   * @param end the beginning token of this JSON value. Null if this JSON value is not originated
   *     from an exiting JSON text.
   */
  JSONValueStructured(
      @NotNull JSONValueType type, @Nullable JSONToken begin, @Nullable JSONToken end) {
    super(type);
    this.begin = begin;
    this.end = end;
  }

  /**
   * The beginning token of this structured value.
   *
   * @return the beginning token of this structured value. Can be null if this JSON value is not
   *     originated from an existing JSON text.
   */
  @Nullable
  public JSONToken begin() {
    return begin;
  }

  /**
   * The end token of this structured value.
   *
   * @return the end token of this structured value. Can be null if this JSON value is not
   *     originated from an existing JSON text.
   */
  @Nullable
  public JSONToken end() {
    return end;
  }
}
