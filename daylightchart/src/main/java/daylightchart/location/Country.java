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

public final class Country
  implements Serializable
{

  private static final long serialVersionUID = -5625327893850178062L;

  private final String countryName;
  private final String iso3166CountryCode2;
  private final String fips10CountryCode;

  Country(final String countryRecord)
  {
    if (countryRecord == null)
    {
      throw new IllegalArgumentException("No country record provided");
    }

    final String[] fields = countryRecord.split(",");

    final boolean invalidNumberOfFields = fields.length != 3;
    final boolean invalidHasNulls = fields[0] == null || fields[1] == null
                                    || fields[2] == null;
    final boolean invalidLengths = fields[0].length() != 2
                                   || fields[2].length() == 0;
    if (invalidNumberOfFields || invalidHasNulls || invalidLengths)
    {
      throw new IllegalArgumentException("Invalid country record: "
                                         + countryRecord);
    }

    iso3166CountryCode2 = fields[0];
    fips10CountryCode = fields[1];
    countryName = fields[2];

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
    if (getClass() != obj.getClass())
    {
      return false;
    }
    final Country other = (Country) obj;
    if (countryName == null)
    {
      if (other.countryName != null)
      {
        return false;
      }
    }
    else if (!countryName.equals(other.countryName))
    {
      return false;
    }
    if (fips10CountryCode == null)
    {
      if (other.fips10CountryCode != null)
      {
        return false;
      }
    }
    else if (!fips10CountryCode.equals(other.fips10CountryCode))
    {
      return false;
    }
    if (iso3166CountryCode2 == null)
    {
      if (other.iso3166CountryCode2 != null)
      {
        return false;
      }
    }
    else if (!iso3166CountryCode2.equals(other.iso3166CountryCode2))
    {
      return false;
    }
    return true;
  }

  /**
   * @return the countryName
   */
  public String getCountryName()
  {
    return countryName;
  }

  /**
   * @return the fips10CountryCode
   */
  public String getFips10CountryCode()
  {
    return fips10CountryCode;
  }

  /**
   * @return the iso3166CountryCode2
   */
  public String getIso3166CountryCode2()
  {
    return iso3166CountryCode2;
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
    result = prime * result + (countryName == null? 0: countryName.hashCode());
    result = prime * result
             + (fips10CountryCode == null? 0: fips10CountryCode.hashCode());
    result = prime * result
             + (iso3166CountryCode2 == null? 0: iso3166CountryCode2.hashCode());
    return result;
  }

  public String toString()
  {
    return countryName;
  }
  
}