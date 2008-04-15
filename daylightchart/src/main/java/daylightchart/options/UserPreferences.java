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
package daylightchart.options;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

import com.thoughtworks.xstream.XStream;

import daylightchart.daylightchart.chart.DaylightChart;
import daylightchart.daylightchart.layout.DaylightChartReport;
import daylightchart.location.Location;
import daylightchart.location.parser.FormatterException;
import daylightchart.location.parser.LocationFormatter;
import daylightchart.location.parser.LocationParser;
import daylightchart.location.parser.ParserException;
import daylightchart.options.chart.ChartOptions;

/**
 * User preferences for the GUI.
 * 
 * @author Sualeh Fatehi
 */
public final class UserPreferences
{

  private static final Logger LOGGER = Logger.getLogger(UserPreferences.class
    .getName());

  private static final File scratchDirectory;
  private static File settingsDirectory;

  private static File locationsDataFile;
  private static File reportFile;
  private static File optionsFile;

  private static List<Location> locations;
  private static JasperReport report;
  private static Options options;

  static
  {
    scratchDirectory = new File(System.getProperty("java.io.tmpdir"), ".");
    validateDirectory(scratchDirectory);

    settingsDirectory = new File(System.getProperty("user.home", "."),
                                 ".daylightchart");
    setSettingsDirectory(settingsDirectory.getAbsolutePath());

    initializeLocations();
    initializeReport();
    initializeOptions();
  }

  /**
   * Clears all user preferences.
   */
  public static void clear()
  {
    if (optionsFile.exists())
    {
      optionsFile.delete();
    }
    initializeOptions();

    if (locationsDataFile.exists())
    {
      locationsDataFile.delete();
    }
    initializeLocations();

    if (reportFile.exists())
    {
      reportFile.delete();
    }
    initializeReport();

    if (settingsDirectory.exists())
    {
      settingsDirectory.delete();
    }
  }

  /**
   * Creates a chart options instance.
   * 
   * @return Chart options
   */
  public static Options getDefaultDaylightChartOptions()
  {
    final ChartOptions chartOptions = new ChartOptions();
    chartOptions.copyFromChart(new DaylightChart());

    final Options options = new Options();
    options.setChartOptions(chartOptions);

    return options;
  }

  /**
   * Gets the locations for the current user.
   * 
   * @return Locations
   */
  public static List<Location> getLocations()
  {
    return locations;
  }

  /**
   * Gets the options for the current user.
   * 
   * @return Options
   */
  public static Options getOptions()
  {
    return options;
  }

  /**
   * Gets the report for the current user.
   * 
   * @return Report
   */
  public static JasperReport getReport()
  {
    return report;
  }

  /**
   * @return the workingDirectory
   */
  public static File getScratchDirectory()
  {
    return scratchDirectory;
  }

  /**
   * @return the applicationSettingsDirectory
   */
  public static File getSettingsDirectory()
  {
    settingsDirectory.mkdirs();
    return settingsDirectory;
  }

  public static boolean importReport(final File reportFile)
  {
    boolean imported = false;
    final JasperReport report = loadReportFromFile(reportFile);
    if (report != null)
    {
      setReport(report);
      imported = true;
    }
    return imported;
  }

  public static List<Location> loadLocationsFromFile(final File file)
  {
    List<Location> locations = LocationsLoader.load(file);
    return locations;
  }

  public static Options loadOptionsFromFile(final File file)
  {
    final Reader reader = LocationsLoader.getFileReader(file);
    if (reader == null)
    {
      LOGGER.log(Level.WARNING, "Could not read options from " + file);
      locations = null;
    }
    Options options;
    try
    {
      final XStream xStream = new XStream();
      options = (Options) xStream.fromXML(reader);
    }
    catch (final Exception e)
    {
      LOGGER.log(Level.WARNING, "Could not read options from " + file, e);
      options = null;
    }
    finally
    {
      if (reader != null)
      {
        try
        {
          reader.close();
        }
        catch (final IOException e)
        {
          LOGGER.log(Level.WARNING, "Could not close " + file, e);
        }
      }
    }
    return options;
  }

  public static JasperReport loadReportFromFile(final File file)
  {
    if (file == null || !file.exists() || !file.canRead())
    {
      return null;
    }
    FileInputStream fileInputStream;
    try
    {
      fileInputStream = new FileInputStream(file);
      return compileReport(fileInputStream);
    }
    catch (final FileNotFoundException e)
    {
      LOGGER.log(Level.WARNING, "Could not read report from " + file, e);
      return null;
    }
  }

  /**
   * Main method. Lists all user preferences.
   * 
   * @param args
   *        Command line arguments
   * @throws Exception
   *         On an exception
   */
  public static void main(final String[] args)
    throws Exception
  {
    UserPreferences.clear();
  }

  public static void saveLocationsToFile(final File file)
  {
    try
    {
      if (file.exists())
      {
        file.delete();
      }
      final Writer writer = getFileWriter(file);
      if (writer == null)
      {
        LOGGER.log(Level.WARNING, "Could not save locations to " + file);
        return;
      }
      LocationFormatter.formatLocations(locations, writer);
    }
    catch (final FormatterException e)
    {
      LOGGER.log(Level.WARNING, "Could not save locations to " + file, e);
    }
  }

  public static void saveOptionsToFile(final File file)
  {
    try
    {
      final Writer writer = getFileWriter(file);
      if (writer == null)
      {
        LOGGER.log(Level.WARNING, "Could not save options to " + file);
        return;
      }
      final XStream xStream = new XStream();
      xStream.toXML(options, writer);
    }
    catch (final Exception e)
    {
      LOGGER.log(Level.WARNING, "Could save options to " + file, e);
    }
  }

  public static void saveReportToFile(final File file)
  {
    try
    {
      if (file.exists())
      {
        file.delete();
      }
      JRXmlWriter.writeReport(report, file.getAbsolutePath(), "UTF-8");
    }
    catch (final JRException e)
    {
      LOGGER.log(Level.WARNING, "Could not save report to " + file, e);
    }
  }

  /**
   * Sets the locations for the current user.
   * 
   * @param locations
   *        Locations
   */
  public static void setLocations(final List<Location> locations)
  {
    if (locations == null)
    {
      return;
    }
    UserPreferences.locations = locations;
    saveLocationsToFile(locationsDataFile);
  }

  /**
   * Sets the options for the current user.
   * 
   * @param options
   *        Options
   */
  public static void setOptions(final Options options)
  {
    if (options == null)
    {
      return;
    }
    UserPreferences.options = options;
    saveOptionsToFile(optionsFile);
  }

  /**
   * Sets the report for the current user.
   * 
   * @param locations
   *        Locations
   */
  public static void setReport(final JasperReport report)
  {
    if (report == null)
    {
      return;
    }
    UserPreferences.report = report;
    saveReportToFile(reportFile);
  }

  public static void setSettingsDirectory(final String settingsDirectoryPath)
  {
    if (settingsDirectoryPath == null
        || settingsDirectoryPath.trim().length() == 0)
    {
      return;
    }
    final File settingsDirectory = new File(settingsDirectoryPath);

    settingsDirectory.mkdirs();
    validateDirectory(settingsDirectory);

    UserPreferences.settingsDirectory = settingsDirectory;
    locationsDataFile = new File(settingsDirectory, "locations.data");
    reportFile = new File(settingsDirectory, "DaylightChartReport.jrxml");
    optionsFile = new File(settingsDirectory, "options.xml");
  }

  public static void setSlimUi(final boolean slimUi)
  {
    options.setSlimUi(slimUi);
    setOptions(options);
  }

  public static void setWorkingDirectory(final File workingDirectory)
  {
    options.setWorkingDirectory(workingDirectory);
    setOptions(options);
  }

  private static JasperReport compileReport(final InputStream reportStream)
  {
    try
    {
      if (reportStream == null)
      {
        return null;
      }
      final JasperDesign jasperDesign = JRXmlLoader.load(reportStream);
      final JasperReport jasperReport = JasperCompileManager
        .compileReport(jasperDesign);
      return jasperReport;
    }
    catch (final JRException e)
    {
      LOGGER.log(Level.WARNING, "Cannot load JasperReport", e);
      return null;
    }
    finally
    {
      if (reportStream != null)
      {
        try
        {
          reportStream.close();
        }
        catch (final IOException e)
        {
          LOGGER.log(Level.WARNING, "Cannot close input stream", e);
        }
      }
    }
  }

  private static Writer getFileWriter(final File file)
  {
    try
    {
      final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),
                                                                              "UTF-8"));
      return writer;
    }
    catch (final UnsupportedEncodingException e)
    {
      LOGGER.log(Level.WARNING, "Cannot write file " + file);
      return null;
    }
    catch (final FileNotFoundException e)
    {
      LOGGER.log(Level.WARNING, "Cannot write file " + file);
      return null;
    }
  }

  private static void initializeLocations()
  {
    // Try loading from user preferences
    locations = loadLocationsFromFile(locationsDataFile);

    // Load from internal store
    if (locations == null)
    {
      final InputStream dataStream = Thread.currentThread()
        .getContextClassLoader().getResourceAsStream("locations.data");
      if (dataStream == null)
      {
        throw new IllegalStateException("Cannot read data from internal database");
      }
      final Reader reader = new InputStreamReader(dataStream);
      try
      {
        locations = LocationParser.parseLocations(reader);
      }
      catch (final ParserException e)
      {
        throw new RuntimeException("Cannot read data from internal database", e);
      }
    }
  }

  private static void initializeOptions()
  {
    // Try loading from user preferences
    options = loadOptionsFromFile(optionsFile);

    // Load from internal store
    if (options == null)
    {
      options = getDefaultDaylightChartOptions();
    }
  }

  private static void initializeReport()
  {
    // Try loading from user preferences
    report = loadReportFromFile(reportFile);

    // Load from internal store
    if (report == null)
    {
      final InputStream reportStream = DaylightChartReport.class
        .getResourceAsStream("/DaylightChartReport.jrxml");
      report = compileReport(reportStream);
      if (report == null)
      {
        throw new RuntimeException("Cannot load default report");
      }
    }
  }

  /**
   * @param directory
   *        the workingDirectory to set
   */
  private static void validateDirectory(final File directory)
  {
    final boolean isDirectoryValid = directory != null && directory.exists()
                                     && directory.isDirectory()
                                     && directory.canWrite();
    if (!isDirectoryValid)
    {
      throw new IllegalArgumentException("Directory is not writable - "
                                         + directory);
    }
  }

  private UserPreferences()
  {
    // Prevent external instantiation
  }
}
