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
package org.sonar.api.rules;

import org.sonar.check.IsoCategory;
import org.sonar.check.RuleProperty;

@org.sonar.check.Rule(key = "overridden_key", name = "Check with parameters", description = "Has parameters", isoCategory = IsoCategory.Efficiency)
public class AnnotatedCheckWithParameters {

  @RuleProperty(description = "Maximum value")
  private String max;

  @RuleProperty(key = "overridden_min", description = "Minimum value")
  protected String min;

  private int nonConfigurableProperty;

  public String getMax() {
    return max;
  }

  public void setMax(String max) {
    this.max = max;
  }
}
