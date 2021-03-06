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
package org.geoname.test;


import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.util.Collection;

import org.geoname.data.Location;
import org.geoname.parser.FormatterException;
import org.geoname.parser.LocationFormatter;
import org.geoname.parser.LocationsListParser;
import org.geoname.parser.ParserException;
import org.junit.Test;

public class TestLocation
{

  @Test
  public void location()
    throws ParserException, FormatterException
  {

    final String locationString = "Aberdeen;GB;Europe/London;+5710-00204/";
    final Location location = LocationsListParser.parseLocation(locationString);

    assertEquals(locationString, LocationFormatter.formatLocation(location));

  }

  @Test
  public void locations()
    throws ParserException
  {
    final InputStream dataStream = this.getClass().getClassLoader()
      .getResourceAsStream("locations.data");
    final Collection<Location> locations = new LocationsListParser(dataStream)
      .parseLocations();

    assertEquals(109, locations.size());
  }

}
