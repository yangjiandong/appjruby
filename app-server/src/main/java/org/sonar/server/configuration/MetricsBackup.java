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
package org.sonar.server.configuration;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.sonar.api.database.DatabaseSession;
import org.sonar.jpa.dao.MeasuresDao;
import org.sonar.api.measures.Metric;

import java.util.Collection;

public class MetricsBackup implements Backupable {

  private MeasuresDao measuresDao;

  public MetricsBackup(DatabaseSession session) {
    measuresDao = new MeasuresDao(session);
  }

  protected MetricsBackup() {
  }

  public void exportXml(SonarConfig sonarConfig) {
    Collection<Metric> metrics = measuresDao.getUserDefinedMetrics();
    exportXml(sonarConfig, metrics);
  }

  protected void exportXml(SonarConfig sonarConfig, Collection<Metric> metrics) {
    sonarConfig.setMetrics(metrics);
  }

  public void importXml(SonarConfig sonarConfig) {
    disabledUserDefinedMetrics();
    registerMetrics(sonarConfig);
  }

  protected void disabledUserDefinedMetrics() {
    Collection<Metric> dbMetrics = measuresDao.getUserDefinedMetrics();
    measuresDao.disabledMetrics(dbMetrics);
  }

  protected void registerMetrics(SonarConfig sonarConfig) {
    Collection<Metric> newMetrics = sonarConfig.getMetrics();
    measuresDao.registerMetrics(newMetrics);
  }

  public void configure(XStream xStream) {
    xStream.alias("metric", Metric.class);
    xStream.omitField(Metric.class, "id");
    xStream.omitField(Metric.class, "userManaged");
    xStream.omitField(Metric.class, "comparable");
    xStream.omitField(Metric.class, "enabled");
    final Converter builtIn = xStream.getConverterLookup().lookupConverterForType(Metric.class);
    xStream.registerConverter(new Converter() {
      public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        builtIn.marshal(source, writer, context);
      }

      public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Metric unmarshalled = (Metric) builtIn.unmarshal(reader, context);
        unmarshalled.setUserManaged(true);
        unmarshalled.setEnabled(true);
        return unmarshalled;
      }

      public boolean canConvert(Class type) {
        return Metric.class.equals(type);
      }
    });
  }

}
