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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.sonar.api.BatchExtension;
import org.sonar.api.ServerExtension;

import java.io.Writer;

/**
 * @since 2.3
 */
public abstract class ProfileExporter implements BatchExtension, ServerExtension {

  private String[] supportedLanguages = new String[0];
  private String key;
  private String name;
  private String mimeType = "plain/text";

  protected ProfileExporter(String key, String name) {
    this.key = key;
    this.name = name;
  }

  public abstract void exportProfile(RulesProfile profile, Writer writer);

  public final String getKey() {
    return key;
  }

  public final ProfileExporter setKey(String s) {
    this.key = s;
    return this;
  }

  public final String getName() {
    return name;
  }

  public final ProfileExporter setName(String s) {
    this.name = s;
    return this;
  }

  protected final ProfileExporter setSupportedLanguages(String... languages) {
    supportedLanguages = (languages != null ? languages : new String[0]);
    return this;
  }

  public final String getMimeType() {
    return mimeType;
  }

  public final ProfileExporter setMimeType(String s) {
    if (StringUtils.isNotBlank(s)) {
      this.mimeType = s;
    }
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
    ProfileExporter that = (ProfileExporter) o;
    if (key != null ? !key.equals(that.key) : that.key != null) {
      return false;
    }
    return true;
  }

  @Override
  public final int hashCode() {
    return key != null ? key.hashCode() : 0;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("key", key)
        .append("name", name)
        .append("mimeType", mimeType)
        .append("languages", supportedLanguages)
        .toString();
  }
}
