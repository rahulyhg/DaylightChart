/* 
 * 
 * Daylight Chart
 * http://sourceforge.net/projects/daylightchart
 * Copyright (c) 2007, Sualeh Fatehi.
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 */
package daylightchart.gui;


import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartPanel;

import daylightchart.chart.DaylightChart;
import daylightchart.location.Location;
import daylightchart.options.Options;
import daylightchart.options.UserPreferences;

/**
 * Tabbed pane for location charts.
 * 
 * @author sfatehi
 */
public class LocationsTabbedPane
  extends JTabbedPane
{

  private static final long serialVersionUID = -2086804705336786590L;

  /**
   * Add a new tab for the location.
   * 
   * @param location
   *        Location
   */
  public void addLocationTab(final Location location)
  {
    final Options options = UserPreferences.getOptions();
    final DaylightChart chart = new DaylightChart(location, options
      .getTimeZoneOption());
    options.getChartOptions().updateChart(chart);

    final ChartPanel chartPanel = new ChartPanel(chart);
    chartPanel.setBackground(Color.WHITE);
    chartPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    chartPanel.setPreferredSize(new Dimension(700, 495));

    final String tabTitle = location.toString();
    final String toolTip = "<html><b>" + tabTitle + "</b><br>" +
                           location.getDetails() + "</html>";

    addTab(tabTitle, new CloseTabIcon(), chartPanel, toolTip);
    setSelectedIndex(getComponentCount() - 1);
  }

  /**
   * Prints the selected chart.
   */
  public void printSelectedChart()
  {
    Actions.doPrintChartAction(getSelectedChart());
  }

  /**
   * Saves the selected chart.
   */
  public void saveSelectedChart()
  {
    Actions.doSaveChartAction(getSelectedChart());
  }

  private ChartPanel getSelectedChart()
  {
    return (ChartPanel) getSelectedComponent();
  }

}
