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

import daylightchart.location.parser.DefaultTimezones;
import daylightchart.location.parser.ParserException;

/**
 * Location tests.
 */
public class TestRounding
{

  @Test
  public void rounding()
    throws ParserException
  {
    assertEquals(2.0, DefaultTimezones.roundToNearestHalf(1.9));
    assertEquals(2.0, DefaultTimezones.roundToNearestHalf(2.0));
    assertEquals(2.0, DefaultTimezones.roundToNearestHalf(2.1));
    
    assertEquals(2.5, DefaultTimezones.roundToNearestHalf(2.4));
    assertEquals(2.5, DefaultTimezones.roundToNearestHalf(2.5));
    assertEquals(2.5, DefaultTimezones.roundToNearestHalf(2.6));
    
//    assertEquals(0.0, DefaultTimezones.roundToNearestHalf(-0.1));
    assertEquals(0.0, DefaultTimezones.roundToNearestHalf(0));
//    assertEquals(0.0, DefaultTimezones.roundToNearestHalf(0.1));
    
    assertEquals(-2.0, DefaultTimezones.roundToNearestHalf(-1.9));
    assertEquals(-2.0, DefaultTimezones.roundToNearestHalf(-2.0));
    assertEquals(-2.0, DefaultTimezones.roundToNearestHalf(-2.1));
    
    assertEquals(-2.5, DefaultTimezones.roundToNearestHalf(-2.4));
    assertEquals(-2.5, DefaultTimezones.roundToNearestHalf(-2.5));
    assertEquals(-2.5, DefaultTimezones.roundToNearestHalf(-2.6));    
  }

}