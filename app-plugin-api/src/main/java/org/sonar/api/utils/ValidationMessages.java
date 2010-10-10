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
package org.sonar.api.utils;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public final class ValidationMessages {

  private List<String> errors = new ArrayList<String>();
  private List<String> warnings = new ArrayList<String>();
  private List<String> infos = new ArrayList<String>();

  /**
   * Use the factory method <code>create()</code>
   */
  ValidationMessages() {
  }

  public static ValidationMessages create() {
    return new ValidationMessages();
  }

  public boolean hasErrors() {
    return !errors.isEmpty();
  }

  public List<String> getErrors() {
    return errors;
  }

  public ValidationMessages addErrorText(String text) {
    errors.add(text);
    return this;
  }

  public List<String> getWarnings() {
    return warnings;
  }

  public boolean hasWarnings() {
    return !warnings.isEmpty();
  }

  public ValidationMessages addWarningText(String text) {
    warnings.add(text);
    return this;
  }

  public List<String> getInfos() {
    return infos;
  }

  public boolean hasInfos() {
    return !infos.isEmpty();
  }

  public ValidationMessages addInfoText(String text) {
    infos.add(text);
    return this;
  }

  public void log(Logger logger) {
    for (String error : getErrors()) {
      logger.error(error);
    }
    for (String warning : getWarnings()) {
      logger.warn(warning);
    }
    for (String info : getInfos()) {
      logger.info(info);
    }
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
        .append("errors", errors)
        .append("warnings", warnings)
        .append("infos", infos)
        .toString();
  }
}
