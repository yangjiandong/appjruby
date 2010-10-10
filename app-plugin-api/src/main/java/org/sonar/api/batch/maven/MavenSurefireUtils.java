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
package org.sonar.api.batch.maven;

import org.sonar.api.resources.Project;

/**
 * @since 1.10
 */
public final class MavenSurefireUtils {

  public static final String GROUP_ID = MavenUtils.GROUP_ID_APACHE_MAVEN;
  public static final String ARTIFACT_ID = "maven-surefire-plugin";
  public static final String VERSION = "2.4.3";

  private MavenSurefireUtils() {
  }

  /**
   * Configures the project POM with base required surefire settings
   *
   * @param project the project currently analyzed
   * @return the configured surefire MavenPlugin object instance, cannot be null
   */
  public static MavenPlugin configure(Project project) {
    MavenPlugin surefire = MavenPlugin.registerPlugin(project.getPom(), GROUP_ID, ARTIFACT_ID, VERSION, false);
    surefire.setParameter("disableXmlReport", "false");
    surefire.setParameter("testFailureIgnore", "true");
    return surefire;
  }

}
