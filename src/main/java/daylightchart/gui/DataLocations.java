/*
 * Copyright (c) 2004-2007 Sualeh Fatehi. All Rights Reserved.
 */

package daylightchart.gui;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import daylightchart.location.$Formatter;
import daylightchart.location.$Parser;
import daylightchart.location.Latitude;
import daylightchart.location.Location;
import daylightchart.location.ParserException;

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
    try
    {
      final InputStream dataStream = this.getClass().getClassLoader()
        .getResourceAsStream("locations.data");
      if (dataStream == null)
      {
        throw new IllegalStateException("Cannot read data from internal database");
      }
      final InputStreamReader reader = new InputStreamReader(dataStream);
      locations = $Parser.parseLocations(reader);
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
   * @throws IOException
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
    locations = $Parser.parseLocations(reader);
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
    if (sortOrder == LocationsSortOrder.BY_NAME)
    {
      // Do nothing special
    }
    else if (sortOrder == LocationsSortOrder.BY_LATITUDE)
    {
      comparator = new Comparator<Location>()
      {

        public int compare(final Location location1, final Location location2)
        {
          final Latitude latitude1 = location1.getCoordinates().getLatitude();
          final Latitude latitude2 = location2.getCoordinates().getLatitude();
          return (int) Math.signum(latitude2.getRadians()
                                   - latitude1.getRadians());
        }
      };
    }
    Collections.sort(locations, comparator);
  }

  /**
   * Writes location data out to a file.
   * 
   * @param dataFile
   *        File to write to.
   * @throws IOException
   *         On an exception.
   */
  public void writeDataToFile(final File dataFile)
    throws IOException
  {
    final BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
    $Formatter.formatLocations(locations, writer);
  }

}
