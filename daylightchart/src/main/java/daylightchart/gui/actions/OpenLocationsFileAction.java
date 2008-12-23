/* 
 * 
 * Daylight Chart
 * http://sourceforge.net/projects/daylightchart
 * Copyright (c) 2007-2008, Sualeh Fatehi.
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


import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import sf.util.ui.Actions;
import sf.util.ui.ExtensionFileFilter;
import sf.util.ui.GuiAction;
import daylightchart.gui.DaylightChartGui;
import daylightchart.gui.Messages;
import daylightchart.location.Location;
import daylightchart.options.UserPreferences;

/**
 * Opens a locations file.
 * 
 * @author sfatehi
 */
public final class OpenLocationsFileAction
  extends GuiAction
{

  private static final class GuiActionListener
    implements ActionListener
  {
    private final DaylightChartGui mainWindow;

    private GuiActionListener(final DaylightChartGui mainWindow)
    {
      this.mainWindow = mainWindow;
    }

    /**
     * {@inheritDoc}
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(final ActionEvent actionevent)
    {
      final List<FileFilter> fileFilters = new ArrayList<FileFilter>();
      fileFilters.add(new ExtensionFileFilter("Data files", ".data"));
      fileFilters.add(new ExtensionFileFilter("Text files", ".txt"));
      final File selectedFile = Actions
        .showOpenDialog(mainWindow,
                        Messages
                          .getString("DaylightChartGui.Menu.File.LoadLocations"),
                        fileFilters,
                        new File(UserPreferences.getOptions()
                          .getWorkingDirectory(), "locations.data"),
                        Messages
                          .getString("DaylightChartGui.Message.Error.CannotOpenFile"));

      mainWindow.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

      try
      {
        final List<Location> locations = UserPreferences
          .loadLocationsFromFile(selectedFile);
        if (locations == null)
        {
          if (selectedFile != null)
          {
            LOGGER.log(Level.WARNING, Messages
              .getString("DaylightChartGui.Message.Error.CannotOpenFile")); //$NON-NLS-1$
            JOptionPane.showMessageDialog(mainWindow, Messages
              .getString("DaylightChartGui.Message.Error.CannotOpenFile") //$NON-NLS-1$
                                                      + "\n" //$NON-NLS-1$
                                                      + selectedFile, Messages
              .getString("DaylightChartGui.Message.Error.CannotOpenFile"), //$NON-NLS-1$
                                          JOptionPane.ERROR_MESSAGE);
          }
        }
        else
        {
          Collections.sort(locations, mainWindow.getOptions()
            .getLocationsSortOrder());
          mainWindow.setLocations(locations);
          UserPreferences.setWorkingDirectory(selectedFile.getParentFile());
        }
      }
      catch (final RuntimeException e)
      {
        // We catch exceptions, because otherwise the cursor may get
        // stuck in busy mode
        LOGGER.log(Level.WARNING, "Could not load locations");
      }

      mainWindow.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
    }
  }

  private static final long serialVersionUID = -177214864870181893L;

  private static final Logger LOGGER = Logger
    .getLogger(OpenLocationsFileAction.class.getName());

  /**
   * Shows an open dialog to open a locations file.
   * 
   * @param mainWindow
   *        Main Daylight Chart window
   */
  public OpenLocationsFileAction(final DaylightChartGui mainWindow)
  {

    super(Messages.getString("DaylightChartGui.Menu.File.LoadLocations"),//$NON-NLS-1$
          "/icons/load_locations.gif" //$NON-NLS-1$
    );
    setShortcutKey(KeyStroke.getKeyStroke("control O"));
    addActionListener(new GuiActionListener(mainWindow));

  }
}
