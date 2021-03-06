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
package org.sonar.test.channel;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.sonar.channel.Channel;
import org.sonar.channel.CodeReader;

public class ChannelMatcher<OUTPUT> extends BaseMatcher<Channel<OUTPUT>> {

  private final String sourceCode;
  private final OUTPUT output;
  private final CodeReader reader;

  public ChannelMatcher(String sourceCode, OUTPUT output) {
    this.sourceCode = sourceCode;
    this.output = output;
    this.reader = new CodeReader(sourceCode);
  }

  public ChannelMatcher(CodeReader reader, OUTPUT output) {
    this.output = output;
    this.sourceCode = new String(reader.peek(30));
    this.reader = reader;
  }

  public boolean matches(Object arg0) {
    if ( !(arg0 instanceof Channel)) {
      return false;
    }
    Channel<OUTPUT> channel = (Channel<OUTPUT>) arg0;
    return channel.consume(reader, output);
  }

  public void describeTo(Description description) {
    description.appendText("Channel consumes '" + sourceCode + "'");
  }

}
