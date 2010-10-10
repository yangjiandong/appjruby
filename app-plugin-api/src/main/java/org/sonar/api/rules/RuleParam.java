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
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "rules_parameters")
public class RuleParam {

  @Id
  @Column(name = "id")
  @GeneratedValue
  private Integer id;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "rule_id")
  private Rule rule;

  @Column(name = "name", updatable = true, nullable = false, length = 128)
  private String key;

  @Column(name = "description", updatable = true, nullable = true, length = 4000)
  private String description;

  @Column(name = "param_type", updatable = true, nullable = true, length = 512)
  private String type = "s";

  @Column(name = "default_value", updatable = true, nullable = true, length = 4000)
  private String defaultValue;

  /**
   * @deprecated since 2.3 use the factory method Rule.setParameter()
   */
  @Deprecated
  public RuleParam() {
  }

  /**
   * @deprecated since 2.3 use the factory method setParameter()
   */
  @Deprecated
  public RuleParam(Rule rule, String key, String description, String type) {
    this.rule = rule;
    this.key = key;
    this.description = description;
    this.type = type;
  }

  public Rule getRule() {
    return rule;
  }

  RuleParam setRule(Rule rule) {
    this.rule = rule;
    return this;
  }

  public String getKey() {
    return key;
  }

  public RuleParam setKey(String key) {
    this.key = key;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public RuleParam setDescription(String s) {
    this.description = StringUtils.defaultString(s, "");
    return this;
  }

  public String getType() {
    return type;
  }

  public RuleParam setType(String type) {
    this.type = type;
    return this;
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public Boolean getDefaultValueAsBoolean() {
    if (defaultValue!=null) {
      return Boolean.parseBoolean(defaultValue);
    }
    return null;
  }

  public Integer getDefaultValueAsInteger() {
    if (defaultValue!=null) {
      return Integer.parseInt(defaultValue);
    }
    return null;
  }

  public RuleParam setDefaultValue(String s) {
    this.defaultValue = s;
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof RuleParam)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    RuleParam other = (RuleParam) obj;
    return new EqualsBuilder()
        //.append(rule, other.getRule())
        .append(key, other.getKey()).isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        //.append(rule)
        .append(key)
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", id)
        .append("key", key)
        .append("desc", description)
        .append("type", type)
        .toString();
  }
}