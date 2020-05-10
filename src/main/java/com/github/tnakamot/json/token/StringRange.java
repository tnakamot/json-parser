package com.github.tnakamot.json.token;

import org.jetbrains.annotations.NotNull;

/**
 * Represents a range in a {@link String}.
 *
 * <p>Instances of this class are immutable.
 */
public class StringRange {
  private final StringLocation begin;
  private final StringLocation end;

  public StringRange(@NotNull StringLocation begin, @NotNull StringLocation end) {
    this.begin = begin;
    this.end = end;
  }

  /**
   * The beginning location of this range.
   *
   * @return beginning location of this range.
   */
  @NotNull
  public StringLocation beginning() {
    return begin;
  }

  /**
   * The end location of this range.
   *
   * @return end location of this range.
   */
  @NotNull
  public StringLocation end() {
    return end;
  }
}
