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
package daylightchart.location;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.pointlocation6709.Latitude;

import daylightchart.UserPreferences;
import daylightchart.location.parser.FormatterException;
import daylightchart.location.parser.LocationFormatter;
import daylightchart.location.parser.LocationParser;
import daylightchart.location.parser.ParserException;

/**
 * In-memory database of locations.
 * 
 * @author Sualeh Fatehi
 */
public final class DataLocations
{

  private static final long serialVersionUID = -2155899588206966572L;

  private List<Location> locations;

  /**
   * Loads data from internal database.
   */
  public DataLocations()
  {

    locations = new UserPreferences().getLocations();
    try
    {
      if (locations.size() == 0)
      {
        final InputStream dataStream = this.getClass().getClassLoader()
          .getResourceAsStream("locations.data");
        if (dataStream == null)
        {
          throw new IllegalStateException("Cannot read data from internal database");
        }
        Reader reader = new InputStreamReader(dataStream);
        locations = LocationParser.parseLocations(reader);
      }
    }
    catch (final ParserException e)
    {
      throw new IllegalStateException("Cannot read data from internal database",
                                      e);
    }

  }

  /**
   * Loads data from external file.
   * 
   * @param dataFile
   *        Data file.
   * @throws ParserException
   *         On an exception
   * @throws IOException
   *         On an exception
   */
  public DataLocations(final File dataFile)
    throws IOException, ParserException
  {
    if (!dataFile.exists() || !dataFile.canRead())
    {
      throw new IllegalArgumentException("Cannot read data from "
                                         + dataFile.getAbsolutePath());
    }

    final FileReader reader = new FileReader(dataFile);
    locations = LocationParser.parseLocations(reader);

    // Save locations to user preferences
    new UserPreferences().setLocations(locations);
  }

  /**
   * Gets a list of locations.
   * 
   * @return Locations list.
   */
  public List<Location> getLocations()
  {
    return new ArrayList<Location>(locations);
  }

  /**
   * Sorts locations, given a sort order.
   * 
   * @param sortOrder
   *        Sort order.
   */
  public void sortLocations(final LocationsSortOrder sortOrder)
  {
    Comparator<Location> comparator = null;
    if (sortOrder == LocationsSortOrder.BY_LATITUDE)
    {
      comparator = new Comparator<Location>()
      {

        public int compare(final Location location1, final Location location2)
        {
          final Latitude latitude1 = location1.getPointLocation().getLatitude();
          final Latitude latitude2 = location2.getPointLocation().getLatitude();
          return (int) Math.signum(latitude2.getRadians()
                                   - latitude1.getRadians());
        }
      };
    }
    // else if (sortOrder == LocationsSortOrder.BY_NAME)
    // {
    // // Do nothing special
    // }
    Collections.sort(locations, comparator);
  }

  /**
   * Writes location data out to a file.
   * 
   * @param dataFile
   *        File to write to.
   * @throws FormatterException
   * @throws FormatterException
   *         On an exception.
   */
  public void writeDataToFile(final File dataFile)
    throws FormatterException
  {
    BufferedWriter writer;
    try
    {
      writer = new BufferedWriter(new FileWriter(dataFile));
    }
    catch (IOException e)
    {
      throw new FormatterException(e);
    }
    LocationFormatter.formatLocations(locations, writer);
  }

}