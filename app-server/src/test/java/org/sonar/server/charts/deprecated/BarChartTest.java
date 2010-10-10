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
package org.sonar.server.charts.deprecated;

import org.junit.Ignore;
import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BarChartTest extends BaseChartWebTest {

  @Test
  public void testBarChartDefaultDimensions() throws IOException {
    Map<String, String> params = getDefaultParams();
    BarChart chart = new BarChart(params);
    BufferedImage img = chart.getChartImage();
    saveChart(img, "bar-chart-default.png");
    assertChartSizeGreaterThan(img, 100);
  }

  @Test
  public void testBarChartRange() throws IOException {
    Map<String, String> params = getDefaultParams();
    params.put(BaseChartWeb.CHART_PARAM_RANGEMAX, "200");
    BarChart chart = new BarChart(params);
    BufferedImage img = chart.getChartImage();
    saveChart(img, "bar-chart-range.png");
    assertChartSizeGreaterThan(img, 100);
  }

  @Test
  public void testBarChartSpecificDimensions() throws IOException {
    Map<String, String> params = getDefaultParams();
    params.put(BaseChartWeb.CHART_PARAM_DIMENSIONS, "750x250");
    BarChart chart = new BarChart(params);
    BufferedImage img = chart.getChartImage();
    saveChart(img, "bar-chart-specific-dimensions.png");
    assertChartSizeGreaterThan(img, 100);
  }

  @Test
  public void testBarChartOneValue() throws IOException {
    Map<String, String> params = getDefaultParams();
    params.put(BaseChartWeb.CHART_PARAM_VALUES, "100");
    BarChart chart = new BarChart(params);
    BufferedImage img = chart.getChartImage();
    saveChart(img, "bar-chart-one-value.png");
    assertChartSizeGreaterThan(img, 100);
  }

  @Test
  public void testBarChartOthersColors() throws IOException {
    Map<String, String> params = getDefaultParams();
    params.put(BaseChartWeb.CHART_PARAM_COLORS, "FFFF00,9900FF");
    BarChart chart = new BarChart(params);
    BufferedImage img = chart.getChartImage();
    saveChart(img, "bar-chart-others-colors.png");
    assertChartSizeGreaterThan(img, 100);
  }

  @Test
  public void testBarChartNullValues() throws IOException {
    Map<String, String> params = getDefaultParams();
    params.put(BaseChartWeb.CHART_PARAM_VALUES, null);
    BarChart chart = new BarChart(params);
    BufferedImage img = chart.getChartImage();
    saveChart(img, "bar-chart-null-values.png");
    assertChartSizeGreaterThan(img, 100);
  }

  @Test
  public void testBarChartWrongValues() throws IOException {
    Map<String, String> params = getDefaultParams();
    params.put(BaseChartWeb.CHART_PARAM_VALUES, "wrong,value");
    BarChart chart = new BarChart(params);
    BufferedImage img = chart.getChartImage();
    saveChart(img, "bar-chart-wrong-values.png");
    assertChartSizeGreaterThan(img, 100);
  }

  @Test
  public void testBarChartTitle() throws IOException {
    Map<String, String> params = getDefaultParams();
    params.put(BaseChartWeb.CHART_PARAM_TITLE, "JFreeChart by Servlet");
    params.put(BaseChartWeb.CHART_PARAM_DIMENSIONS, "750x250");
    BarChart chart = new BarChart(params);
    BufferedImage img = chart.getChartImage();
    saveChart(img, "bar-chart-title.png");
    assertChartSizeGreaterThan(img, 100);
  }

  @Test
  @Ignore
  public void testBarChartTitle2() throws IOException {
    Map<String, String> params = getDefaultParams();
    params.put(BaseChartWeb.CHART_PARAM_TITLE, "JFreeChart by Servlet");
    params.put(BaseChartWeb.CHART_PARAM_DIMENSIONS, "750x250");
    params.put(BaseChartWeb.CHART_PARAM_TYPE, BaseChartWeb.BAR_CHART_VERTICAL);
    params.put(BaseChartWeb.CHART_PARAM_CATEGORIES, "0+,5+,10+,20+,30+,60+,90+");
    params.put(BaseChartWeb.CHART_PARAM_SERIES, "1,2,3");
    params.put(BaseChartWeb.CHART_PARAM_CATEGORIES_AXISMARGIN_VISIBLE, "y");
    params.put(BaseChartWeb.CHART_PARAM_RANGEAXIS_VISIBLE, "y");
    params.put(BaseChartWeb.CHART_PARAM_VALUES, "100,50,75,92,30,58");
    BarChart chart = new BarChart(params);
    BufferedImage img = chart.getChartImage();
    saveChart(img, "bar-chart-vertical-multi-series.png");
    assertChartSizeGreaterThan(img, 100);
  }

  @Test
  public void testStackedBarCharteightyTwenty() throws IOException {
    Map<String, String> params = new HashMap<String, String>();
    params.put(BaseChartWeb.CHART_PARAM_TYPE, BaseChartWeb.STACKED_BAR_CHART);
    params.put(BaseChartWeb.CHART_PARAM_VALUES, "80,20");
    BarChart chart = new BarChart(params);
    BufferedImage img = chart.getChartImage();
    saveChart(img, "stacked-bar-chart-80-20.png");
    assertChartSizeGreaterThan(img, 100);
  }

  @Test
  public void testStackedBarChartfiftyFifty() throws IOException {
    Map<String, String> params = new HashMap<String, String>();
    params.put(BaseChartWeb.CHART_PARAM_TYPE, BaseChartWeb.STACKED_BAR_CHART);
    params.put(BaseChartWeb.CHART_PARAM_VALUES, "50,50");
    BarChart chart = new BarChart(params);
    BufferedImage img = chart.getChartImage();
    saveChart(img, "stacked-bar-chart-50-50.png");
    assertChartSizeGreaterThan(img, 100);
  }

  private Map<String, String> getDefaultParams() {
    Map<String, String> params = new HashMap<String, String>();
    params.put(BaseChartWeb.CHART_PARAM_TYPE, BaseChartWeb.BAR_CHART_HORIZONTAL);
    params.put(BaseChartWeb.CHART_PARAM_VALUES, "100,50");
    return params;
  }

}
