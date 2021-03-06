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
package org.sonar.api.database.model;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import org.sonar.api.measures.CoreMetrics;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

public class MeasureModelTest {

  @Test
  public void doNotCopyDataWhenCloning() {
    MeasureModel initial = new MeasureModel();
    initial.setMetric(CoreMetrics.CLASS_COMPLEXITY_DISTRIBUTION);
    initial.setMeasureData(new MeasureData(initial, "foo"));
    initial.setValue(30.0);
    assertThat(initial.getData(), is("foo"));

    MeasureModel clone = (MeasureModel) initial.clone();
    assertThat(clone.getData(), nullValue());
    assertThat(clone.getValue(), is(30.0));
    assertThat(clone.getMetric(), is(CoreMetrics.CLASS_COMPLEXITY_DISTRIBUTION));
  }
}
