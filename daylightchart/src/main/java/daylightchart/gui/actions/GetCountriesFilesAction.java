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
package daylightchart.gui.actions;


import daylightchart.gui.Messages;

public final class GetCountriesFilesAction
  extends BaseBrowserAction
{

  private static final long serialVersionUID = 4002590686393404496L;

  public GetCountriesFilesAction()
  {
    super(Messages.getString("DaylightChartGui.Menu.Actions.GetCountriesFiles"), //$NON-NLS-1$
          "http://geonames.nga.mil/gns/html/namefiles.html" //$NON-NLS-1$
    );
  }

}
