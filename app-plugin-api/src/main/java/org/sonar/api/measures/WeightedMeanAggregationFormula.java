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

import java.util.Collections;
import java.util.List;

/**
 * @since 2.0
 */
public class WeightedMeanAggregationFormula implements Formula {

  private Metric weightingMetric;
  private boolean zeroIfNoValues=false;

  public WeightedMeanAggregationFormula(Metric weightingMetric, boolean zeroIfNoValues) {
    this.weightingMetric = weightingMetric;
    if (weightingMetric==null) {
      throw new IllegalArgumentException("Metric can not be null");
    }
    this.zeroIfNoValues = zeroIfNoValues;
  }

  public List<Metric> dependsUponMetrics() {
    return Collections.emptyList();
  }

  public Measure calculate(FormulaData data, FormulaContext context) {
    double sum=0.0;
    double count=0.0;
    boolean hasValue=false;

    for (FormulaData child : data.getChildren()) {
      Measure measure = child.getMeasure(context.getTargetMetric());
      Measure weightingMeasure = child.getMeasure(weightingMetric);
      if (MeasureUtils.haveValues(measure, weightingMeasure)) {
        sum += (measure.getValue() * weightingMeasure.getValue());
        count += weightingMeasure.getValue();
        hasValue=true;
      }
    }

    if (!hasValue && !zeroIfNoValues) {
      return null;
    }

    double result = (count==0.0 ? 0.0 : sum/count);
    return new Measure(context.getTargetMetric(), result);
  }
}

