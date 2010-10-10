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
package org.sonar.server.startup;

import org.apache.commons.io.FileUtils;
import org.sonar.server.platform.DefaultServerFileSystem;

import java.io.File;
import java.io.IOException;

public class JdbcDriverDeployer {

  private DefaultServerFileSystem fileSystem;

  public JdbcDriverDeployer(DefaultServerFileSystem fileSystem) {
    this.fileSystem = fileSystem;
  }

  public void start() {
    File driver = fileSystem.getJdbcDriver();
    File deployedDriver = fileSystem.getDeployedJdbcDriver();
    if (deployedDriver == null || !deployedDriver.exists() || deployedDriver.length() != driver.length()) {
      try {
        FileUtils.copyFile(driver, deployedDriver);

      } catch (IOException e) {
        throw new RuntimeException("Can not copy the JDBC driver from " + driver + " to " + deployedDriver, e);
      }
    }
  }
}
