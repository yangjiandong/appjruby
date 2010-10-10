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

import org.jfree.chart.plot.CategoryPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.RectangleInsets;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
import java.util.StringTokenizer;

public class CustomBarChart extends BarChart {

  private CustomBarRenderer renderer = null;

  public CustomBarChart(Map<String, String> params) {
    super(params);
  }

  @Override
  protected BufferedImage getChartImage() throws IOException {
    configure();
    return getBufferedImage(jfreechart);
  }

  @Override
  protected void configure() {
    configureChart(jfreechart, false);
    configureCategoryDataset();
    configureCategoryAxis();
    configureRenderer();
    configureRangeAxis();
    configureCategoryPlot();
    applyParams();
  }

  @Override
  protected void configureCategoryDataset() {
    dataset = new DefaultCategoryDataset();
  }

  @Override
  protected void configureRenderer() {
    renderer = new CustomBarRenderer(null);
    renderer.setItemMargin(0.0);
    renderer.setDrawBarOutline(false);
  }

  @Override
  protected void configureCategoryPlot() {
    CategoryPlot plot = jfreechart.getCategoryPlot();
    plot.setNoDataMessage(DEFAULT_MESSAGE_NODATA);
    plot.setInsets(RectangleInsets.ZERO_INSETS); // To remove inner space around chart
    plot.setDataset(dataset);
    plot.setDomainAxis(categoryAxis);
    plot.setRenderer(renderer);
    plot.setRangeAxis(numberAxis);
  }

  protected void applyParams() {
    applyCommonParams();
    applyCommomParamsBar();

    configureColors(params.get(CHART_PARAM_COLORS));
    addMeasures(params.get(CHART_PARAM_VALUES));
  }

  private void configureColors(String colorsParam) {
    Paint[] colors = CustomBarRenderer.COLORS;
    if (colorsParam != null && colorsParam.length() > 0) {
      StringTokenizer stColors = new StringTokenizer(colorsParam, ",");
      colors = new Paint[stColors.countTokens()];
      int i = 0;
      while (stColors.hasMoreTokens()) {
        colors[i] = Color.decode("0x" + stColors.nextToken());
        i++;
      }
    }

    renderer.setColors(colors);
  }

  private void addMeasures(String values) {
    if (values != null && values.length() > 0) {
      // Values
      StringTokenizer stValues = new StringTokenizer(values, ",");
      int nbValues = stValues.countTokens();

      // Categories
      String categoriesParam = params.get(CHART_PARAM_CATEGORIES);
      boolean categoriesPresent = categoriesParam != null && categoriesParam.length() > 0;
      String[] categoriesSplit = null;
      if (categoriesPresent) {
        categoriesSplit = categoriesParam.split(",");
      } else {
        categoriesSplit = new String[nbValues];
        for (int i = 0; i < nbValues; i++) {
          categoriesSplit[i] = DEFAULT_NAME_CATEGORY + i;
        }
      }
      int nbCategories = categoriesSplit.length;

      // Series
      String[] seriesSplit = {DEFAULT_NAME_SERIE};
      int nbSeries = 1;

      //
      for (int iCategories = 0; iCategories < nbCategories; iCategories++) {
        String currentCategory = categoriesSplit[iCategories];
        for (int iSeries = 0; iSeries < nbSeries; iSeries++) {
          String currentSerie = seriesSplit[iSeries];
          double currentValue = 0.0;
          if (stValues.hasMoreTokens()) {
            try {
              currentValue = Double.parseDouble(stValues.nextToken());
            } catch (NumberFormatException e) {
            }
          }
          dataset.addValue(currentValue, currentSerie, currentCategory);
        }
      }
    }
  }

}