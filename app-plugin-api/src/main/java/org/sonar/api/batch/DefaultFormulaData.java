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

import com.google.common.collect.Lists;
import org.sonar.api.measures.FormulaData;
import org.sonar.api.measures.Measure;
import org.sonar.api.measures.MeasuresFilter;
import org.sonar.api.measures.Metric;

import java.util.Collection;
import java.util.List;

/**
 * @since 1.11
 */
public class DefaultFormulaData implements FormulaData {

  private DecoratorContext decoratorContext;

  public DefaultFormulaData(DecoratorContext decoratorContext) {
    this.decoratorContext = decoratorContext;
  }

  public Measure getMeasure(Metric metric) {
    return decoratorContext.getMeasure(metric);
  }

  public <M> M getMeasures(MeasuresFilter<M> filter) {
    return decoratorContext.getMeasures(filter);
  }

  public Collection<Measure> getChildrenMeasures(MeasuresFilter filter) {
    return decoratorContext.getChildrenMeasures(filter);
  }

  public Collection<Measure> getChildrenMeasures(Metric metric) {
    return decoratorContext.getChildrenMeasures(metric);
  }

  public Collection<FormulaData> getChildren() {
    List<FormulaData> result = Lists.newArrayList();
    for (DecoratorContext childContext : decoratorContext.getChildren()) {
      result.add(new DefaultFormulaData(childContext));
    }
    return result;
  }
}
