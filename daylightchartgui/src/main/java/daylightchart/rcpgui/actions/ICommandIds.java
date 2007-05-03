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
package daylightchart.rcpgui.actions;


/**
 * Interface defining the application's command IDs. Key bindings can be
 * defined for specific commands. To associate an action with a command,
 * use IAction.setActionDefinitionId(commandId).
 * 
 * @see org.eclipse.jface.action.IAction#setActionDefinitionId(String)
 */
public interface ICommandIds
{

  public static final String CMD_OPEN_LOCATONS_FILE = OpenLocationsFileAction.ID;
  public static final String CMD_SAVE_LOCATONS_FILE = SaveLocationsFileAction.ID;

  public static final String CMD_SAVE_CHART = "daylightchart.rcpgui.LoadLocationsAction";

}
