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
package org.sonar.api.resources;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.sonar.api.utils.WildcardPattern;

/**
 * This class is an implementation of a resource of type FILE
 * 
 * @since 1.10
 */
public class File extends Resource<Directory> {

  private String directoryKey;
  private String filename;
  private Language language;
  private Directory parent;
  private String qualifier = Resource.QUALIFIER_FILE;

  /**
   * File in project. Key is the path relative to project source directories. It is not the absolute path and it does not include the path
   * to source directories. Example : <code>new File("org/sonar/foo.sql")</code>. The absolute path may be
   * c:/myproject/src/main/sql/org/sonar/foo.sql. Project root is c:/myproject and source dir is src/main/sql.
   */
  public File(String key) {
    if (key == null) {
      throw new IllegalArgumentException("File key is null");
    }
    String realKey = parseKey(key);
    if (realKey.indexOf(Directory.SEPARATOR) >= 0) {
      this.directoryKey = Directory.parseKey(StringUtils.substringBeforeLast(key, Directory.SEPARATOR));
      this.filename = StringUtils.substringAfterLast(realKey, Directory.SEPARATOR);
      realKey = new StringBuilder().append(this.directoryKey).append(Directory.SEPARATOR).append(filename).toString();

    } else {
      this.filename = key;
    }
    setKey(realKey);
  }

  /**
   * Creates a file from its containing directory and name
   */
  public File(String directory, String filename) {
    this.filename = StringUtils.trim(filename);
    if (StringUtils.isBlank(directory)) {
      setKey(filename);

    } else {
      this.directoryKey = Directory.parseKey(directory);
      setKey(new StringBuilder().append(directoryKey).append(Directory.SEPARATOR).append(this.filename).toString());
    }
  }

  /**
   * Creates a File from its language and its key
   */
  public File(Language language, String key) {
    this(key);
    this.language = language;
  }

  /**
   * Creates a File from language, directory and filename
   */
  public File(Language language, String directory, String filename) {
    this(directory, filename);
    this.language = language;
  }

  /**
   * {@inheritDoc}
   * 
   * @see Resource#getParent()
   */
  public Directory getParent() {
    if (parent == null) {
      parent = new Directory(directoryKey);
    }
    return parent;
  }

  private static String parseKey(String key) {
    if (StringUtils.isBlank(key)) {
      return null;
    }

    key = key.replace('\\', '/');
    key = StringUtils.trim(key);
    return key;
  }

  /**
   * {@inheritDoc}
   * 
   * @see Resource#matchFilePattern(String)
   */
  public boolean matchFilePattern(String antPattern) {
    WildcardPattern matcher = WildcardPattern.create(antPattern, "/");
    return matcher.match(getKey());
  }

  /**
   * Creates a File from an io.file and a list of sources directories
   */
  public static File fromIOFile(java.io.File file, List<java.io.File> sourceDirs) {
    String relativePath = DefaultProjectFileSystem.getRelativePath(file, sourceDirs);
    if (relativePath != null) {
      return new File(relativePath);
    }
    return null;
  }

  /**
   * Creates a File from its name and a project
   */
  public static File fromIOFile(java.io.File file, Project project) {
    return fromIOFile(file, project.getFileSystem().getSourceDirs());
  }

  /**
   * {@inheritDoc}
   * 
   * @see Resource#getName()
   */
  public String getName() {
    return filename;
  }

  /**
   * {@inheritDoc}
   * 
   * @see Resource#getLongName()
   */
  public String getLongName() {
    return getKey();
  }

  /**
   * {@inheritDoc}
   * 
   * @see Resource#getDescription()
   */
  public String getDescription() {
    return null;
  }

  /**
   * {@inheritDoc}
   * 
   * @see Resource#getLanguage()
   */
  public Language getLanguage() {
    return language;
  }

  /**
   * Sets the language of the file
   */
  public void setLanguage(Language language) {
    this.language = language;
  }

  /**
   * @return SCOPE_ENTITY
   */
  public String getScope() {
    return Resource.SCOPE_ENTITY;
  }

  /**
   * Returns the qualifier associated to this File. Should be QUALIFIER_FILE or
   * 
   * @return QUALIFIER_UNIT_TEST_CLASS
   */
  public String getQualifier() {
    return qualifier;
  }

  public void setQualifier(String qualifier) {
    this.qualifier = qualifier;
  }

  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("key", getKey())
        .append("dir", directoryKey)
        .append("filename", filename)
        .append("language", language)
        .toString();
  }
}