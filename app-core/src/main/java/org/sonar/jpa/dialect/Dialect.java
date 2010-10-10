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
package org.sonar.jpa.dialect;

/**
 * @since 1.12
 */
public interface Dialect {

  /**
   * @return the sonar dialect Id to be matched with the sonar.jdbc.dialect property when provided
   */
  String getId();

  /**
   * @return the hibernate dialect class to be used
   */
  Class<? extends org.hibernate.dialect.Dialect> getHibernateDialectClass();

  /**
   * @return the activerecord dialect to be used
   */
  String getActiveRecordDialectCode();

  /**
   * Used to autodetect a dialect for a given driver URL
   *
   * @param jdbcConnectionURL a jdbc driver url such as jdbc:mysql://localhost:3306/sonar
   * @return true if the dialect supports surch url
   */
  boolean matchesJdbcURL(String jdbcConnectionURL);

}
