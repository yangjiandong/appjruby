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
package org.sonar.api.database.model;

import org.junit.Test;

import java.util.Date;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class SnapshotTest {


  @Test
  public void testGetDate() {
    Snapshot snapshot = new Snapshot();
    assertNull(snapshot.getCreatedAt());

    Date now = new Date();
    snapshot.setCreatedAt(now);
    assertEquals(now, snapshot.getCreatedAt());
  }

  @Test
  public void testGetVersion() {
    Snapshot snapshot = new Snapshot();
    assertNull(snapshot.getVersion());

    snapshot.setVersion("1.0");
    assertEquals("1.0", snapshot.getVersion());
  }

  @Test
  public void testGetStatus() {
    Snapshot snapshot = new Snapshot();
    assertNotNull(snapshot.getStatus());
    assertEquals(Snapshot.STATUS_UNPROCESSED, snapshot.getStatus());
  }

  @Test
  public void testGetLast() {
    Snapshot snapshot = new Snapshot();
    assertNotNull(snapshot.getLast());
    assertEquals(Boolean.FALSE, snapshot.getLast());
  }

  @Test
  public void testRootProjectIdOfProjects() {
    ResourceModel resource = new ResourceModel();
    resource.setQualifier("TRK");
    resource.setId(3);

    Snapshot snapshot = new Snapshot(resource, null);
    assertThat(snapshot.getRootProjectId(), is(3));
  }

  @Test
  public void testRootProjectIdOfModules() {
    ResourceModel parentResource = new ResourceModel();
    parentResource.setId(3);
    parentResource.setQualifier("TRK");
    Snapshot parentSnapshot = new Snapshot(parentResource, null);

    ResourceModel resource = new ResourceModel();
    resource.setId(4);
    resource.setQualifier("BRC");
    Snapshot snapshot = new Snapshot(resource, parentSnapshot);
    assertThat(snapshot.getRootProjectId(), is(3));
  }

  @Test
  public void testRootProjectIdOfViews() {
    ResourceModel view = new ResourceModel();
    view.setId(3);
    view.setQualifier("VW");
    Snapshot viewSnapshot = new Snapshot(view, null);

    ResourceModel subview = new ResourceModel();
    subview.setId(4);
    subview.setQualifier("SVW");
    Snapshot subviewSnapshot = new Snapshot(subview, viewSnapshot);
    assertThat(subviewSnapshot.getRootProjectId(), is(4));

    ResourceModel project = new ResourceModel();
    project.setId(5);
    project.setQualifier("TRK");
    project.setCopyResourceId(66);
    Snapshot projectSnapshot = new Snapshot(project, subviewSnapshot);
    assertThat(projectSnapshot.getRootProjectId(), is(66));
  }
}
