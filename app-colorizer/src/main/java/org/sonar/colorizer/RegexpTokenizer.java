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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.sonar.channel.CodeReader;

public class RegexpTokenizer extends NotThreadSafeTokenizer{

  private final String tagBefore;
  private final String tagAfter;
  private final Matcher matcher;
  private final StringBuilder tmpBuilder = new StringBuilder();

  /**
   * @param tagBefore
   *          Html tag to add before the token
   * @param tagAfter
   *          Html tag to add after the token
   * @param regexp
   *          Regular expression which must be used to match token
   */
  public RegexpTokenizer(String tagBefore, String tagAfter, String regexp) {
    this.tagBefore = tagBefore;
    this.tagAfter = tagAfter;
    this.matcher = Pattern.compile(regexp).matcher("");
  }

  @Override
  public boolean consume(CodeReader code, HtmlCodeBuilder codeBuilder) {
    if (code.popTo(matcher, tmpBuilder) > 0) {
      codeBuilder.appendWithoutTransforming(tagBefore);
      codeBuilder.append(tmpBuilder);
      codeBuilder.appendWithoutTransforming(tagAfter);
      tmpBuilder.delete(0, tmpBuilder.length());
      return true;
    }
    return false;
  }

  public RegexpTokenizer clone() {
    return new RegexpTokenizer(tagBefore, tagAfter, matcher.pattern().pattern());
  }
}
