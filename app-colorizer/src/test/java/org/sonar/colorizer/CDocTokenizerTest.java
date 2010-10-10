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

import org.junit.Before;
import org.junit.Test;
import org.sonar.channel.CodeReader;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class CDocTokenizerTest {

  private HtmlCodeBuilder codeBuilder;

  @Before
  public void init() {
    codeBuilder = new HtmlCodeBuilder();
  }

  @Test
  public void testRead() {
    CDocTokenizer tokenizer = new CDocTokenizer("<c>", "</c>");
    assertTrue(tokenizer.consume(new CodeReader("//this is a comment"), codeBuilder));
    assertThat(codeBuilder.toString(), is("<c>//this is a comment</c>"));

    assertFalse(tokenizer.consume(new CodeReader("this is not a comment"), codeBuilder));
    assertThat(codeBuilder.toString(), is("<c>//this is a comment</c>"));
  }
}
