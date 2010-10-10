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

import org.sonar.api.BatchExtension;
import org.sonar.api.resources.Project;

/**
 * @since 1.10
 */
public interface MavenPluginHandler extends BatchExtension {

  /**
   * The plugin group id
   *
   * @return the group id
   */
  String getGroupId();

  /**
   * The plugin artifact id
   *
   * @return artifact id
   */
  String getArtifactId();

  /**
   * The fixed plugin version to execute
   *
   * @return the plugin version
   */
  String getVersion();

  /**
   * Indicates if the plugin version should be fixed or not, it means that if your pom defines another version
   * than the one defined by getVersion(), this version will be used
   *
   * @return true if the version should be fixed
   */
  boolean isFixedVersion();

  /**
   * The maven goals to execute
   *
   * @return an array of goals
   */
  String[] getGoals();

  /**
   * Configures the pom being executed, add or remove plugin properties.
   * This method is automatically executed by Sonar. Plugins do NOT have to execute it.
   */
  void configure(Project project, MavenPlugin plugin);

}
