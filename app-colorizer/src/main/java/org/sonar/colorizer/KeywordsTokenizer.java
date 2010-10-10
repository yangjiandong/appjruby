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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.channel.CodeReader;

/**
 * Detect case-sensitive keywords
 */
public class KeywordsTokenizer extends NotThreadSafeTokenizer {

  private final String tagBefore;
  private final String tagAfter;
  private boolean caseInsensitive = false;
  private Matcher matcher;
  private final StringBuilder tmpBuilder = new StringBuilder();
  private final static String defaultRegex = "[a-zA-Z_][a-zA-Z0-9_]*+";

  private Set<String> keywords = new HashSet<String>();

  public KeywordsTokenizer(String tagBefore, String tagAfter, Set<String> keywords) {
    this(tagBefore, tagAfter, keywords, defaultRegex);
  }

  public KeywordsTokenizer(String tagBefore, String tagAfter, Set<String> keywords, String regex) {
    this.tagBefore = tagBefore;
    this.tagAfter = tagAfter;
    this.keywords = keywords;
    this.matcher = Pattern.compile(regex).matcher("");
  }

  public KeywordsTokenizer(String tagBefore, String tagAfter, String... keywords) {
    this.tagBefore = tagBefore;
    this.tagAfter = tagAfter;
    Collections.addAll(this.keywords, keywords);
    this.matcher = Pattern.compile(defaultRegex).matcher("");
  }

  public boolean consume(CodeReader code, HtmlCodeBuilder codeBuilder) {
    if (code.popTo(matcher, tmpBuilder) > 0) {
      if (isKeyword(tmpBuilder.toString())) {
        codeBuilder.appendWithoutTransforming(tagBefore);
        codeBuilder.append(tmpBuilder);
        codeBuilder.appendWithoutTransforming(tagAfter);
      } else {
        codeBuilder.append(tmpBuilder);
      }
      tmpBuilder.delete(0, tmpBuilder.length());
      return true;
    }
    return false;
  }

  private boolean isKeyword(String word) {
    if ( !caseInsensitive && keywords.contains(word)) {
      return true;
    } else if (caseInsensitive && keywords.contains(word.toUpperCase())) {
      return true;
    }
    return false;
  }

  public void setCaseInsensitive(boolean caseInsensitive) {
    this.caseInsensitive = caseInsensitive;
  }

  public KeywordsTokenizer clone() {
    KeywordsTokenizer clone = new KeywordsTokenizer(tagBefore, tagAfter, keywords, matcher.pattern().pattern());
    clone.caseInsensitive = caseInsensitive;
    return clone;
  }
}
