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
package daylightchart;


import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;

import sf.util.CommandLineParser;
import sf.util.CommandLineUtility;
import sf.util.CommandLineParser.BooleanOption;
import sf.util.CommandLineParser.Option;

import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import com.jgoodies.looks.plastic.theme.LightGray;

import daylightchart.gui.DaylightChartGui;
import daylightchart.options.UserPreferences;

/**
 * Main window.
 * 
 * @author Sualeh Fatehi
 */
public final class Main
{

  private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

  private static final String OPTION_NO_PREFERENCES = "noprefs";

  /**
   * Main window.
   * 
   * @param args
   *        Arguments
   */
  public static void main(final String[] args)
  {

    CommandLineUtility.setLogLevel(args);

    // Parse command line
    final CommandLineParser parser = new CommandLineParser();
    parser.addOption(new BooleanOption(Option.NO_SHORT_FORM,
                                       OPTION_NO_PREFERENCES));
    parser.parse(args);
    final boolean noPreferences = parser
      .getBooleanOptionValue(OPTION_NO_PREFERENCES);

    UserPreferences.setSavePreferences(!noPreferences);

    // Set UI look and feel
    try
    {
      PlasticLookAndFeel.setPlasticTheme(new LightGray());
      UIManager.setLookAndFeel(new PlasticLookAndFeel());
    }
    catch (final Exception e)
    {
      LOGGER.log(Level.WARNING, "Cannot set look and feel");
    }

    new DaylightChartGui().setVisible(true);
  }

  private Main()
  {
    // No-op
  }

}
