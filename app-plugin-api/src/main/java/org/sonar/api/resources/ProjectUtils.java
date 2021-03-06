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
package org.sonar.api.resources;

import org.sonar.api.batch.maven.MavenUtils;

/**
 * @since 1.10
 */
public final class ProjectUtils {

  private ProjectUtils() {
    // utility class with only static methods
  }

  /**
   * Java version as defined in maven-compiler-plugin
   */
  public static String getJavaVersion(Project project) {
    return MavenUtils.getJavaVersion(project.getPom());
  }

  public static String getJavaSourceVersion(Project project) {
    return MavenUtils.getJavaSourceVersion(project.getPom());
  }
}
