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
package org.sonar.api.web;

import org.sonar.api.ServerExtension;
import org.sonar.channel.Channel;
import org.sonar.colorizer.HtmlCodeBuilder;
import org.sonar.colorizer.Tokenizer;

import java.util.List;

/**
 * Extend the library sonar-colorizer to support new languages. By default only Java sources are colorized in Sonar.
 *
 * @since 1.12
 */
public abstract class CodeColorizerFormat implements ServerExtension {

  private String languageKey;

  /**
   * @param languageKey the unique sonar key. Not null.
   */
  protected CodeColorizerFormat(String languageKey) {
    this.languageKey = languageKey;
  }

  public final String getLanguageKey() {
    return languageKey;
  }

  /**
   * sonar-colorizer tokenizers for HTML output.
   * @return a not null list (empty if no tokenizers) 
   */
  public abstract List<Tokenizer> getTokenizers();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof CodeColorizerFormat)) {
      return false;
    }

    CodeColorizerFormat format = (CodeColorizerFormat) o;
    return languageKey.equals(format.languageKey);

  }

  @Override
  public int hashCode() {
    return languageKey.hashCode();
  }
}
