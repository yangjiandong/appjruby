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

import org.sonar.api.BatchExtension;
import org.sonar.api.ServerExtension;

/**
 * The extension point to define a new language
 *
 * @since 1.10
 */
public interface Language extends BatchExtension, ServerExtension {

  /**
   * For example "java". Should not be more than 5 chars.
   */
  String getKey();

  /**
   * For example "Java"
   */
  String getName();

  /**
   * For example ["jav","java"]. If empty, then all files in source directories are considered as sources.
   */
  String[] getFileSuffixes();

}
