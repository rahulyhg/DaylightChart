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
package org.geoname.parser;


import static us.fatehi.pointlocation6709.format.PointLocationFormatType.MEDIUM;
import static us.fatehi.pointlocation6709.format.PointLocationFormatter.formatPointLocation;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;

import org.geoname.data.Country;
import org.geoname.data.Location;

/**
 * Formats objects to strings.
 * 
 * @author Sualeh Fatehi
 */
public final class LocationFormatter
{

  /**
   * Formats location to a string, in a parseable way.
   * 
   * @param location
   *        Location to format
   * @return Formated string.
   * @throws org.geoname.parser.FormatterException
   */
  public static String formatLocation(final Location location)
    throws org.geoname.parser.FormatterException
  {
    String coordinatesString;
    try
    {
      coordinatesString = formatPointLocation(location.getPointLocation(),
                                              MEDIUM);
    }
    catch (final us.fatehi.pointlocation6709.format.FormatterException e)
    {
      throw new org.geoname.parser.FormatterException(e);
    }
    final String tzId = location.getTimeZoneId();

    final String city = location.getCity();
    final Country country = location.getCountry();

    final StringBuffer representation = new StringBuffer().append(city)
      .append(";").append(country.getCode()).append(";").append(tzId)
      .append(";").append(coordinatesString);
    return new String(representation);
  }

  /**
   * Writes location data out to a file. *
   * 
   * @param locations
   *        Locations to write.
   * @param writer
   *        Writer to write to.
   * @throws org.geoname.parser.FormatterException
   *         On an exception.
   */
  public static void formatLocations(final Collection<Location> locations,
                                     final Writer writer)
                                       throws org.geoname.parser.FormatterException
  {
    if (locations == null || writer == null)
    {
      return;
    }

    try
    {
      final String line_separator = System.getProperty("line.separator", "\n");
      for (final Location location: locations)
      {
        writer.write(formatLocation(location) + line_separator);
      }
      writer.flush();
      writer.close();
    }
    catch (final IOException e)
    {
      throw new org.geoname.parser.FormatterException(e);
    }
  }

  /**
   * Gets the tooltip for a location.
   * 
   * @param location
   *        Loaction.
   * @return Tooltip
   */
  public static String getToolTip(final Location location)
  {
    String toolTip = "";
    if (location != null)
    {
      toolTip = "<html><b>" + location.toString() + "</b><br>"
                + location.getDetails() + "</html>";
    }
    return toolTip;
  }

  private LocationFormatter()
  {
  }

}
