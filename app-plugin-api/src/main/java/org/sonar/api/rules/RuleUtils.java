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

import java.util.Map;

import org.apache.commons.configuration.Configuration;
import org.sonar.api.CoreProperties;
import org.sonar.api.utils.KeyValueFormat;

/**
 * A utility class to manipulate concepts around rules
 */
public final class RuleUtils {

  private RuleUtils() {
  }

  /**
   * Gets a Map<RulePriority, Integer> containing the weights defined in the settings
   * Default value is used when the property is not set (see property key and default value in the class CoreProperties)
   *
   * @param configuration the Sonar configuration
   * @return a map
   */
  public static Map<RulePriority, Integer> getPriorityWeights(final Configuration configuration) {
    String levelWeight = configuration.getString(CoreProperties.CORE_RULE_WEIGHTS_PROPERTY, CoreProperties.CORE_RULE_WEIGHTS_DEFAULT_VALUE);

    Map<RulePriority, Integer> weights = KeyValueFormat.parse(levelWeight, new KeyValueFormat.RulePriorityNumbersPairTransformer());

    for (RulePriority priority : RulePriority.values()) {
      if (!weights.containsKey(priority)) {
        weights.put(priority, 1);
      }
    }
    return weights;
  }
}
