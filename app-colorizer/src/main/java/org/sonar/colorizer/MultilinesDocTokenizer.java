/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2009 SonarSource SA
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.colorizer;

import java.util.Arrays;

import org.sonar.channel.CodeReader;
import org.sonar.channel.EndMatcher;

public class MultilinesDocTokenizer extends Tokenizer {

  private static final String COMMENT_STARTED_ON_PREVIOUS_LINE = "COMMENT_STARTED_ON_PREVIOUS_LINE";
  private static final String COMMENT_TOKENIZER = "MULTILINE_COMMENT_TOKENIZER";
  private final char[] startToken;
  private final char[] endToken;
  private final String tagBefore;
  private final String tagAfter;

  /**
   * @deprecated endToken is hardcoded to star-slash, whatever the startToken !
   */
  @Deprecated
  public MultilinesDocTokenizer(String startToken, String tagBefore, String tagAfter) {
    this(startToken, "*/", tagBefore, tagAfter);
  }

  public MultilinesDocTokenizer(String startToken, String endToken, String tagBefore, String tagAfter) {
    this.tagBefore = tagBefore;
    this.tagAfter = tagAfter;
    this.startToken = startToken.toCharArray();
    this.endToken = endToken.toCharArray();
  }

  public boolean hasNextToken(CodeReader code, HtmlCodeBuilder codeBuilder) {
    return code.peek() != '\n'
        && code.peek() != '\r'
        && (isCommentStartedOnPreviousLine(codeBuilder) || (code.peek() == startToken[0] && Arrays.equals(code.peek(startToken.length),
            startToken)));
  }

  public boolean consume(CodeReader code, HtmlCodeBuilder codeBuilder) {
    if (hasNextToken(code, codeBuilder)) {
      codeBuilder.appendWithoutTransforming(tagBefore);
      code.popTo(new MultilineEndTokenMatcher(codeBuilder), codeBuilder);
      codeBuilder.appendWithoutTransforming(tagAfter);
      return true;
    } else {
      return false;
    }
  }

  private class MultilineEndTokenMatcher implements EndMatcher {

    private final HtmlCodeBuilder code;
    private final StringBuilder colorizedCode;
    private int commentSize = 0;

    public MultilineEndTokenMatcher(HtmlCodeBuilder code) {
      this.code = code;
      this.colorizedCode = code.getColorizedCode();
    }

    public boolean match(int endFlag) {
      commentSize++;
      if (commentSize >= endToken.length + startToken.length || (commentSize >= endToken.length && isCommentStartedOnPreviousLine(code))) {
        boolean matches = true;
        for (int i = 1; i <= endToken.length; i++) {
          if (colorizedCode.charAt(colorizedCode.length() - i) != endToken[endToken.length - i]) {
            matches = false;
            break;
          }
        }
        if (matches) {
          setCommentStartedOnPreviousLine(code, Boolean.FALSE);
          return true;
        }
      }

      if (endFlag == '\r' || endFlag == '\n') {
        setCommentStartedOnPreviousLine(code, Boolean.TRUE);
        return true;
      }
      return false;
    }
  }

  private boolean isCommentStartedOnPreviousLine(HtmlCodeBuilder codeBuilder) {
    Boolean b = (Boolean) codeBuilder.getVariable(COMMENT_STARTED_ON_PREVIOUS_LINE, Boolean.FALSE);
    return (b == Boolean.TRUE) && (getTokenizerId().equals(codeBuilder.getVariable(COMMENT_TOKENIZER)));
  }

  private void setCommentStartedOnPreviousLine(HtmlCodeBuilder codeBuilder, Boolean b) {
    codeBuilder.setVariable(COMMENT_STARTED_ON_PREVIOUS_LINE, b);
    codeBuilder.setVariable(COMMENT_TOKENIZER, b ? getTokenizerId() : null);
  }

  private String getTokenizerId() {
    return getClass().getSimpleName();
  }
}
