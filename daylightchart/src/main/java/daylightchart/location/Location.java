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


import java.io.Serializable;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.pointlocation6709.Longitude;
import org.pointlocation6709.PointLocation;

/**
 * A location object has all the information required to define a
 * location, such as the name of the city and the country, the point
 * location, and the time zone.
 * 
 * @author Sualeh Fatehi
 */
public final class Location
  implements Serializable, Comparable<Location>
{

  private static final long serialVersionUID = 7929385835483597186L;

  private static final Logger LOGGER = Logger.getLogger(Location.class
    .getName());

  private final String city;
  private final Country country;
  private final PointLocation pointLocation;
  private final String tzId;
  
  /**
   * Copy constructor. Copies the value of a provided location.
   * 
   * @param location
   *        Location to copy the value from.
   */
  public Location(final Location location)
  {
    this(location.city, location.country, location.tzId, location.pointLocation);
  }

  /**
   * Constructor.
   * 
   * @param city
   *        City
   * @param country
   *        Country
   * @param pointLocation
   *        Point location
   * @param tzId
   *        Timezone id
   */
  public Location(final String city,
                  final Country country,
                  final String tzId,
                  final PointLocation pointLocation)
  {

    this.city = city;
    if (country == null)
    {
      throw new IllegalArgumentException("Country needs to be specified");
    }    
    this.country = country;

    this.tzId = tzId;

    if (pointLocation == null)
    {
      throw new IllegalArgumentException("Both latitude and longitude need to be specified");
    }
    this.pointLocation = pointLocation;

    // Now that all fields are set, validate
    validateTimeZone();
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  public int compareTo(final Location location)
  {
    return toString().compareTo(location.toString());
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(final Object obj)
  {
    if (this == obj)
    {
      return true;
    }
    if (obj == null)
    {
      return false;
    }
    if (!(obj instanceof Location))
    {
      return false;
    }
    final Location other = (Location) obj;
    if (city == null)
    {
      if (other.city != null)
      {
        return false;
      }
    }
    else if (!city.equals(other.city))
    {
      return false;
    }
    if (pointLocation == null)
    {
      if (other.pointLocation != null)
      {
        return false;
      }
    }
    else if (!pointLocation.equals(other.pointLocation))
    {
      return false;
    }
    if (country == null)
    {
      if (other.country != null)
      {
        return false;
      }
    }
    else if (!country.equals(other.country))
    {
      return false;
    }
    if (tzId == null)
    {
      if (other.tzId != null)
      {
        return false;
      }
    }
    else if (!tzId.equals(other.tzId))
    {
      return false;
    }
    return true;
  }

  /**
   * City.
   * 
   * @return City.
   */
  public String getCity()
  {
    return city;
  }

  /**
   * Country.
   * 
   * @return Country.
   */
  public Country getCountry()
  {
    return country;
  }

  /**
   * Point location.
   * 
   * @return Point location
   */
  public PointLocation getPointLocation()
  {
    return pointLocation;
  }

  /**
   * Time zone id for this location.
   * 
   * @return Time zone.
   */
  public TimeZone getTimeZone()
  {
    return TimeZone.getTimeZone(tzId);
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + (city == null? 0: city.hashCode());
    result = prime * result
             + (pointLocation == null? 0: pointLocation.hashCode());
    result = prime * result + (country == null? 0: country.hashCode());
    result = prime * result + (tzId == null? 0: tzId.hashCode());
    return result;
  }

  /**
   * Description for the location. The description is the name of the
   * city and country, separated by a comma.
   * 
   * @return Description for this location.
   * @see #getCountry
   * @see #getCity
   */
  @Override
  public String toString()
  {
    String description = "";
    if (city != null && country != null)
    {
      description = city + ", " + country;
    }
    return description;
  }

  private void validateTimeZone()
  {
    final TimeZone timeZone = getTimeZone();
    final Longitude longitude = getPointLocation().getLongitude();

    final double tzOffsetHours = timeZone.getRawOffset() / (1000D * 60D * 60D);
    final double longitiudeTzOffsetHours = longitude.getDegrees() / 15D;
    final double hoursDifference = Math.abs(longitiudeTzOffsetHours
                                            - tzOffsetHours);
    // The tolerance band is a half hour on each side of the time zone,
    // plus about 10 minutes
    final double toleranceBand = 0.5 + 0.17;
    if (!(hoursDifference <= toleranceBand))
    {
      LOGGER.log(Level.INFO, toString() + ": Longitude (" + longitude
                             + ") and timezone (" + timeZone.getDisplayName()
                             + ") do not match (difference " + hoursDifference
                             + " hours)");
    }
  }
}