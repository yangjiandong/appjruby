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

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DsmManualSorterTest {

  @Test
  public void testSort() {
    DirectedGraph<String, StringEdge> graph = DirectedGraph.createStringDirectedGraph();
    graph.addEdge("A", "B", 2);
    graph.addEdge("A", "C", 3);
    graph.addEdge("C", "B", 1);
    Dsm<String> dsm = new Dsm<String>(graph);
    DsmManualSorter.sort(dsm, "B", "A", "C");

    StringPrintWriter expectedDsm = new StringPrintWriter();
    expectedDsm.println("  | B | A | C |");
    expectedDsm.println("B |   | 2 | 1 |");
    expectedDsm.println("A |   |   |   |");
    expectedDsm.println("C |   | 3 |   |");

    assertEquals(expectedDsm.toString(), DsmPrinter.print(dsm));
  }

}
