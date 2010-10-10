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
import org.sonar.squid.api.SourcePackage;

import static org.junit.Assert.assertEquals;

public class DistanceFormulaTest {

  DistanceFormula distance   = new DistanceFormula();
  Measurable      measurable = new SourcePackage("pac1");

  @Test
  public void calculateBestDistance() {
    measurable.setMeasure(Metric.CLASSES, 5);
    measurable.setMeasure(Metric.INTERFACES, 5);
    measurable.setMeasure(Metric.CA, 10);
    measurable.setMeasure(Metric.CE, 10);
    assertEquals(0.5, measurable.getDouble(Metric.DISTANCE), 0.01);
  }
  
  @Test
  public void calculateWorstDistance() {
    measurable.setMeasure(Metric.CLASSES, 5);
    measurable.setMeasure(Metric.CA, 10);
    measurable.setMeasure(Metric.CE, 0);
    assertEquals(1, measurable.getDouble(Metric.DISTANCE), 0.01);
  }

}
