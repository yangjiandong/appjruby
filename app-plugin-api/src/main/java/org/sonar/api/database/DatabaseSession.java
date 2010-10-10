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
package org.sonar.api.database;

import org.sonar.api.BatchComponent;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * This component should not accessible from plugin API
 *
 * @since 1.10
 */
public abstract class DatabaseSession implements BatchComponent {


  // IMPORTANT : this value must be the same than the property
  // hibernate.jdbc.batch_size from /META-INF/persistence.xml (module sonar-database)
  public static final int BATCH_SIZE = 30;


  public abstract EntityManager getEntityManager();

  public abstract void start();

  public abstract void stop();

  public abstract void commit();

  public abstract void rollback();

  public abstract <T> T save(T entity);

  public abstract Object saveWithoutFlush(Object entity);

  public abstract boolean contains(Object entity);

  public abstract void save(Object... entities);

  public abstract Object merge(Object entity);

  public abstract void remove(Object entity);

  public abstract void removeWithoutFlush(Object entity);

  public abstract <T> T reattach(Class<T> entityClass, Object primaryKey);

  public abstract Query createQuery(String hql);

  public abstract <T> T getSingleResult(Query query, T defaultValue);

  public abstract <T> T getEntity(Class<T> entityClass, Object id);

  public abstract <T> T getSingleResult(Class<T> entityClass, Object... criterias);

  public abstract <T> List<T> getResults(Class<T> entityClass, Object... criterias);

  public abstract <T> List<T> getResults(Class<T> entityClass);
}
