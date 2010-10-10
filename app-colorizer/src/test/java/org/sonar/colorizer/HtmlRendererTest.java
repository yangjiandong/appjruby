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

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.sonar.channel.Channel;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.internal.matchers.StringContains.containsString;

public class HtmlRendererTest {

  private KeywordsTokenizer javaKeywordTokenizer = new KeywordsTokenizer("<span class='k'>", "</span>", JavaKeywords.get());

  @Test
  public void renderJavaSyntax() {
    String html = new HtmlRenderer(HtmlOptions.ONLY_SYNTAX).render(new StringReader("public class Hello {"), Arrays
        .<Channel<HtmlCodeBuilder>> asList(javaKeywordTokenizer));

    assertThat(html, is("<span class='k'>public</span> <span class='k'>class</span> Hello {"));
  }

  @Test
  public void supportHtmlSpecialCharacters() {
    String html = new HtmlRenderer(HtmlOptions.ONLY_SYNTAX).render(new StringReader("foo(\"<html>\");"), Arrays
        .<Channel<HtmlCodeBuilder>> asList(new LiteralTokenizer("<s>", "</s>")));

    assertThat(html, is("foo(<s>\"&lt;html&gt;\"</s>);"));
  }

  @Test
  public void renderJavaFile() throws IOException {
    File java = FileUtils.toFile(getClass().getResource("/org/sonar/colorizer/HtmlRendererTest/Sample.java"));

    String html = new HtmlRenderer().render(new FileReader(java), Arrays.<Channel<HtmlCodeBuilder>> asList(javaKeywordTokenizer));

    assertThat(html, containsString("<html>"));
    assertThat(html, containsString("<style"));
    assertThat(html, containsString("<table class=\"code\""));
    assertThat(html, containsString("public"));
    assertThat(html, containsString("class"));
    assertThat(html, containsString("Sample"));
    assertThat(html, containsString("</html>"));
  }

}
