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

/**
 * Inherit this class to define a new language like PLSQL, PHP or C#
 *
 * @since 1.10
 */
public abstract class AbstractLanguage implements Language {
  private final String key;
  private String name;

  /**
   * Better to use AbstractLanguage(key, name). In this case, key and name will be the same
   */
  public AbstractLanguage(String key) {
    this(key, key);
  }

  /**
   * Should be the constructor used to build an AbstractLanguage.
   *
   * @param key the key that will be used to retrieve the language. This key is important as it will be used to teint rules repositories...
   * @param name the display name of the language in the interface
   */
  public AbstractLanguage(String key, String name) {
    this.key = key.toLowerCase();
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  public String getKey() {
    return key;
  }

  /**
   * {@inheritDoc}
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the language name
   */
  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Language language = (Language) o;
    return !(key != null ? !key.equals(language.getKey()) : language.getKey() != null);

  }

  @Override
  public int hashCode() {
    return (key != null ? key.hashCode() : 0);
  }

  @Override
  public String toString() {
    return name;
  }
}
