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

import java.util.List;

/**
 * A plugin is a group of extensions. See <code>org.sonar.api.Extension</code> interface to get all extension points.
 * <p/>
 * <p>The manifest property <code>Plugin-Class</code> must declare the name of the implementation class.
 * See META-INF/MANIFEST.MF.</p>
 *
 * @see org.sonar.api.Extension
 * @since 1.10
 */
public interface Plugin {

  /**
   * Unique key within sonar plugins
   * @deprecated since 2.2. The key must be set in the manifest.
   */
  @Deprecated
  String getKey();

  /**
   * Descriptive name
   * @deprecated since 2.2. The name must be set in the manifest.
   */
  @Deprecated
  String getName();

  /**
   * description of the plugin, can contains html or ruby code
   * @deprecated since 2.2. The description must be set in the manifest.
   */
  @Deprecated
  String getDescription();

  /**
   * Classes of the implemented extensions.
   */
  List getExtensions();

}
