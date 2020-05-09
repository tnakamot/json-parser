package com.github.tnakamot.json.pointer;

import java.util.regex.Pattern;

public class InvalidJSONPointerException extends Exception {
  private final String text;
  private final int begin;
  private final int end;

  /**
   * Instantiate the exception with a JSON Pointer text that has an error.
   *
   * @param msg error message to show
   * @param text the JSON Pointer text that has a syntax error
   * @param begin beginning location of the character range where the problem is
   * @param end end location of the character range where the problem is
   */
  public InvalidJSONPointerException(String msg, String text, int begin, int end) {
    super(msg);
    this.text = text;
    this.begin = begin;
    this.end = end;

    if (text == null) {
      throw new NullPointerException("text cannot be null");
    } else if (begin < 0) {
      throw new IllegalArgumentException("begin cannot be negative");
    } else if (end < 0) {
      throw new IllegalArgumentException("end cannot be negative");
    } else if (end < begin) {
      throw new IllegalArgumentException("end cannot be less than begin");
    } else if (begin > text.length()) {
      throw new IllegalArgumentException("begin cannot be larger than the text length");
    } else if (end > text.length()) {
      throw new IllegalArgumentException("end cannot be larger than the text length");
    }
  }

  /**
   * Returns the JSON Pointer text that has an error.
   *
   * @return the JSON Pointer text that has an error.
   */
  public String text() {
    return text;
  }

  /**
   * Beginning location of the character range where the problem is.
   *
   * @return beginning location of the character range where the problem is.
   */
  public int begin() {
    return begin;
  }

  /**
   * End location of the character range where the problem is.
   *
   * @return end location of the character range where the problem is.
   */
  public int end() {
    return end;
  }

  @Override
  public String getMessage() {
    StringBuilder sb = new StringBuilder();
    sb.append(super.getMessage());
    sb.append(System.lineSeparator());

    sb.append("Location     : ");
    if (begin == end || begin + 1 == end) {
      sb.append(begin);
    } else {
      sb.append(begin);
      sb.append(" - ");
      sb.append(end - 1);
    }
    sb.append(System.lineSeparator());

    sb.append("JSON Pointer : ");
    sb.append(text);

    Pattern p = Pattern.compile("\\p{Cntrl}");
    if (!(text.isEmpty() || p.matcher(text).find())) {
      sb.append(System.lineSeparator());
      sb.append("               ");
      sb.append(" ".repeat(begin));
      if (begin == end) {
        sb.append("^");
      } else {
        sb.append("^".repeat(end - begin));
      }
    }

    return sb.toString();
  }
}
