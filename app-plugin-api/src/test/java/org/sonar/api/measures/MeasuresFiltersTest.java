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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.RulePriority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class MeasuresFiltersTest {

  @Test
  public void metric() {
    MeasuresFilter<Measure> filter = MeasuresFilters.metric(CoreMetrics.VIOLATIONS);

    Collection<Measure> measures = Arrays.asList(
        RuleMeasure.createForPriority(CoreMetrics.VIOLATIONS, RulePriority.CRITICAL, 50.0),
        new Measure(CoreMetrics.VIOLATIONS, 500.0));

    assertThat(filter.filter(measures).getValue(), is(500.0));
  }

  @Test
  public void all() {
    Collection<Measure> measures = Arrays.asList(
        RuleMeasure.createForPriority(CoreMetrics.VIOLATIONS, RulePriority.CRITICAL, 50.0),
        new Measure(CoreMetrics.VIOLATIONS, 500.0));

    assertThat(MeasuresFilters.all().filter(measures).size(), is(2));
  }

  @Test
  public void rulePriority() {
    MeasuresFilter<RuleMeasure> filter = MeasuresFilters.rulePriority(CoreMetrics.VIOLATIONS, RulePriority.CRITICAL);
    List<Measure> measures = Arrays.asList(
        RuleMeasure.createForPriority(CoreMetrics.VIOLATIONS, RulePriority.CRITICAL, 50.0),
        RuleMeasure.createForPriority(CoreMetrics.VIOLATIONS, RulePriority.BLOCKER, 10.0),
        RuleMeasure.createForPriority(CoreMetrics.COVERAGE, RulePriority.CRITICAL, 400.0),
        new Measure(CoreMetrics.VIOLATIONS, 500.0));

    assertThat(filter.filter(measures).getValue(), is(50.0));
  }

  @Test
  public void rule() {
    Rule rule1 = new Rule("pmd", "key1");
    Rule rule2 = new Rule("pmd", "key2");
    MeasuresFilter<RuleMeasure> filter = MeasuresFilters.rule(CoreMetrics.VIOLATIONS, rule1);
    List<Measure> measures = Arrays.asList(
        RuleMeasure.createForRule(CoreMetrics.VIOLATIONS, rule1, 50.0),
        RuleMeasure.createForRule(CoreMetrics.VIOLATIONS, rule2, 10.0),
        RuleMeasure.createForRule(CoreMetrics.VIOLATIONS_DENSITY, rule2, 3.3),

        RuleMeasure.createForPriority(CoreMetrics.VIOLATIONS, RulePriority.CRITICAL, 400.0),
        RuleMeasure.createForPriority(CoreMetrics.COVERAGE, RulePriority.CRITICAL, 400.0),
        new Measure(CoreMetrics.VIOLATIONS, 500.0));

    assertThat(filter.filter(measures).getValue(), is(50.0));
  }

  @Test
  public void rules() {
    Rule rule1 = new Rule("pmd", "key1");
    Rule rule2 = new Rule("pmd", "key2");
    MeasuresFilter<Collection<RuleMeasure>> filter = MeasuresFilters.rules(CoreMetrics.VIOLATIONS);
    List<Measure> measures = Arrays.asList(
        RuleMeasure.createForRule(CoreMetrics.VIOLATIONS, rule1, 50.0),
        RuleMeasure.createForRule(CoreMetrics.VIOLATIONS, rule2, 10.0),
        RuleMeasure.createForRule(CoreMetrics.VIOLATIONS_DENSITY, rule2, 3.3),

        RuleMeasure.createForPriority(CoreMetrics.VIOLATIONS, RulePriority.CRITICAL, 400.0),
        RuleMeasure.createForPriority(CoreMetrics.COVERAGE, RulePriority.CRITICAL, 400.0),
        new Measure(CoreMetrics.VIOLATIONS, 500.0));

    assertThat(filter.filter(measures).size(), is(2));
  }

  @Test
  public void ruleCategory() {
    MeasuresFilter<RuleMeasure> filter = MeasuresFilters.ruleCategory(CoreMetrics.VIOLATIONS, 2);
    List<Measure> measures = Arrays.asList(
        RuleMeasure.createForPriority(CoreMetrics.VIOLATIONS, RulePriority.CRITICAL, 50.0),
        RuleMeasure.createForCategory(CoreMetrics.VIOLATIONS, 2, 10.0),
        RuleMeasure.createForCategory(CoreMetrics.VIOLATIONS, 3, 15.0),
        new Measure(CoreMetrics.VIOLATIONS, 500.0));

    assertThat(filter.filter(measures).getValue(), is(10.0));
  }

  @Test
  public void measure() {
    MeasuresFilter<Measure> filter = MeasuresFilters.measure(new Measure(CoreMetrics.VIOLATIONS));
    List<Measure> measures = Arrays.asList(
        new Measure(CoreMetrics.COMMENT_LINES, 50.0),
        new Measure(CoreMetrics.VIOLATIONS, 10.0),
        RuleMeasure.createForCategory(CoreMetrics.VIOLATIONS, 2, 12.0),
        new Measure(CoreMetrics.COVERAGE, 15.0));

    assertThat(filter.filter(measures).getValue(), is(10.0));
  }
}
