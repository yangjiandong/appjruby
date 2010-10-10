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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.sonar.api.utils.WildcardPattern;

/**
 * @since 1.10
 */
public class Directory extends Resource {

  public static final String SEPARATOR = "/";
  public static final String ROOT = "[root]";

  private Language language;

  public Directory(String key) {
    this(key, null);
  }

  public Directory(String key, Language language) {
    setKey(parseKey(key));
    this.language = language;
  }

  public String getName() {
    return getKey();
  }

  public String getLongName() {
    return null;
  }

  public String getDescription() {
    return null;
  }

  public Language getLanguage() {
    return language;
  }

  public String getScope() {
    return Resource.SCOPE_SPACE;
  }

  public String getQualifier() {
    return Resource.QUALIFIER_DIRECTORY;
  }

  public Resource getParent() {
    return null;
  }

  public boolean matchFilePattern(String antPattern) {
    WildcardPattern matcher = WildcardPattern.create(antPattern, "/");
    return matcher.match(getKey());
  }

  public static String parseKey(String key) {
    if (StringUtils.isBlank(key)) {
      return ROOT;
    }

    key = key.replace('\\', '/');
    key = StringUtils.trim(key);
    key = StringUtils.removeStart(key, Directory.SEPARATOR);
    key = StringUtils.removeEnd(key, Directory.SEPARATOR);
    return key;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("key", getKey())
        .append("language", language)
        .toString();
  }
}
