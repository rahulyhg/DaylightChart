/*
 *
 * Daylight Chart
 * http://sualeh.github.io/DaylightChart
 * Copyright (c) 2007-2016, Sualeh Fatehi.
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
package daylightchart.options;


import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.geoname.data.LocationsSortOrder;

import daylightchart.daylightchart.calculation.TwilightType;
import daylightchart.daylightchart.chart.ChartOrientation;
import daylightchart.daylightchart.chart.TimeZoneOption;
import daylightchart.options.chart.ChartOptions;
import sf.util.FileUtils;

/**
 * All Daylight Chart options.
 *
 * @author Sualeh Fatehi
 */
public class Options
  implements Serializable
{

  private static final long serialVersionUID = 3195704386171200909L;

  private LocationsSortOrder locationsSortOrder;
  private TimeZoneOption timeZoneOption;
  private ChartOrientation chartOrientation;
  private TwilightType twilightType;
  private boolean showChartLegend;
  private ChartOptions chartOptions;
  private Path workingDirectory;
  private boolean slimUi;

  /**
   * Default options.
   */
  public Options()
  {
    locationsSortOrder = LocationsSortOrder.BY_NAME;
    timeZoneOption = TimeZoneOption.USE_TIME_ZONE;
    chartOptions = new ChartOptions();
    chartOrientation = ChartOrientation.STANDARD;
    twilightType = TwilightType.CIVIL;
    showChartLegend = true;
  }

  /**
   * Get just the chart options.
   *
   * @return Chart options.
   */
  public final ChartOptions getChartOptions()
  {
    return chartOptions;
  }

  /**
   * Gets the chart orientation.
   *
   * @return Chart orientation
   */
  public ChartOrientation getChartOrientation()
  {
    return chartOrientation;
  }

  /**
   * @return the locationsSortOrder
   */
  public final LocationsSortOrder getLocationsSortOrder()
  {
    return locationsSortOrder;
  }

  /**
   * @return the timeZoneOption
   */
  public final TimeZoneOption getTimeZoneOption()
  {
    return timeZoneOption;
  }

  /**
   * Gets the twilight display setting for the chart.
   *
   * @return TwilightType setting
   */
  public TwilightType getTwilightType()
  {
    return twilightType;
  }

  /**
   * Get the working directory.
   *
   * @return Working directory.
   */
  public Path getWorkingDirectory()
  {
    if (FileUtils.isDirectoryValid(workingDirectory))
    {
      return workingDirectory;
    }
    else
    {
      return Paths.get(".");
    }
  }

  /**
   * Whether to show the chart legend.
   *
   * @return Whether to show the chart legend
   */
  public boolean isShowChartLegend()
  {
    return showChartLegend;
  }

  /**
   * @return the slimUi
   */
  public boolean isSlimUi()
  {
    return slimUi;
  }

  /**
   * @param chartOptions
   *        the chartOptions to set
   */
  public final void setChartOptions(final ChartOptions chartOptions)
  {
    if (chartOptions != null)
    {
      this.chartOptions = chartOptions;
    }
  }

  /**
   * Sets the chart orientation.
   *
   * @param chartOrientation
   *        Chart orientation
   */
  public void setChartOrientation(final ChartOrientation chartOrientation)
  {
    if (chartOrientation != null)
    {
      this.chartOrientation = chartOrientation;
    }
  }

  /**
   * @param locationsSortOrder
   *        the locationsSortOrder to set
   */
  public final void setLocationsSortOrder(final LocationsSortOrder locationsSortOrder)
  {
    if (locationsSortOrder != null)
    {
      this.locationsSortOrder = locationsSortOrder;
    }
  }

  /**
   * Whether to show the chart legend.
   *
   * @param showChartLegend
   *        Whether to show the chart legend
   */
  public void setShowChartLegend(final boolean showChartLegend)
  {
    this.showChartLegend = showChartLegend;
  }

  /**
   * Whether the UI should work in "slim" mode.
   *
   * @param slimUi
   *        Slim mode setting.
   */
  public void setSlimUi(final boolean slimUi)
  {
    this.slimUi = slimUi;
  }

  /**
   * @param timeZoneOption
   *        the timeZoneOption to set
   */
  public final void setTimeZoneOption(final TimeZoneOption timeZoneOption)
  {
    if (timeZoneOption != null)
    {
      this.timeZoneOption = timeZoneOption;
    }
  }

  /**
   * Sets the twilight display setting for the chart.
   *
   * @param twilight
   *        TwilightType setting
   */
  public void setTwilightType(final TwilightType twilight)
  {
    if (twilight != null)
    {
      twilightType = twilight;
    }
  }

  /**
   * Working directory.
   *
   * @param workingDirectory
   *        Working directory.
   */
  public void setWorkingDirectory(final Path workingDirectory)
  {
    if (FileUtils.isDirectoryValid(workingDirectory))
    {
      this.workingDirectory = workingDirectory;
    }
  }

  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return ReflectionToStringBuilder.toString(this,
                                              ToStringStyle.MULTI_LINE_STYLE);
  }

}
