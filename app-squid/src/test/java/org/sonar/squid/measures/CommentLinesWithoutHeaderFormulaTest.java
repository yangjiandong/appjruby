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
package org.sonar.squid.measures;

import org.junit.Test;
import org.sonar.squid.api.SourceClass;

import static org.junit.Assert.assertEquals;

public class CommentLinesWithoutHeaderFormulaTest {

  CommentLinesWithoutHeaderFormula formula    = new CommentLinesWithoutHeaderFormula();
  Measurable                       measurable = new SourceClass("com.Toto");

  @Test
  public void calculateDensityOnEmptyFile() {
    measurable.setMeasure(Metric.COMMENT_LINES, 10);
    measurable.setMeasure(Metric.HEADER_COMMENT_LINES, 5);
    assertEquals(5, measurable.getInt(Metric.COMMENT_LINES_WITHOUT_HEADER));
  }
}
