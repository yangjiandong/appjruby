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
package org.sonar.api.design;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.persistence.*;

@Entity
@Table(name = "dependencies")
public class DependencyDto {

  @Id
  @Column(name = "id")
  @GeneratedValue
  private Long id;

  @Column(name = "from_snapshot_id", updatable = true, nullable = false)
  private Integer fromSnapshotId;

  @Column(name = "from_resource_id", updatable = true, nullable = false)
  private Integer fromResourceId;

  @Column(name = "from_scope", updatable = true, nullable = true)
  private String fromScope;

  @Column(name = "to_snapshot_id", updatable = true, nullable = false)
  private Integer toSnapshotId;

  @Column(name = "to_resource_id", updatable = true, nullable = false)
  private Integer toResourceId;

  @Column(name = "to_scope", updatable = true, nullable = true)
  private String toScope;

  @Column(name = "dep_weight", updatable = true, nullable = true)
  private Integer weight;

  @Column(name = "dep_usage", updatable = true, nullable = true, length = 30)
  private String usage;

  @Column(name = "project_snapshot_id", updatable = true, nullable = false)
  private Integer projectSnapshotId;

  @Column(name = "parent_dependency_id", updatable = true, nullable = true)
  private Long parentDependencyId;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Integer getFromSnapshotId() {
    return fromSnapshotId;
  }

  public DependencyDto setFromSnapshotId(Integer fromSnapshotId) {
    this.fromSnapshotId = fromSnapshotId;
    return this;
  }

  public Integer getFromResourceId() {
    return fromResourceId;
  }

  public DependencyDto setFromResourceId(Integer fromResourceId) {
    this.fromResourceId = fromResourceId;
    return this;
  }

  public Integer getToSnapshotId() {
    return toSnapshotId;
  }

  public DependencyDto setToSnapshotId(Integer toSnapshotId) {
    this.toSnapshotId = toSnapshotId;
    return this;
  }

  public Integer getToResourceId() {
    return toResourceId;
  }

  public DependencyDto setToResourceId(Integer toResourceId) {
    this.toResourceId = toResourceId;
    return this;
  }

  public Integer getWeight() {
    return weight;
  }

  public DependencyDto setWeight(Integer weight) {
    if (weight < 0) {
      throw new IllegalArgumentException("Dependency weight can not be negative");
    }
    this.weight = weight;
    return this;
  }

  public String getFromScope() {
    return fromScope;
  }

  public DependencyDto setFromScope(String fromScope) {
    this.fromScope = fromScope;
    return this;
  }

  public String getToScope() {
    return toScope;
  }

  public DependencyDto setToScope(String toScope) {
    this.toScope = toScope;
    return this;
  }

  public String getUsage() {
    return usage;
  }

  public DependencyDto setUsage(String usage) {
    this.usage = usage;
    return this;
  }

  public Integer getProjectSnapshotId() {
    return projectSnapshotId;
  }

  public DependencyDto setProjectSnapshotId(Integer projectSnapshotId) {
    this.projectSnapshotId = projectSnapshotId;
    return this;
  }

  public Long getParentDependencyId() {
    return parentDependencyId;
  }

  public DependencyDto setParentDependencyId(Long parentDependencyId) {
    this.parentDependencyId = parentDependencyId;
    return this;
  }

  @Override
  public boolean equals(Object obj) {
    if (!(obj instanceof DependencyDto)) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    DependencyDto other = (DependencyDto) obj;
    return new EqualsBuilder()
        .append(fromSnapshotId, other.fromSnapshotId)
        .append(toSnapshotId, other.toSnapshotId)
        .isEquals();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
        .append(fromSnapshotId)
        .append(toSnapshotId)
        .toHashCode();
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("id", getId())
        .append("fromSnapshotId", fromSnapshotId)
        .append("fromResourceId", fromResourceId)
        .append("fromScope", fromScope)
        .append("toSnapshotId", toSnapshotId)
        .append("toResourceId", toResourceId)
        .append("toScope", toScope)
        .append("weight", weight)
        .append("usage", usage)
        .append("projectSnapshotId", projectSnapshotId)
        .append("parentDependencyId", parentDependencyId)
        .toString();
  }
}
