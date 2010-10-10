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
package org.sonar.api.test;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.sonar.api.resources.Resource;
import org.sonar.api.rules.Rule;
import org.sonar.api.rules.Violation;

public class IsViolation extends BaseMatcher<Violation> {

  private Rule rule;
  private String message;
  private Resource resource;
  private Integer lineId;

  public IsViolation(Violation wanted) {
    this.lineId = wanted.getLineId();
    this.message = wanted.getMessage();
    this.resource = wanted.getResource();
    this.rule = wanted.getRule();
  }

  public IsViolation(Rule rule, String message, Resource resource, Integer lineId) {
    this.rule = rule;
    this.message = message;
    this.resource = resource;
    this.lineId = lineId;
  }

  public boolean matches(Object o) {
    Violation violation = (Violation) o;
    if (lineId != null && !lineId.equals(violation.getLineId())) {
      return false;
    }

    if (message != null && !message.equals(violation.getMessage())) {
      return false;
    }

    if (resource != null && !resource.equals(violation.getResource())) {
      return false;
    }

    if (rule != null && !rule.equals(violation.getRule())) {
      return false;
    }

    return true;
  }

  public void describeTo(Description description) {

  }
}
