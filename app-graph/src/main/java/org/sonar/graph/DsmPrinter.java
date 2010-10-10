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
package org.sonar.graph;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public final class DsmPrinter {

  private final Writer writer;
  private final Dsm dsm;
  private static final String CELL_SEPARATOR = "| ";
  private static final String FEEDBACK_EDGE_FLAG = "*";
  private final boolean displayColumnHeaders;

  private DsmPrinter(Writer writer, Dsm dsm, boolean displayColumnHeaders) {
    this.writer = writer;
    this.dsm = dsm;
    this.displayColumnHeaders = displayColumnHeaders;
  }

  private void print() {
    try {
      if (displayColumnHeaders) {
        printColumnHeaders();
      }
      for (int y = 0; y < dsm.getDimension(); y++) {
        printRow(y);
      }
      writer.flush();
      
    } catch (IOException e) {
      throw new RuntimeException("Unable to print the desired DSM.", e); // NOSONAR
    }
  }

  public static String print(Dsm dsm, boolean displayColumnHeaders) {
    StringWriter writer = new StringWriter();
    print(writer, dsm, displayColumnHeaders);
    return writer.toString();
  }

  public static String print(Dsm dsm) {
    return print(dsm, true);
  }

  public static void print(Writer writer, Dsm dsm, boolean displayColumnHeaders) {
    DsmPrinter printer = new DsmPrinter(writer, dsm, displayColumnHeaders);
    printer.print();
  }

  private void printRow(int y) throws IOException {
    printRowHeader(y);
    for (int x = 0; x < dsm.getDimension(); x++) {
      printCell(y, x);
    }
    writer.append((char) Character.LINE_SEPARATOR);
  }

  private void printCell(int y, int x) throws IOException {
    if (dsm.getCell(x, y).getWeight() == 0) {
      writer.append(" ");
    } else {
      writer.append("").append(String.valueOf(dsm.getCell(x, y).getWeight()));
    }
    if (dsm.getCell(x, y).isFeedbackEdge()) {
      writer.append(FEEDBACK_EDGE_FLAG);
    } else {
      writer.append(' ');
    }
    writer.append(CELL_SEPARATOR);
  }

  private void printRowHeader(int y) throws IOException {
    writer.append(String.valueOf(dsm.getVertex(y))).append(" " + CELL_SEPARATOR);
  }

  private void printColumnHeaders() throws IOException {
    writer.append("  " + CELL_SEPARATOR);
    for (int i = 0; i < dsm.getDimension(); i++) {
      printRowHeader(i);
    }
    writer.append((char) Character.LINE_SEPARATOR);
  }

}
