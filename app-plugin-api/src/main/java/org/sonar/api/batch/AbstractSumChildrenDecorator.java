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
package org.sonar.api.batch;

import org.sonar.api.measures.Measure;
import org.sonar.api.measures.MeasureUtils;
import org.sonar.api.measures.Metric;
import org.sonar.api.resources.Project;
import org.sonar.api.resources.Resource;

import java.util.List;

/**
 * Sum measures of child resources.
 *
 * @since 1.10
 */
public abstract class AbstractSumChildrenDecorator implements Decorator {


  /**
   * Each metric is used individually. There are as many generated measures than metrics.
   * <p/>
   * <p><b>Important</b> : annotations are not inherited, so you have to copy the @DependedUpon annotation
   * when implementing this method.</p>
   *
   * @return not null list of metrics
   */
  @DependedUpon
  public abstract List<Metric> generatesMetrics();

  /**
   * {@inheritDoc}
   */
  public boolean shouldExecuteOnProject(Project project) {
    return true;
  }

  /**
   * @return whether it should save zero if no child measures
   */
  protected abstract boolean shouldSaveZeroIfNoChildMeasures();

  /**
   * {@inheritDoc}
   */
  public void decorate(Resource resource, DecoratorContext context) {
    if (!shouldDecorateResource(resource)) {
      return;
    }
    for (Metric metric : generatesMetrics()) {
      if (context.getMeasure(metric) == null) {
        Double sum = MeasureUtils.sum(shouldSaveZeroIfNoChildMeasures(), context.getChildrenMeasures(metric));
        if (sum != null) {
          context.saveMeasure(new Measure(metric, sum));
        }
      }
    }
  }

  /**
   * @return whether the resource should be decorated or not
   */
  public boolean shouldDecorateResource(Resource resource) {
    return true;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName();
  }
}
