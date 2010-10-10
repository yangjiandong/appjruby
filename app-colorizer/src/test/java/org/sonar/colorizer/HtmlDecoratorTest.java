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

import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.number.OrderingComparisons.greaterThan;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.junit.internal.matchers.StringContains.containsString;

public class HtmlDecoratorTest {

  @Test
  public void beginOfFileTag() {
    HtmlDecorator decorator = new HtmlDecorator(HtmlOptions.DEFAULT);
    String tag = decorator.getTagBeginOfFile();
    assertContains(tag, "<html", "<style", "<table");
  }

  @Test
  public void beginOfFileTagWithTableId() {
    HtmlOptions options = new HtmlOptions().setGenerateTable(true).setTableId("foo");
    HtmlDecorator decorator = new HtmlDecorator(options);

    String tag = decorator.getTagBeginOfFile();

    assertContains(tag, "<table class=\"code\" id=\"foo\">");
  }

  @Test
  public void beginOfFileTagWithoutHeader() {
    HtmlOptions options = new HtmlOptions().setGenerateTable(true).setGenerateHtmlHeader(false);
    HtmlDecorator decorator = new HtmlDecorator(options);

    String tag = decorator.getTagBeginOfFile();

    assertNotContains(tag, "<html", "<style");
    assertContains(tag, "<table");
  }

  @Test
  public void endOfFileTag() {
    HtmlDecorator decorator = new HtmlDecorator(HtmlOptions.DEFAULT);
    String tag = decorator.getTagEndOfFile();
    assertContains(tag, "</table>", "</body>", "</html>");
  }

  @Test
  public void endOfFileTagWithoutHeader() {
    HtmlOptions options = new HtmlOptions().setGenerateTable(true).setGenerateHtmlHeader(false);
    HtmlDecorator decorator = new HtmlDecorator(options);

    String tag = decorator.getTagEndOfFile();

    assertContains(tag, "</table>");
    assertNotContains(tag, "</body>", "</html>");
  }

  @Test
  public void getCss() {
    assertThat(HtmlDecorator.getCss().length(), greaterThan(100));
    assertThat(HtmlDecorator.getCss(), containsString(".code"));
  }


  public void assertContains(String html, String... strings) {
    for (String string : strings) {
      assertThat(html, containsString(string));
    }
  }

  public void assertNotContains(String html, String... strings) {
    for (String string : strings) {
      assertThat(html, not(containsString(string)));
    }
  }
}
