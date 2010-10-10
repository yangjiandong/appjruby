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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.number.OrderingComparisons.greaterThan;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

public class CodeColorizerTest {

  @Test
  public void javaToHtml() throws IOException {
    Reader java = readFile("/org/sonar/colorizer/samples/Sample.java");

    String html = CodeColorizer.javaToHtml(java, HtmlOptions.DEFAULT);

    assertHtml(html);
    assertContains(html, "<pre><span class=\"k\">public</span> <span class=\"k\">class</span> Sample {</pre>");
  }

  @Test
  public void useHtmlOptions() throws IOException {
    Reader java = readFile("/org/sonar/colorizer/samples/Sample.java");

    HtmlOptions options = new HtmlOptions(true, "my-table-id", false);
    String html = CodeColorizer.javaToHtml(java, options);

    assertThat(html, containsString("<table class=\"code\" id=\"my-table-id\""));
    assertThat(html, not(containsString("<style")));
  }

  @Test
  public void groovyToHtml() throws IOException {
    Reader groovy = readFile("/org/sonar/colorizer/samples/Sample.groovy");

    String html = CodeColorizer.groovyToHtml(groovy, HtmlOptions.DEFAULT);

    assertHtml(html);
    assertContains(html, "<pre><span class=\"k\">class</span> Greet {</pre>");
  }

  @Test
  public void getCss() {
    assertThat(CodeColorizer.getCss().length(), greaterThan(100));
    assertThat(CodeColorizer.getCss(), containsString(".code"));
  }

  @Test
  public void mustBeThreadsafe() throws FileNotFoundException, InterruptedException, ExecutionException {
    final int taskCount = 50;
    final int threadCount = 5;

    class ColorizerTask implements Callable<String> {

      Reader java;

      ColorizerTask() throws FileNotFoundException {
        this.java = readFile("/org/sonar/colorizer/samples/Sample.java");
      }

      public String call() throws Exception {
        return CodeColorizer.javaToHtml(java, HtmlOptions.ONLY_SYNTAX);
      }
    }

    Collection<Callable<String>> tasks = new ArrayList<Callable<String>>();
    for (int i = 0; i < taskCount; i++) {
      tasks.add(new ColorizerTask());
    }
    List<Future<String>> futures = Executors.newFixedThreadPool(threadCount).invokeAll(tasks);

    assertThat(futures.size(), is(taskCount));

    // all html must be the same
    String html = futures.get(0).get();
    for (Future<String> future : futures) {
      assertEquals(html, future.get());
    }
  }

  private FileReader readFile(String path) throws FileNotFoundException {
    return new FileReader(FileUtils.toFile(getClass().getResource(path)));
  }

  private void assertHtml(String html) {
    assertContains(html, "<style", "<table class=\"code\"", "</html>");
  }

  private void assertContains(String html, String... strings) {
    for (String string : strings) {
      assertThat(html, containsString(string));
    }
  }
}
