/* 
 * 
 * Daylight Chart
 * http://sourceforge.net/projects/daylightchart
 * Copyright (c) 2007-2008, Sualeh Fatehi.
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */
package org.pointlocation6709.parser;


import java.text.NumberFormat;

import org.pointlocation6709.Angle;
import org.pointlocation6709.Latitude;
import org.pointlocation6709.Longitude;
import org.pointlocation6709.PointLocation;

/**
 * Formats point locations to strings.
 * 
 * @author Sualeh Fatehi
 */
public final class PointLocationFormatter
{

  /**
   * Formats a point location as an ISO 6709:1983 string.
   * 
   * @param pointLocation
   *        Point location to format
   * @param formatType
   *        Format type
   * @return Formatted string
   * @throws FormatterException
   *         On an exception
   */
  public static String formatIso6709(final PointLocation pointLocation,
                                     final PointLocationFormatType formatType)
    throws FormatterException
  {
    if (pointLocation == null)
    {
      throw new FormatterException("No point location provided");
    }
    if (formatType == null)
    {
      throw new FormatterException("No format type provided");
    }

    String formattedPointLocation = "";
    switch (formatType)
    {
      case DECIMAL:
        formattedPointLocation = formatIso6709WithDecimals(pointLocation);
        break;
      case LONG:
        formattedPointLocation = formatIso6709Long(pointLocation);
        break;
      case MEDIUM:
        formattedPointLocation = formatIso6709Medium(pointLocation);
        break;
      case SHORT:
        formattedPointLocation = formatIso6709Short(pointLocation);
        break;
      default:
        throw new FormatterException("Unsupported format type");
    }
    return formattedPointLocation;

  }

  /**
   * Formats a latitude as an ISO 6709:1983 string.
   * 
   * @param latitude
   *        Latitude to format
   * @param formatType
   *        Format type
   * @return Formatted string
   * @throws FormatterException
   *         On an exception
   */
  public static String formatLatitude(final Latitude latitude,
                                      final PointLocationFormatType formatType)
    throws FormatterException
  {
    if (latitude == null)
    {
      throw new FormatterException("No point location provided");
    }
    if (formatType == null)
    {
      throw new FormatterException("No format type provided");
    }

    String formattedPointLocation = "";
    switch (formatType)
    {
      case DECIMAL:
        formattedPointLocation = formatLatitudeWithDecimals(latitude);
        break;
      case LONG:
        formattedPointLocation = formatLatitudeLong(latitude);
        break;
      case MEDIUM:
        formattedPointLocation = formatLatitudeMedium(latitude);
        break;
      case SHORT:
        formattedPointLocation = formatLatitudeShort(latitude);
        break;
      default:
        throw new FormatterException("Unsupported format type");
    }
    return formattedPointLocation;

  }

  /**
   * Formats a longitude as an ISO 6709:1983 string.
   * 
   * @param longitude
   *        Longitude to format
   * @param formatType
   *        Format type
   * @return Formatted string
   * @throws FormatterException
   *         On an exception
   */
  public static String formatLongitude(final Longitude longitude,
                                       final PointLocationFormatType formatType)
    throws FormatterException
  {
    if (longitude == null)
    {
      throw new FormatterException("No point location provided");
    }
    if (formatType == null)
    {
      throw new FormatterException("No format type provided");
    }

    String formattedPointLocation = "";
    switch (formatType)
    {
      case DECIMAL:
        formattedPointLocation = formatLongitudeWithDecimals(longitude);
        break;
      case LONG:
        formattedPointLocation = formatLongitudeLong(longitude);
        break;
      case MEDIUM:
        formattedPointLocation = formatLongitudeMedium(longitude);
        break;
      case SHORT:
        formattedPointLocation = formatLongitudeShort(longitude);
        break;
      default:
        throw new FormatterException("Unsupported format type");
    }
    return formattedPointLocation;

  }

  private static String formatDecimalMinutesString(final Angle angle)
  {
    final int intDegrees = Math.abs(angle.getField(Angle.Field.DEGREES));
    final double absMinutes = Math.abs(angle.getDegrees()) - intDegrees;
    return getNumberFormat(0).format(absMinutes);
  }

  private static String formatDoubleWithSign(final double value)
  {
    final String sign = value < 0? "-": "+";
    return sign + getNumberFormat(1).format(Math.abs(value));
  }

  private static String formatIntegerDegreesString(final Angle angle)
  {
    String sign = angle.getRadians() < 0? "-": "+";
    final int intDegrees = Math.abs(angle.getField(Angle.Field.DEGREES));

    final int fieldlength;
    if (angle instanceof Latitude)
    {
      fieldlength = 2;
    }
    else if (angle instanceof Longitude)
    {
      // According to the ISO6709:1983 standard,
      // the 180th meridian is negative
      if (intDegrees == 180)
      {
        sign = "-";
      }
      fieldlength = 3;
    }
    else
    {
      fieldlength = 0;
    }

    final String degrees = getIntegerFormat(fieldlength).format(intDegrees);

    return sign + degrees;
  }

  /**
   * Formats a point location as an ISO 6709:1983 string.
   * 
   * @param pointLocation
   *        Point location to format
   * @return Formatted string
   */
  private static String formatIso6709Long(final PointLocation pointLocation)
  {
    final Latitude latitude = pointLocation.getLatitude();
    final Longitude longitude = pointLocation.getLongitude();
    String string = formatLatitudeLong(latitude) +
                    formatLongitudeLong(longitude);
    final double altitude = pointLocation.getAltitude();
    if (altitude != 0)
    {
      string = string + formatDoubleWithSign(altitude);
    }
    return string + "/";
  }

  /**
   * Formats a point location as an ISO 6709:1983 string.
   * 
   * @param pointLocation
   *        Point location to format
   * @return Formatted string
   */
  private static String formatIso6709Medium(final PointLocation pointLocation)
  {
    final Latitude latitude = pointLocation.getLatitude();
    final Longitude longitude = pointLocation.getLongitude();
    String string = formatLatitudeMedium(latitude) +
                    formatLongitudeMedium(longitude);
    final double altitude = pointLocation.getAltitude();
    if (altitude != 0)
    {
      string = string + formatDoubleWithSign(altitude);
    }
    return string + "/";
  }

  /**
   * Formats a point location as an ISO 6709:1983 string.
   * 
   * @param pointLocation
   *        Point location to format
   * @return Formatted string
   */
  private static String formatIso6709Short(final PointLocation pointLocation)
  {
    final Latitude latitude = pointLocation.getLatitude();
    final Longitude longitude = pointLocation.getLongitude();
    String string = formatLatitudeShort(latitude) +
                    formatLongitudeShort(longitude);
    final double altitude = pointLocation.getAltitude();
    if (altitude != 0)
    {
      string = string + formatDoubleWithSign(altitude);
    }
    return string + "/";
  }

  /**
   * Formats a point location as an ISO 6709:1983 string, using
   * decimals.
   * 
   * @param pointLocation
   *        Point location to format
   * @return Formatted string
   */
  private static String formatIso6709WithDecimals(final PointLocation pointLocation)
  {
    final Latitude latitude = pointLocation.getLatitude();
    final Longitude longitude = pointLocation.getLongitude();
    String string = formatLatitudeWithDecimals(latitude) +
                    formatLongitudeWithDecimals(longitude);
    final double altitude = pointLocation.getAltitude();
    if (altitude != 0)
    {
      string = string + formatDoubleWithSign(altitude);
    }
    return string + "/";
  }

  private static String formatLatitudeLong(final Latitude latitude)
  {
    final String string = formatLatitudeShort(latitude) +
                          formatSexagesimalMinutesStringLong(latitude);
    return string;
  }

  private static String formatLatitudeMedium(final Latitude latitude)
  {
    final String string = formatLatitudeShort(latitude) +
                          formatSexagesimalMinutesStringMedium(latitude);
    return string;
  }

  private static String formatLatitudeShort(final Latitude latitude)
  {
    return formatIntegerDegreesString(latitude);
  }

  private static String formatLatitudeWithDecimals(final Latitude latitude)
  {
    final String string = formatLatitudeShort(latitude) +
                          formatDecimalMinutesString(latitude);
    return string;
  }

  private static String formatLongitudeLong(final Longitude longitude)
  {
    final String string = formatLongitudeShort(longitude) +
                          formatSexagesimalMinutesStringLong(longitude);
    return string;
  }

  private static String formatLongitudeMedium(final Longitude longitude)
  {
    final String string = formatLongitudeShort(longitude) +
                          formatSexagesimalMinutesStringMedium(longitude);
    return string;
  }

  private static String formatLongitudeShort(final Longitude longitude)
  {
    return formatIntegerDegreesString(longitude);
  }

  private static String formatLongitudeWithDecimals(final Longitude longitude)
  {
    final String string = formatIntegerDegreesString(longitude) +
                          formatDecimalMinutesString(longitude);
    return string;
  }

  private static String formatSexagesimalMinutesStringLong(final Angle angle)
  {
    final int absMinutes = Math.abs(angle.getField(Angle.Field.MINUTES));
    final int absSeconds = Math.abs(angle.getField(Angle.Field.SECONDS));

    final NumberFormat integerFormat = getIntegerFormat(2);
    return integerFormat.format(absMinutes) + integerFormat.format(absSeconds);
  }

  private static String formatSexagesimalMinutesStringMedium(final Angle angle)
  {
    int absMinutes = Math.abs(angle.getField(Angle.Field.MINUTES));
    final int absSeconds = Math.abs(angle.getField(Angle.Field.SECONDS));
    if (absMinutes < 59 && absSeconds >= 30)
    {
      absMinutes = absMinutes + 1;
    }

    final NumberFormat integerFormat = getIntegerFormat(2);
    return integerFormat.format(absMinutes);
  }

  private static NumberFormat getIntegerFormat(final int integerDigits)
  {
    final NumberFormat numberFormat = NumberFormat.getIntegerInstance();
    numberFormat.setMinimumIntegerDigits(integerDigits);
    return numberFormat;
  }

  private static NumberFormat getNumberFormat(final int integerDigits)
  {
    final NumberFormat numberFormat = NumberFormat.getInstance();
    numberFormat.setMinimumIntegerDigits(integerDigits);
    numberFormat.setMinimumFractionDigits(1);
    numberFormat.setMaximumFractionDigits(5);
    numberFormat.setGroupingUsed(false);
    return numberFormat;
  }

  private PointLocationFormatter()
  {
    // Prevent instantiation
  }

}
