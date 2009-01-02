package daylightchart.options;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.geoname.data.Location;
import org.geoname.parser.GNISFilesParser;
import org.geoname.parser.GNSCountryFilesParser;
import org.geoname.parser.LocationFormatter;
import org.geoname.parser.LocationParser;
import org.geoname.parser.UnicodeReader;

import daylightchart.gui.actions.LocationFileType;

/**
 * Represents a location file, with data.
 * 
 * @author sfatehi
 */
public abstract class BaseLocationsDataFile
  extends BaseDataFile<LocationFileType, List<Location>>
{

  private static final Logger LOGGER = Logger
    .getLogger(BaseLocationsDataFile.class.getName());

  /**
   * Constructor.
   * 
   * @param locationDataFile
   *        File
   */
  public BaseLocationsDataFile(final BaseTypedFile<LocationFileType> locationDataFile)
  {
    this(locationDataFile.getFile(), locationDataFile.getFileType());
  }

  /**
   * Constructor.
   * 
   * @param file
   *        File
   * @param fileType
   *        Location file type
   */
  public BaseLocationsDataFile(final File file, final LocationFileType fileType)
  {
    super(file, fileType);
  }

  /**
   * Loads a list of locations from a file of a given format.
   */
  public void load()
  {
    if (!exists())
    {
      LOGGER.log(Level.WARNING, "No locations file provided");
      data = null;
      return;
    }

    final List<InputStream> inputs = new ArrayList<InputStream>();
    final File file = getFile();
    try
    {
      switch (getFileType())
      {
        case data:
        case gns_country_file:
        case gnis_state_file:
          inputs.add(new FileInputStream(file));
          break;
        case gns_country_file_zipped:
        case gnis_state_file_zipped:
          final ZipFile zipFile = new ZipFile(file);
          final List<ZipEntry> zippedFiles = (List<ZipEntry>) Collections
            .list(zipFile.entries());
          for (final ZipEntry zipEntry: zippedFiles)
          {
            inputs.add(zipFile.getInputStream(zipEntry));
          }
          break;
      }
    }
    catch (final Exception e)
    {
      LOGGER.log(Level.WARNING, "Could not read locations from " + file, e);
      data = null;
    }

    load(inputs.toArray(new InputStream[inputs.size()]));
  }

  protected final void load(final InputStream... inputs)
  {
    data = new ArrayList<Location>();
    try
    {
      for (final InputStream inputStream: inputs)
      {
        final Reader reader = new UnicodeReader(inputStream, "UTF-8");
        switch (getFileType())
        {
          case data:
            data.addAll(LocationParser.parseLocations(reader));
            break;
          case gns_country_file:
          case gns_country_file_zipped:
            data.addAll(GNSCountryFilesParser.parseLocations(reader));
            break;
          case gnis_state_file:
          case gnis_state_file_zipped:
            data.addAll(GNISFilesParser.parseLocations(reader));
            break;
        }
        reader.close();
      }
    }
    catch (final Exception e)
    {
      LOGGER.log(Level.WARNING, "Could not read locations", e);
      data = null;
    }
    finally
    {
      for (final InputStream inputStream: inputs)
      {
        if (inputStream != null)
        {
          try
          {
            inputStream.close();
          }
          catch (final IOException e)
          {
            LOGGER.log(Level.WARNING, "Could not close input stream", e);
          }
        }
      }
    }

    if (data.size() == 0)
    {
      data = null;
    }
  }

  /**
   * Saves locations to a file.
   */
  public void save()
  {
    final File file = getFile();
    if (file == null)
    {
      LOGGER.log(Level.WARNING, "No locations file provided");
      return;
    }

    try
    {
      file.delete();

      final Writer writer = getFileWriter(file);
      if (writer == null)
      {
        return;
      }

      LocationFormatter.formatLocations(data, writer);
    }
    catch (final Exception e)
    {
      LOGGER.log(Level.WARNING, "Could not save locations to " + file, e);
    }
  }

}