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

import org.apache.commons.lang.StringUtils;

import java.util.Collection;

/**
 * An utility class to manipulate measures
 *
 * @since 1.10
 */
public final class MeasureUtils {

  /**
   * Class cannot be instantiated, it should only be access through static methods
   */
  private MeasureUtils() {
  }

  /**
   * Return true if all measures have numeric value
   *
   * @param measures the measures
   * @return true if all measures numeric values
   */
  public static boolean haveValues(Measure... measures) {
    if (measures == null || measures.length == 0) {
      return false;
    }
    for (Measure measure : measures) {
      if (!hasValue(measure)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Get the value of a measure, or alternatively a default value
   *
   * @param measure the measure
   * @param defaultValue the default value
   * @return  <code>defaultValue</code> if measure is null or has no values.
   */

  public static Double getValue(Measure measure, Double defaultValue) {
    if (MeasureUtils.hasValue(measure)) {
      return measure.getValue();
    }
    return defaultValue;
  }

  /**
   * Tests if a measure has a value
   *
   * @param measure the measure
   * @return whether the measure has a value
   */
  public static boolean hasValue(Measure measure) {
    return measure != null && measure.getValue() != null;
  }

  /**
   * Tests if a measure has a data field
   *
   * @param measure the measure
   * @return whether the measure has a data field
   */
  public static boolean hasData(Measure measure) {
    return measure != null && StringUtils.isNotBlank(measure.getData());
  }

  /**
   * Sums a series of measures
   *
   * @param zeroIfNone whether to return 0 or null in case measures is null
   * @param measures the series of measures
   * @return the sum of the measure series
   */
  public static Double sum(boolean zeroIfNone, Collection<Measure> measures) {
    if (measures != null) {
      return sum(zeroIfNone, measures.toArray(new Measure[measures.size()]));
    }
    return zeroIfNone(zeroIfNone);
  }

  /**
   * Sums a series of measures
   *
   * @param zeroIfNone whether to return 0 or null in case measures is null
   * @param measures the series of measures
   * @return the sum of the measure series
   */
  public static Double sum(boolean zeroIfNone, Measure... measures) {
    if (measures == null) {
      return zeroIfNone(zeroIfNone);
    }
    Double sum = 0d;
    boolean hasValue = false;
    for (Measure measure : measures) {
      if (measure != null && measure.getValue() != null) {
        hasValue = true;
        sum += measure.getValue();
      }
    }

    if (hasValue) {
      return sum;
    }
    return zeroIfNone(zeroIfNone);
  }

  private static Double zeroIfNone(boolean zeroIfNone) {
    return zeroIfNone ? 0d : null;
  }
}
