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
package org.sonar.channel;

import java.io.IOException;
import java.io.Reader;
import java.util.regex.Matcher;

/**
 * The CodeReader class provides all the basic features to lex a source code. 
 * Those features are :
 * <ul>
 * <li>Read and consume next characters until a regular expression is matched</li>
 * </ul>
 */
public class CodeReader extends CodeBuffer {

  private Cursor previousCursor;

  public CodeReader(Reader code) {
    super(code);
  }

  public CodeReader(String code) {
    super(code);
  }

  protected CodeReader(String code, int bufferCapacity) {
    super(code, bufferCapacity);
  }

  /**
   * Read and consume the next character
   * 
   * @param appendable
   *          the read character is appended to appendable
   */
  public final void pop(Appendable appendable) {
    try {
      appendable.append((char) pop());
    } catch (IOException e) {
      throw new ChannelException(e.getMessage());
    }
  }

  /**
   * Read without consuming the next characters
   * 
   * @param length
   *          number of character to read
   * @return array of characters
   */
  public final char[] peek(int length) {
    char[] result = new char[length];
    int index = 0;
    int nextChar = intAt(index);
    while (nextChar != -1 && index < length) {
      result[index] = (char) nextChar;
      nextChar = intAt(++index);
    }
    return result;
  }

  /**
   * Read without consuming the next characters until a condition is reached (EndMatcher)
   * 
   * @param matcher
   *          the EndMatcher used to stop the reading
   * @param appendable
   *          the read characters is appended to appendable
   */
  public final void peekTo(EndMatcher matcher, Appendable appendable) {
    int index = 0;
    char nextChar = charAt(index);
    try {
      while ( !matcher.match(nextChar) && nextChar != -1) {
        appendable.append(nextChar);
        nextChar = charAt(++index);
      }
    } catch (IOException e) {
      throw new ChannelException(e.getMessage(), e);
    }
  }

  /**
   * @deprecated see peekTo(EndMatcher matcher, Appendable appendable)
   */
  @Deprecated
  public final String peekTo(EndMatcher matcher) {
    StringBuilder sb = new StringBuilder();
    peekTo(matcher, sb);
    return sb.toString();
  }

  /**
   * @deprecated see popTo(Matcher matcher, Appendable appendable)
   */
  @Deprecated
  public final void popTo(EndMatcher matcher, Appendable appendable) {
    previousCursor = getCursor().clone();
    try {
      do {
        appendable.append((char) pop());
      } while ( !matcher.match(peek()) && peek() != -1);
    } catch (IOException e) {
      throw new ChannelException(e.getMessage(), e);
    }
  }

  /**
   * Read and consume the next characters according to a given regular expression
   * 
   * @param matcher
   *          the regular expression matcher
   * @param appendable
   *          the consumed characters are appended to this appendable
   * @return number of consumed characters or -1 if the next input sequence doesn't match this matcher's pattern
   */
  public final int popTo(Matcher matcher, Appendable appendable) {
    return popTo(matcher, null, appendable);
  }

  /**
   * Read and consume the next characters according to a given regular expression. Moreover the character sequence immediately following the
   * desired characters must also match a given regular expression.
   * 
   * @param matcher
   *          the Matcher used to try consuming next characters
   * @param afterMatcher
   *          the Matcher used to check character sequence immediately following the consumed characters
   * @param appendable
   *          the consumed characters are appended to this appendable
   * @return number of consumed characters or -1 if one of the two Matchers doesn't match
   */
  public final int popTo(Matcher matcher, Matcher afterMatcher, Appendable appendable) {
    try {
      matcher.reset(this);
      if (matcher.lookingAt()) {
        if (afterMatcher != null) {
          afterMatcher.reset(this);
          afterMatcher.region(matcher.end(), length());
          if ( !afterMatcher.lookingAt()) {
            return -1;
          }
        }
        previousCursor = getCursor().clone();
        for (int i = 0; i < matcher.end(); i++) {
          appendable.append((char) pop());
        }
        return matcher.end();
      }
    } catch (IndexOutOfBoundsException e) {
      return -1;
    } catch (IOException e) {
      throw new ChannelException(e.getMessage(), e);
    }
    return -1;
  }

  public final Cursor getPreviousCursor() {
    return previousCursor;
  }
}
