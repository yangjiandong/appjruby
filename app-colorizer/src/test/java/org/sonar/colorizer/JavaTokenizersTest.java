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

import java.util.List;

import org.junit.Test;

import static junit.framework.Assert.fail;

import static org.hamcrest.number.OrderingComparisons.greaterThan;
import static org.hamcrest.number.OrderingComparisons.lessThan;
import static org.junit.Assert.assertThat;

public class JavaTokenizersTest {

  @Test
  public void forHtml() {
    assertThat(JavaTokenizers.forHtml().size(), greaterThan(3));
  }

  @Test
  public void javadocIsDefinedBeforeCppComment() {
    // just because /** must be detected before /*
    assertThat(indexOf(JavaTokenizers.forHtml(), JavadocTokenizer.class),
        lessThan(indexOf(JavaTokenizers.forHtml(), CppDocTokenizer.class)));
  }

  private Integer indexOf(List<Tokenizer> tokenizers, Class tokenizerClass) {
    for (int i = 0; i < tokenizers.size(); i++) {
      if (tokenizers.get(i).getClass().equals(tokenizerClass)) {
        return i;
      }
    }

    fail("Tokenizer not found: " + tokenizerClass);
    return null;
  }
}
