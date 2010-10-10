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

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static junit.framework.Assert.assertNull;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SumChildDistributionFormulaTest {
  SumChildDistributionFormula formula;
  FormulaContext context;
  FormulaData data;

  @Before
  public void init() {
    formula = new SumChildDistributionFormula();
    context = mock(FormulaContext.class);
    data = mock(FormulaData.class);
  }

  @Test
  public void testWhenGetChildrenReturnsNull() {
    when(context.getTargetMetric()).thenReturn(new Metric("foo"));
    when(data.getChildrenMeasures(new Metric("foo"))).thenReturn(null);
    assertNull(formula.calculate(data, context));
  }

  @Test
  public void testWhenGetChildrenReturnsEmpty() {
    when(context.getTargetMetric()).thenReturn(new Metric("foo"));
    when(data.getChildrenMeasures(new Metric("foo"))).thenReturn((List) Collections.emptyList());
    assertNull(formula.calculate(data, context));
  }

  @Test
  public void doNotSumDifferentRanges() {
    Metric m = new Metric("foo", Metric.ValueType.DATA);
    when(context.getTargetMetric()).thenReturn(m);

    List<Measure> list = Lists.newArrayList(
      new Measure(m, "1=0;2=2;5=0;10=10;20=2"),
      new Measure(m, "1=0;2=2;5=0;10=10;30=3")
    );
    when(data.getChildrenMeasures(new Metric("foo"))).thenReturn(list);
    assertThat(formula.calculate(data, context), nullValue());
  }

  @Test
  public void sumSameIntRanges() {
    Metric m = new Metric("foo", Metric.ValueType.DATA);
    when(context.getTargetMetric()).thenReturn(m);

    List<Measure> list = Lists.newArrayList(
      new Measure(m, "1=0;2=2;5=0;10=10;20=2"),
      new Measure(m, "1=3;2=2;5=3;10=12;20=0")
    );
    when(data.getChildrenMeasures(new Metric("foo"))).thenReturn(list);
    assertThat(formula.calculate(data, context).getData(), is("1=3;2=4;5=3;10=22;20=2"));
  }

  @Test
  public void sumSameDoubleRanges() {
    Metric m = new Metric("foo", Metric.ValueType.DATA);
    when(context.getTargetMetric()).thenReturn(m);

    List<Measure> list = Lists.newArrayList(
      new Measure(m, "0.5=0;2.5=2"),
      new Measure(m, "0.5=3;2.5=4")
    );
    when(data.getChildrenMeasures(new Metric("foo"))).thenReturn(list);
    assertThat(formula.calculate(data, context).getData(), is("0.5=3;2.5=6"));
  }
}
