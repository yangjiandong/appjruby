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
package org.sonar.squid.recognizer;

public abstract class Detector {

  public final double probability;

  public Detector(double probability) {
    if (probability < 0 || probability > 1) {
      throw new IllegalArgumentException("probability should be between [0 .. 1]");
    }
    this.probability = probability;
  }

  public abstract int scan(String line);

  public final double recognition(String line) {
    int matchers = scan(line);
    if (matchers == 0) {
      return 0;
    }
    return 1 - Math.pow(1 - probability, scan(line));
  }
}
