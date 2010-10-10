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
package org.sonar.api.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A very simple profiler to log the time elapsed performing some tasks.
 * This implementation is not thread-safe.
 *
 * @since 2.0
 */
public class TimeProfiler {

  private Logger logger;
  private long start = 0;
  private String name;

  public TimeProfiler(Logger logger) {
    this.logger = logger;
  }

  public TimeProfiler(Class clazz) {
    this.logger = LoggerFactory.getLogger(clazz);
  }

  /**
   * Use the default Sonar logger
   */
  public TimeProfiler() {
    this.logger = Logs.INFO;
  }

  public TimeProfiler start(String name) {
    this.name = name;
    this.start = System.currentTimeMillis();
    logger.info(name + "...");
    return this;
  }

  public TimeProfiler setLogger(Logger logger) {
    this.logger = logger;
    return this;
  }

  public Logger getLogger() {
    return logger;
  }

  public TimeProfiler stop() {
    if (start > 0) {
      logger.info("{} done: {} ms", name, (System.currentTimeMillis() - start));
    }
    start = 0;
    return this;
  }
}