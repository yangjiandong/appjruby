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
package org.sonar.batch;

import org.apache.commons.io.IOUtils;
import org.apache.maven.project.MavenProject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonar.api.batch.BatchExtensionDictionnary;
import org.sonar.api.batch.SensorContext;
import org.sonar.api.batch.maven.MavenPlugin;
import org.sonar.api.batch.maven.MavenPluginHandler;
import org.sonar.api.resources.Project;
import org.sonar.api.utils.SonarException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MavenPluginsConfigurator implements CoreJob {

  private BatchExtensionDictionnary dictionnary = null;

  public MavenPluginsConfigurator(BatchExtensionDictionnary dictionnary) {
    this.dictionnary = dictionnary;
  }

  public void execute(Project project, SensorContext context) {
    Logger logger = LoggerFactory.getLogger(getClass());
    logger.info("Configure maven plugins...");

    for (MavenPluginHandler handler : dictionnary.selectMavenPluginHandlers(project)) {
      logger.debug("Configure {}...", handler);
      configureHandler(project, handler);
    }
    savePom(project);
  }

  protected void configureHandler(Project project, MavenPluginHandler handler) {
    MavenPlugin plugin = MavenPlugin.registerPlugin(project.getPom(), handler.getGroupId(), handler.getArtifactId(), handler.getVersion(), handler.isFixedVersion());
    handler.configure(project, plugin);
  }

  protected void savePom(Project project) {
    MavenProject pom = project.getPom();
    File targetPom = new File(project.getFileSystem().getSonarWorkingDirectory(), "sonar-pom.xml");
    FileWriter fileWriter = null;
    try {
      fileWriter = new FileWriter(targetPom, false);
      pom.writeModel(fileWriter);

    } catch (IOException e) {
      throw new SonarException("Can not save pom to " + targetPom, e);
    } finally {
      IOUtils.closeQuietly(fileWriter);
    }
  }
}
