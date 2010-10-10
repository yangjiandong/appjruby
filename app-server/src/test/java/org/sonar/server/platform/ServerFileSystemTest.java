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
package org.sonar.server.platform;

import org.junit.Test;
import org.sonar.jpa.session.DatabaseConnector;
import org.sonar.jpa.dialect.Dialect;
import org.sonar.jpa.dialect.MySql;
import org.sonar.test.TestUtils;

import java.io.File;
import java.util.List;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServerFileSystemTest {

  private static final String PATH = "/org/sonar/server/platform/ServerFileSystemTest/";

  @Test
  public void testGetJdbcDriver() {
    DatabaseConnector databaseConnector = mock(DatabaseConnector.class);
    when(databaseConnector.getDialect()).thenReturn(new MySql());
    File driver = new DefaultServerFileSystem(databaseConnector, TestUtils.getResource(PATH + "testGetJdbcDriver"), null).getJdbcDriver();
    assertNotNull(driver);
  }


  @Test(expected = ServerStartException.class)
  public void failIfJdbcDriverNotFound() {
    DatabaseConnector databaseConnector = mock(DatabaseConnector.class);

    Dialect fakeDialect = mock(Dialect.class);
    when(fakeDialect.getId()).thenReturn("none");
    when(databaseConnector.getDialect()).thenReturn(fakeDialect);

    new DefaultServerFileSystem(databaseConnector, TestUtils.getResource(PATH + "testGetJdbcDriver"), null).getJdbcDriver();
  }

  @Test
  public void shouldFindPlugins() {
    List<File> plugins = new DefaultServerFileSystem(null, TestUtils.getResource(PATH + "shouldFindPlugins"), null).getUserPlugins();
    assertEquals(2, plugins.size());
  }

  @Test
  public void shouldNotFailIfNoPlugins() {
    List<File> plugins = new DefaultServerFileSystem(null, TestUtils.getResource(PATH + "shouldNotFailIfNoPlugins"), null).getUserPlugins();
    assertEquals(0, plugins.size());
  }

  @Test
  public void shouldFindCheckstyleExtensions() {
    DefaultServerFileSystem fs = new DefaultServerFileSystem(null, TestUtils.getResource(PATH + "shouldFindCheckstyleExtensions"), null);

    List<File> xmls = fs.getPluginExtensionXml("checkstyle");
    assertEquals(1, xmls.size());

    List<File> jars = fs.getExtensions("checkstyle");
    assertEquals(3, jars.size());
  }


  @Test
  public void shouldNotFailIfNoCheckstyleExtensions() {
    DefaultServerFileSystem fs = new DefaultServerFileSystem(null, TestUtils.getResource(PATH + "shouldNotFailIfNoCheckstyleExtensions"), null);
    List<File> xmls = fs.getPluginExtensionXml("checkstyle");
    assertEquals(0, xmls.size());

    List<File> jars = fs.getExtensions("checkstyle");
    assertEquals(0, jars.size());
  }

}
