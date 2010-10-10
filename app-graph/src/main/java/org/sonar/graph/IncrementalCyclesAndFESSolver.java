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

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class IncrementalCyclesAndFESSolver<V> {

  private Set<Cycle> cycles = new HashSet<Cycle>();
  private Set<Edge> edgesToExclude = new HashSet<Edge>();
  private long searchCyclesCalls = 0;
  private static final int DEFAULT_MAX_SEARCH_DEPTH_AT_FIRST = 3;
  private static final int DEFAULT_MAX_CYCLES_TO_FOUND_BY_ITERATION = 100;
  private MinimumFeedbackEdgeSetSolver solver;
  private int iterations = 0;

  public IncrementalCyclesAndFESSolver(DirectedGraphAccessor<V, ? extends Edge> graph, Collection<V> vertices) {
    this(graph, vertices, DEFAULT_MAX_SEARCH_DEPTH_AT_FIRST, DEFAULT_MAX_CYCLES_TO_FOUND_BY_ITERATION);
  }

  public IncrementalCyclesAndFESSolver(DirectedGraphAccessor<V, ? extends Edge> graph, Collection<V> vertices, int maxSearchDepthAtFirst,
      int maxCyclesToFoundByIteration) {

    iterations++;
    CycleDetector<V> cycleDetector = new CycleDetector<V>(graph, vertices);
    cycleDetector.detectCyclesWithMaxSearchDepth(maxSearchDepthAtFirst);
    searchCyclesCalls += cycleDetector.getSearchCyclesCalls();
    cycles.addAll(cycleDetector.getCycles());
    solver = new MinimumFeedbackEdgeSetSolver(cycles);
    edgesToExclude = solver.getEdges();

    do {
      iterations++;
      cycleDetector = new CycleDetector<V>(graph, vertices, edgesToExclude);
      cycleDetector.detectCyclesWithUpperLimit(maxCyclesToFoundByIteration);
      searchCyclesCalls += cycleDetector.getSearchCyclesCalls();
      cycles.addAll(cycleDetector.getCycles());
      solver = new MinimumFeedbackEdgeSetSolver(cycles);
      edgesToExclude = solver.getEdges();
    } while (cycleDetector.getCycles().size() != 0);
  }

  public int getWeightOfFeedbackEdgeSet() {
    return solver.getWeightOfFeedbackEdgeSet();
  }

  public int getNumberOfLoops() {
    return solver.getNumberOfLoops();
  }

  public Set<Edge> getFeedbackEdgeSet() {
    return solver.getEdges();
  }

  public Set<Cycle> getCycles() {
    return cycles;
  }

  public boolean isAcyclicGraph() {
    return cycles.isEmpty();
  }

  public long getSearchCyclesCalls() {
    return searchCyclesCalls;
  }

  public int getIterations() {
    return iterations;
  }
}
