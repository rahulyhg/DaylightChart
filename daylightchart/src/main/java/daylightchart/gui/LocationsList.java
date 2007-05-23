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
package daylightchart.gui;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import daylightchart.location.Location;
import daylightchart.location.parser.LocationFormatter;
import daylightchart.options.UserPreferences;

/**
 * Locations list component.
 * 
 * @author Sualeh Fatehi
 */
public class LocationsList
  extends JPanel
{

  private static final long serialVersionUID = -6884483130453983685L;

  private final DaylightChartGui parent;
  private final JList locationsList;
  private List<Location> locations;
  private DaylightChartGuiAction add;
  private DaylightChartGuiAction delete;
  private DaylightChartGuiAction edit;

  /**
   * Create a new locations list component.
   * 
   * @param parent
   *        Main window.
   */
  public LocationsList(final DaylightChartGui parent)
  {

    super(new BorderLayout());

    this.parent = parent;

    createActions();

    final JToolBar toolBar = new JToolBar();
    add(toolBar, BorderLayout.NORTH);
    toolBar.add(add);
    toolBar.add(delete);
    toolBar.add(edit);

    locationsList = new JList();
    add(new JScrollPane(locationsList));
    locationsList.setCellRenderer(new DefaultListCellRenderer()
    {
      private static final long serialVersionUID = -5892518623547830472L;

      @Override
      public Component getListCellRendererComponent(final JList list,
                                                    final Object value,
                                                    final int index,
                                                    final boolean isSelected,
                                                    final boolean cellHasFocus)
      {
        super.getListCellRendererComponent(list,
                                           value,
                                           index,
                                           isSelected,
                                           cellHasFocus);
        setToolTipText(LocationFormatter.getToolTip((Location) value));
        return this;
      }
    });
    locationsList.addMouseListener(new MouseAdapter()
    {
      @Override
      public void mouseClicked(final MouseEvent e)
      {
        if (!e.isConsumed())
        {
          if (e.getClickCount() == 2)
          {
            Location location = (Location) locationsList.getSelectedValue();
            if (location == null)
            {
              locationsList.setSelectedIndex(0);
              location = (Location) locationsList.getSelectedValue();
            }
            parent.addLocationTab(location);
          }
          e.consume();
        }
        else if (e.getButton() == MouseEvent.BUTTON2
                 || e.getButton() == MouseEvent.BUTTON3)
        {
          createPopupMenu().show(e.getComponent(), e.getX(), e.getY());
        }
      }
    });

    setLocations(UserPreferences.getLocations());
  }

  /**
   * Gets all locations in the list.
   * 
   * @return Locations list.
   */
  public List<Location> getLocations()
  {
    return new ArrayList<Location>(locations);
  }

  /**
   * Get the currently selected location.
   * 
   * @return Currently selected location.
   */
  public Location getSelectedLocation()
  {
    return (Location) locationsList.getSelectedValue();
  }

  public int getSelectedLocationIndex()
  {
    return locationsList.getSelectedIndex();
  }

  /**
   * Set the locations list.
   * 
   * @param locations
   *        Locations list.
   */
  public void setLocations(final List<Location> locations)
  {
    if (locations != null && locations.size() > 0)
    {
      this.locations = locations;
      locationsList.setListData(new Vector<Location>(locations));
      locationsList.setSelectedIndex(0);
      UserPreferences.setLocations(locations);
    }
  }

  /**
   * Sets the selected location.
   * 
   * @param location
   *        Location to select
   */
  public void setSelectedLocation(final Location location)
  {
    locationsList.setSelectedValue(location, true);
  }

  /**
   * Sort the locations in the list.
   */
  public void sortLocations()
  {
    UserPreferences.sortLocations(locations);
    setLocations(locations);
  }

  private void createActions()
  {

    add = new DaylightChartGuiAction("Add", new ImageIcon(LocationsList.class
      .getResource("/icons/add_location.gif")), "Add a new location");
    delete = new DaylightChartGuiAction("Delete",
                                        new ImageIcon(LocationsList.class
                                          .getResource("/icons/delete_location.gif")),
                                        "Delete the selected location");
    edit = new DaylightChartGuiAction("Edit", new ImageIcon(LocationsList.class
      .getResource("/icons/edit_location.gif")), "Edit the selected  location");

    add.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        final LocationDialog ld = new LocationDialog(parent, null, "Add");
        ld.setVisible(true);
      }
    });

    delete.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        final Location location = getSelectedLocation();
        final LocationDialog locDialog = new LocationDialog(parent,
                                                            location,
                                                            "Delete");
        locDialog.setVisible(true);
        setSelectedLocation(location);
      }
    });

    edit.addActionListener(new ActionListener()
    {
      public void actionPerformed(final ActionEvent e)
      {
        final Location location = getSelectedLocation();
        final LocationDialog locDialog = new LocationDialog(parent,
                                                            location,
                                                            "Edit");
        locDialog.setVisible(true);
      }
    });

  }

  private JPopupMenu createPopupMenu()
  {
    final JPopupMenu rightPopup = new JPopupMenu();
    rightPopup.add(add);
    rightPopup.add(delete);
    rightPopup.add(edit);
    return rightPopup;
  }

}
