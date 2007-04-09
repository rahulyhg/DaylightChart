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
package daylightchart.test;


import static org.junit.Assert.assertEquals;

import org.junit.Test;

import daylightchart.location.$Formatter;
import daylightchart.location.$Parser;
import daylightchart.location.Location;
import daylightchart.location.ParserException;

/**
 * Location tests.
 */
public class TestLocation
{

  @Test
  public void location()
    throws ParserException
  {

    final String strLoc = "Aberdeen;UK;Europe/London;+5710-00204/";
    final Location location = $Parser.parseLocation(strLoc);

    assertEquals(strLoc, $Formatter.formatLocation(location));

  }

}
