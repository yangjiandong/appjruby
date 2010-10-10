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
package org.sonar.api.profiles;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.sonar.api.ServerExtension;
import org.sonar.api.utils.ValidationMessages;

import java.io.Reader;

/**
 * @since 2.3
 */
public abstract class ProfileImporter implements ServerExtension {

  private String[] supportedLanguages = new String[0];
  private String importerKey;
  private String importerName;

  protected ProfileImporter(String key, String name) {
    this.importerKey = key;
    this.importerName = name;
  }

  public abstract RulesProfile importProfile(Reader reader, ValidationMessages messages);

  public final String getKey() {
    return importerKey;
  }

  public final ProfileImporter setKey(String s) {
    this.importerKey = s;
    return this;
  }

  public final String getName() {
    return importerName;
  }

  public final ProfileImporter setName(String s) {
    this.importerName = s;
    return this;
  }

  protected final ProfileImporter setSupportedLanguages(String... languages) {
    supportedLanguages = (languages != null ? languages : new String[0]);
    return this;
  }

  /**
   * @return if empty, then any languages are supported.
   */
  public final String[] getSupportedLanguages() {
    return supportedLanguages;
  }

  @Override
  public final boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ProfileImporter that = (ProfileImporter) o;
    if (importerKey != null ? !importerKey.equals(that.importerKey) : that.importerKey != null) {
      return false;
    }
    return true;
  }

  @Override
  public final int hashCode() {
    return importerKey != null ? importerKey.hashCode() : 0;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("key", importerKey)
        .append("name", importerName)
        .append("languages", supportedLanguages)
        .toString();
  }
}
