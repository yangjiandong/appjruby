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
package org.sonar.api;

import static org.junit.Assert.assertEquals;

public abstract class BaseModelTestCase {

  protected String overFillString(int maxSize) {
    StringBuilder overFilled = new StringBuilder();
    for (int i = 0; i < 50 + maxSize; i++) {
      overFilled.append("x");
    }
    return overFilled.toString();
  }

  protected void assertAbbreviated(int maxSize, String value) {
    assertEquals(maxSize, value.length());
    assertEquals('.', value.charAt(maxSize - 1));
  }
}
