package com.github.tnakamot.json.parser;

import com.github.tnakamot.json.value.JSONValue;
import com.github.tnakamot.json.value.JSONValueNumber;
import com.github.tnakamot.json.value.JSONValueString;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** Result of parsing a JSON text. */
public class JSONParserResult {
  private final JSONValue root;
  private final List<List<JSONValueString>> duplicateKeys;
  private final List<JSONValueNumber> numbersTooBigForDouble;

  JSONParserResult(
      @Nullable JSONValue root,
      @NotNull List<List<JSONValueString>> duplicateKeys,
      @NotNull List<JSONValueNumber> numbersTooBigForDouble) {
    this.root = root;

    List<List<JSONValueString>> tmp = new ArrayList<>(duplicateKeys.size());
    for (List<JSONValueString> dup : duplicateKeys) {
      tmp.add(Collections.unmodifiableList(dup));
    }
    this.duplicateKeys = Collections.unmodifiableList(tmp);

    this.numbersTooBigForDouble = Collections.unmodifiableList(numbersTooBigForDouble);
  }

  /**
   * Returns the root JSON value that the parser found.
   *
   * @return the root JSON value, null if there is no value.
   */
  @Nullable
  public JSONValue root() {
    return root;
  }

  /**
   * Returns lists of duplicated keys found when parsing.
   *
   * @return lists of duplicated keys found when parsing
   */
  @NotNull
  public List<List<JSONValueString>> duplicateKeys() {
    List<List<JSONValueString>> ret = new ArrayList<>(duplicateKeys.size());
    for (List<JSONValueString> dup : duplicateKeys) {
      ret.add(new ArrayList<>(dup));
    }

    return ret;
  }

  /**
   * Returns list of numbers that are found to be too big for Java double primitive when parsing.
   *
   * @return list of numbers that are found to be too big for Java double primitive when parsing.
   */
  @NotNull
  public List<JSONValueNumber> numbersTooBigForDouble() {
    return new ArrayList<>(numbersTooBigForDouble);
  }
}
