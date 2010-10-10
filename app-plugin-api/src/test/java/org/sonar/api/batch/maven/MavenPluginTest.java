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
package org.sonar.api.batch.maven;

import org.apache.maven.model.Plugin;
import org.apache.maven.project.MavenProject;
import org.junit.Before;
import org.junit.Test;
import org.sonar.api.test.MavenTestUtils;

import java.util.Collection;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

public class MavenPluginTest {

  private MavenPlugin fakePlugin;

  @Before
  public void initPlugin() {
    fakePlugin = new MavenPlugin("foo", "bar", "1.0");
  }

  @Test
  public void removeParameters() {
    fakePlugin
        .setParameter("foo", "bar")
        .setParameter("hello", "world")
        .removeParameters();

    assertThat(fakePlugin.getParameter("foo"), nullValue());
    assertThat(fakePlugin.getParameter("hello"), nullValue());
    assertThat(fakePlugin.hasConfiguration(), is(false));
  }

  @Test
  public void shouldWriteAndReadSimpleConfiguration() {
    fakePlugin.setParameter("abc", "test");
    assertThat(fakePlugin.getParameter("abc"), is("test"));
  }

  @Test
  public void shouldWriteAndReadComplexConfiguration() {
    fakePlugin.setParameter("abc/def/ghi", "test");
    assertThat(fakePlugin.getParameter("abc/def/ghi"), is("test"));
  }

  @Test
  public void shouldReturnNullWhenChildNotFound() {
    assertThat(fakePlugin.getParameter("abc/def/ghi"), is(nullValue()));
  }

  @Test(expected = IllegalArgumentException.class)
  public void getChildValueShouldThrowExceptionWhenKeyIsNull() {
    fakePlugin.getParameter(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void setChildValueShouldThrowExceptionWhenKeyIsNull() {
    fakePlugin.setParameter(null, null);
  }

  @Test
  public void shouldRemoveParameter() {
    fakePlugin.setParameter("abc", "1");
    assertThat(fakePlugin.getParameter("abc"), is(not(nullValue())));

    fakePlugin.removeParameter("abc");
    assertThat(fakePlugin.getParameter("abc"), is(nullValue()));
  }

  @Test
  public void shouldRemoveNestedParameter() {
    fakePlugin.setParameter("abc/def", "1");
    assertThat(fakePlugin.getParameter("abc/def"), is(not(nullValue())));

    fakePlugin.removeParameter("abc/def");

    assertThat(fakePlugin.getParameter("abc/def"), is(nullValue()));
  }

  @Test
  public void shouldRemoveNestedParameterButLeaveTheParent() {
    fakePlugin.setParameter("abc/x", "1");
    fakePlugin.setParameter("abc/y", "2");

    fakePlugin.removeParameter("abc/x");

    assertThat(fakePlugin.getParameter("abc/y"), is(not(nullValue())));
  }

  @Test
  public void shouldRemoveUnfoundChildWithoutError() {
    fakePlugin.removeParameter("abc/def");
  }


  @Test
  public void shouldSetParameter() {
    fakePlugin.addParameter("exclude", "abc");
    assertThat(fakePlugin.toString(), fakePlugin.getParameter("exclude"), is("abc"));
    assertThat(fakePlugin.toString(), fakePlugin.getParameters("exclude"), is(new String[]{"abc"}));
  }

  @Test
  public void shouldOverrideNestedParameter() {
    fakePlugin.setParameter("excludes/exclude", "abc");
    fakePlugin.setParameter("excludes/exclude", "overridden");
    assertThat(fakePlugin.toString(), fakePlugin.getParameter("excludes/exclude"), is("overridden"));
    assertThat(fakePlugin.toString(), fakePlugin.getParameters("excludes/exclude"), is(new String[]{"overridden"}));
  }

  @Test
  public void shouldOverriddeParameter() {
    fakePlugin.setParameter("exclude", "abc");
    fakePlugin.setParameter("exclude", "overridden");
    assertThat(fakePlugin.toString(), fakePlugin.getParameter("exclude"), is("overridden"));
    assertThat(fakePlugin.toString(), fakePlugin.getParameters("exclude"), is(new String[]{"overridden"}));
  }

  @Test
  public void shouldAddNestedParameter() {
    fakePlugin.addParameter("excludes/exclude", "abc");
    assertThat(fakePlugin.toString(), fakePlugin.getParameter("excludes/exclude"), is("abc"));
    assertThat(fakePlugin.toString(), fakePlugin.getParameters("excludes/exclude"), is(new String[]{"abc"}));
  }

  @Test
  public void shouldAddManyValuesToTheSameParameter() {
    fakePlugin.addParameter("excludes/exclude", "abc");
    fakePlugin.addParameter("excludes/exclude", "def");
    assertThat(fakePlugin.toString(), fakePlugin.getParameters("excludes/exclude"), is(new String[]{"abc", "def"}));
  }

  @Test
  public void defaultParameterIndexIsZero() {
    fakePlugin.addParameter("items/item/entry", "value1");
    fakePlugin.addParameter("items/item/entry", "value2");

    assertThat(fakePlugin.toString(), fakePlugin.getParameters("items/item/entry"), is(new String[]{"value1", "value2"}));
    assertThat(fakePlugin.toString(), fakePlugin.getParameters("items/item[0]/entry"), is(new String[]{"value1", "value2"}));
  }
  

  @Test
  public void addIndexedParameters() {
    fakePlugin.addParameter("items/item[0]/entry", "value1");
    fakePlugin.addParameter("items/item[1]/entry", "value2");

    assertThat(fakePlugin.getParameter("items/item[0]/entry"), is("value1"));
    assertThat(fakePlugin.getParameters("items/item[0]/entry"), is(new String[]{"value1"}));

    assertThat(fakePlugin.getParameter("items/item[1]/entry"), is("value2"));
    assertThat(fakePlugin.getParameters("items/item[1]/entry"), is(new String[]{"value2"}));

    //ensure that indexes aren't serialized to real configuration
    assertThat(fakePlugin.getPlugin().getConfiguration().toString(), not(containsString("item[0]")));
    assertThat(fakePlugin.getPlugin().getConfiguration().toString(), not(containsString("item[1]")));
  }

  @Test
  public void removeIndexedParameter(){
    fakePlugin.addParameter("items/item[0]/entry", "value1");
    fakePlugin.addParameter("items/item[1]/entry", "value2");

    fakePlugin.removeParameter("items/item[1]");
    fakePlugin.removeParameter("items/notExists");

    assertThat(fakePlugin.getParameter("items/item[0]/entry"), notNullValue());
    assertThat(fakePlugin.getParameter("items/item[1]/entry"), nullValue());
    assertThat(fakePlugin.getParameter("items/notExists"), nullValue());
  }

  @Test
  public void registerNewPlugin() {
    MavenProject pom = MavenTestUtils.loadPom(getClass(), "registerNewPlugin.xml");
    MavenPlugin mavenPlugin = MavenPlugin.registerPlugin(pom, "mygroup", "my.artifact", "1.0", true);

    assertThat(mavenPlugin, not(nullValue()));
    Plugin plugin = MavenUtils.getPlugin((Collection<Plugin>) pom.getBuildPlugins(), "mygroup", "my.artifact");
    assertThat(plugin, not(nullValue()));
    assertThat(plugin.getVersion(), is("1.0"));
  }

  @Test
  public void overridePluginManagementSection() {
    MavenProject pom = MavenTestUtils.loadPom(getClass(), "overridePluginManagementSection.xml");
    MavenPlugin mavenPlugin = MavenPlugin.registerPlugin(pom, "mygroup", "my.artifact", "1.0", true);
    assertThat(mavenPlugin, not(nullValue()));

    Plugin plugin = MavenUtils.getPlugin((Collection<Plugin>) pom.getBuildPlugins(), "mygroup", "my.artifact");
    assertThat(plugin, not(nullValue()));
    assertThat(plugin.getVersion(), is("1.0"));

    Plugin pluginManagement = MavenUtils.getPlugin((Collection<Plugin>) pom.getPluginManagement().getPlugins(), "mygroup", "my.artifact");
    assertThat(pluginManagement, nullValue());
  }

  @Test
  public void doNotOverrideVersionFromPluginManagementSection() {
    MavenProject pom = MavenTestUtils.loadPom(getClass(), "overridePluginManagementSection.xml");
    MavenPlugin mavenPlugin = MavenPlugin.registerPlugin(pom, "mygroup", "my.artifact", "1.0", false);
    assertThat(mavenPlugin, not(nullValue()));

    Plugin plugin = MavenUtils.getPlugin((Collection<Plugin>) pom.getBuildPlugins(), "mygroup", "my.artifact");
    assertThat(plugin, not(nullValue()));
    assertThat(plugin.getVersion(), is("0.9"));

    Plugin pluginManagement = MavenUtils.getPlugin((Collection<Plugin>) pom.getPluginManagement().getPlugins(), "mygroup", "my.artifact");
    assertThat(pluginManagement, nullValue());
  }

  @Test
  public void keepPluginManagementDependencies() {
    MavenProject pom = MavenTestUtils.loadPom(getClass(), "keepPluginManagementDependencies.xml");
    MavenPlugin mavenPlugin = MavenPlugin.registerPlugin(pom, "mygroup", "my.artifact", "1.0", false);
    assertThat(mavenPlugin, not(nullValue()));

    Plugin plugin = MavenUtils.getPlugin((Collection<Plugin>) pom.getBuildPlugins(), "mygroup", "my.artifact");
    assertThat(plugin, not(nullValue()));
    assertThat(plugin.getVersion(), is("0.9"));
    assertThat(plugin.getDependencies().size(), is(1));
  }

  @Test
  public void keepPluginDependencies() {
    MavenProject pom = MavenTestUtils.loadPom(getClass(), "keepPluginDependencies.xml");
    MavenPlugin mavenPlugin = MavenPlugin.registerPlugin(pom, "mygroup", "my.artifact", "1.0", false);
    assertThat(mavenPlugin, not(nullValue()));

    Plugin plugin = MavenUtils.getPlugin((Collection<Plugin>) pom.getBuildPlugins(), "mygroup", "my.artifact");
    assertThat(plugin, not(nullValue()));
    assertThat(plugin.getVersion(), is("0.9"));
    assertThat(plugin.getDependencies().size(), is(1));
  }

  @Test
  public void mergeSettings() {
    MavenProject pom = MavenTestUtils.loadPom(getClass(), "mergeSettings.xml");
    MavenPlugin.registerPlugin(pom, "mygroup", "my.artifact", "1.0", false);

    MavenPlugin plugin = MavenPlugin.getPlugin(pom, "mygroup", "my.artifact");
    assertThat(plugin, not(nullValue()));
    assertThat(plugin.getPlugin().getVersion(), is("0.9"));
    assertThat(plugin.getParameter("foo"), is("bar"));
  }

  @Test
  public void overrideVersionFromPluginManagement() {
    MavenProject pom = MavenTestUtils.loadPom(getClass(), "overrideVersionFromPluginManagement.xml");
    MavenPlugin.registerPlugin(pom, "mygroup", "my.artifact", "1.0", true);

    MavenPlugin plugin = MavenPlugin.getPlugin(pom, "mygroup", "my.artifact");
    assertThat(plugin, not(nullValue()));
    assertThat(plugin.getPlugin().getVersion(), is("1.0"));
    assertThat(plugin.getParameter("foo"), is("bar"));
  }

  @Test
  public void overrideVersion() {
    MavenProject pom = MavenTestUtils.loadPom(getClass(), "overrideVersion.xml");
    MavenPlugin.registerPlugin(pom, "mygroup", "my.artifact", "1.0", true);

    MavenPlugin plugin = MavenPlugin.getPlugin(pom, "mygroup", "my.artifact");
    assertThat(plugin, not(nullValue()));
    assertThat(plugin.getPlugin().getVersion(), is("1.0"));
    assertThat(plugin.getParameter("foo"), is("bar"));
  }

  @Test
  public void getConfigurationFromReport() {
    MavenProject pom = MavenTestUtils.loadPom(getClass(), "getConfigurationFromReport.xml");
    MavenPlugin.registerPlugin(pom, "mygroup", "my.artifact", "1.0", true);

    assertThat(pom.getBuildPlugins().size(), is(1));
    assertThat(pom.getReportPlugins().size(), is(0));

    MavenPlugin plugin = MavenPlugin.getPlugin(pom, "mygroup", "my.artifact");
    assertThat(plugin, not(nullValue()));
    assertThat(plugin.getPlugin().getVersion(), is("1.0"));
    assertThat(plugin.getParameter("foo"), is("bar"));
  }
}
