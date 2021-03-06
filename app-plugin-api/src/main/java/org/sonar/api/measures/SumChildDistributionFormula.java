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
package org.sonar.api.measures;

import java.util.List;
import java.util.Collections;
import java.util.Collection;

/**
 * @since 2.0
 *
 * Used to consolidate a distribution measure throughout the resource tree
 */
public class SumChildDistributionFormula implements Formula {

  public List<Metric> dependsUponMetrics() {
    return Collections.emptyList();
  }

  public Measure calculate(FormulaData data, FormulaContext context) {
    Collection<Measure> measures = data.getChildrenMeasures(context.getTargetMetric());
    if (measures == null || measures.isEmpty()) {
      return null;
    }
    else {
      RangeDistributionBuilder distribution = new RangeDistributionBuilder(context.getTargetMetric());
      for (Measure measure : measures) {
        distribution.add(measure);
      }
      return distribution.build();
    }
  }
}
