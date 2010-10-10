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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

public class SumChildValuesFormulaTest {
  private FormulaContext context;
  private FormulaData data;

  @Before
  public void before() {
    context = mock(FormulaContext.class);
    data = mock(FormulaData.class);
  }

  @Test
  public void sumChildValues() {
    when(context.getTargetMetric()).thenReturn(CoreMetrics.NCLOC);
    when(data.getChildrenMeasures(CoreMetrics.NCLOC)).thenReturn(
        Arrays.<Measure>asList(new Measure(CoreMetrics.NCLOC, 100.0), new Measure(CoreMetrics.NCLOC, 50.0)));

    Measure measure = new SumChildValuesFormula(true).calculate(data, context);

    assertThat(measure.getMetric(), is(CoreMetrics.NCLOC));
    assertThat(measure.getValue(), is(150.0));
  }

  @Test
  public void doNotInsertZero() {
    when(context.getTargetMetric()).thenReturn(CoreMetrics.NCLOC);
    when(data.getChildrenMeasures(CoreMetrics.NCLOC)).thenReturn(Collections.<Measure>emptyList());

    Measure measure = new SumChildValuesFormula(false).calculate(data, context);

    assertThat(measure, nullValue());
  }

  @Test
  public void doInsertZero() {
    when(context.getTargetMetric()).thenReturn(CoreMetrics.NCLOC);
    when(data.getChildrenMeasures(CoreMetrics.NCLOC)).thenReturn(Collections.<Measure>emptyList());

    Measure measure = new SumChildValuesFormula(true).calculate(data, context);

    assertThat(measure.getMetric(), is(CoreMetrics.NCLOC));
    assertThat(measure.getValue(), is(0.0));
  }
}
