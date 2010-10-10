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
package org.sonar.api.rules;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.sonar.api.ServerExtension;

import java.util.List;

/**
 * @since 2.3
 */
public abstract class RuleRepository implements ServerExtension {

  private String key;
  private String language;
  private String name;

  protected RuleRepository(String key, String language) {
    this.key = key;
    this.language = language;
  }

  public final String getKey() {
    return key;
  }

  public final String getLanguage() {
    return language;
  }

  public final String getName() {
    return name;
  }

  public final String getName(boolean useKeyIfEmpty) {
    if (useKeyIfEmpty) {
      return StringUtils.defaultIfEmpty(name, key);
    }
    return name;
  }

  public final RuleRepository setName(String s) {
    this.name = s;
    return this;
  }

  public abstract List<Rule> createRules();

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("key", key)
        .append("language", language)
        .append("name", name)
        .toString();
  }
}
