/*
 * Copyright (c) 2004-2007 Sualeh Fatehi. All Rights Reserved.
 */
package daylightchart.location;


import java.io.Serializable;
import java.util.Arrays;
import java.util.TimeZone;

/**
 * <p>
 * Represents a location. The Location class has all the information
 * required to define a location, such as the name of the city and the
 * country, the cordinates, and the time zone.
 * </p>
 * This is an example of the string representation of a location:
 * "Aberdeen;UK;Europe/London;+5710-00204/". The city and country appear
 * first, separated by ;. Next, there is a time zone id, and last the
 * corordinates.
 * 
 * @author Sualeh Fatehi
 */
public final class Location
  implements Serializable, Comparable<Location>
{

  private static final long serialVersionUID = 7929385835483597186L;

  private final String city, country;
  private final Coordinates coordinates;
  private final String tzId;

  /**
   * Copy constructor. Copies the value of a provided location.
   * 
   * @param location
   *        Location to copy the value from.
   * @throws NullPointerException
   *         If the argument is null
   */
  public Location(final Location location)
  {
    this(location.city, location.country, location.tzId, location.coordinates);
  }

  /**
   * Constructor.
   * 
   * @param city
   * @param country
   * @param coordinates
   * @param tzId
   */
  public Location(final String city,
                  final String country,
                  final String tzId,
                  final Coordinates coordinates)
  {

    this.city = city;
    this.country = country;

    this.tzId = tzId;

    if (coordinates == null)
    {
      throw new IllegalArgumentException("Both latitude and longitude need to be specified");
    }
    this.coordinates = coordinates;

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
  public boolean equals(Object obj)
  {
    if (this == obj) return true;
    if (obj == null) return false;
    if (!(obj instanceof Location)) return false;
    final Location other = (Location) obj;
    if (city == null)
    {
      if (other.city != null) return false;
    }
    else if (!city.equals(other.city)) return false;
    if (coordinates == null)
    {
      if (other.coordinates != null) return false;
    }
    else if (!coordinates.equals(other.coordinates)) return false;
    if (country == null)
    {
      if (other.country != null) return false;
    }
    else if (!country.equals(other.country)) return false;
    if (tzId == null)
    {
      if (other.tzId != null) return false;
    }
    else if (!tzId.equals(other.tzId)) return false;
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
   * Coordinates
   * 
   * @return Coordinates
   */
  public Coordinates getCoordinates()
  {
    return coordinates;
  }

  /**
   * Country.
   * 
   * @return Country.
   */
  public String getCountry()
  {
    return country;
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
    result = prime * result + ((city == null)? 0: city.hashCode());
    result = prime * result
             + ((coordinates == null)? 0: coordinates.hashCode());
    result = prime * result + ((country == null)? 0: country.hashCode());
    result = prime * result + ((tzId == null)? 0: tzId.hashCode());
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
    final String[] ignoreTzIds = new String[] {
      "IST"
    };
    final TimeZone timeZone = getTimeZone();
    if (Arrays.asList(ignoreTzIds).contains(timeZone.getID()))
    {
      return;
    }
    final int rawOffset = timeZone.getRawOffset();
    final double tzOffsetHours = rawOffset / (1000D * 60D * 60D);
    final double longitiudeTzOffsetHours = getCoordinates().getLongitude()
      .getDegrees() / 15D;
    boolean longitudeInTz = Math.abs(longitiudeTzOffsetHours - tzOffsetHours) <= 0.5;
    if (!longitudeInTz)
    {
      System.err
        .println
        /* throw new IllegalStateException */("Longitude and timezones "
                                               + "do not match for "
                                               + toString());
    }
  }

}
