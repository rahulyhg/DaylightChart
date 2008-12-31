/* 
 * 
 * Daylight Chart
 * http://sourceforge.net/projects/daylightchart
 * Copyright (c) 2007-2009, Sualeh Fatehi.
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
package org.sunposition.calculation;


import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.joda.time.LocalDateTime;

/**
 * Dates and times for the solstices and equinoxes.
 * 
 * @author sfatehi
 */
public class Equinox
{

  private final LocalDateTime vernalEquinox;
  private final LocalDateTime summerSolstice;
  private final LocalDateTime autumnalEquinox;
  private final LocalDateTime winterSolstice;

  /**
   * Calculate the dates and times for the solstices and equinoxes.
   * 
   * @param year
   *        The year to calculate for,
   */
  public Equinox(final int year)
  {
    final double m, ve, ss, ae, ws;
    m = ((double) year - 2000) / 1000;
    final double m2 = m * m;
    final double m3 = m2 * m;
    final double m4 = m3 * m;
    ve = 2451623.80984 + 365242.37404 * m + 0.05169 * m2 - 0.00411 * m3
         - 0.00057 * m4;
    vernalEquinox = toDate(ve);
    ss = 2451716.56767 + 365241.62603 * m + 0.00325 * m2 + 0.00888 * m3
         - 0.00030 * m4;
    summerSolstice = toDate(ss);
    ae = 2451810.21715 + 365242.01767 * m - 0.11575 * m2 + 0.00337 * m3
         + 0.00078 * m4;
    autumnalEquinox = toDate(ae);
    ws = 2451900.05952 + 365242.74049 * m - 0.06223 * m2 - 0.00823 * m3
         + 0.00032 * m4;
    winterSolstice = toDate(ws);
  }

  /**
   * @return the autumnalEquinox
   */
  public LocalDateTime getAutumnalEquinox()
  {
    return autumnalEquinox;
  }

  /**
   * @return the summerSolstice
   */
  public LocalDateTime getSummerSolstice()
  {
    return summerSolstice;
  }

  /**
   * @return the vernalEquinox
   */
  public LocalDateTime getVernalEquinox()
  {
    return vernalEquinox;
  }

  /**
   * @return the winterSolstice
   */
  public LocalDateTime getWinterSolstice()
  {
    return winterSolstice;
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString()
  {
    return ReflectionToStringBuilder.toString(this,
                                              ToStringStyle.MULTI_LINE_STYLE);
  }

  private LocalDateTime toDate(final double jdn)
  {
    final double p = Math.floor(jdn + 0.5);
    final double s1 = p + 68569;
    final double n = Math.floor(4 * s1 / 146097);
    final double s2 = s1 - Math.floor((146097 * n + 3) / 4);
    final double i = Math.floor(4000 * (s2 + 1) / 1461001);
    final double s3 = s2 - Math.floor(1461 * i / 4) + 31;
    final double q = Math.floor(80 * s3 / 2447);
    final double e = s3 - Math.floor(2447 * q / 80);
    final double s4 = Math.floor(q / 11);

    final double mm = q + 2 - 12 * s4;
    final double yy = 100 * (n - 49) + i + s4;
    final double dd = e + jdn - p + 0.5;

    double hrs, min, sec, tm;

    tm = 24 * (dd - Math.floor(dd));
    hrs = Math.floor(tm);
    tm = 60 * (tm - hrs);
    min = Math.floor(tm);
    tm = 60 * (tm - min);
    sec = Math.round(tm);

    return new LocalDateTime((int) yy,
                             (int) mm,
                             (int) dd,
                             (int) hrs,
                             (int) min,
                             (int) sec);
  }

}
