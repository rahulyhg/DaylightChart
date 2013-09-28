/* 
 * 
 * Daylight Chart
 * http://sourceforge.net/projects/daylightchart
 * Copyright (c) 2007-2013, Sualeh Fatehi.
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a data file, with data.
 * 
 * @author sfatehi
 * @param <T>
 *        File type
 * @param <D>
 *        Data object type
 */
abstract class BaseDataFile<T extends FileType, D>
  extends BaseTypedFile<T>
{

  private static final Logger LOGGER = Logger.getLogger(BaseDataFile.class
    .getName());

  protected D data;

  /**
   * Constructor.
   * 
   * @param typedFile
   *        File
   */
  public BaseDataFile(final BaseTypedFile<T> typedFile)
  {
    this(typedFile.getFile(), typedFile.getFileType());
  }

  /**
   * Constructor.
   * 
   * @param file
   *        File
   * @param fileType
   *        File type
   */
  public BaseDataFile(final File file, final T fileType)
  {
    super(file, fileType);

    // Validation
    if (file == null)
    {
      throw new IllegalArgumentException("No file provided");
    }
    if (fileType == null)
    {
      throw new IllegalArgumentException("No file type provided");
    }
  }

  /**
   * Constructor.
   * 
   * @param settingsDirectory
   *        Settings directory
   * @param resource
   *        Resource
   * @param fileType
   *        File type
   */
  BaseDataFile(final File settingsDirectory,
               final String resource,
               final T fileType)
  {
    this(new File(settingsDirectory, resource), fileType);
    // Validation
    if (!settingsDirectory.isDirectory() || !settingsDirectory.exists())
    {
      throw new IllegalArgumentException("Settings directory is not a directory");
    }
    // Load
    loadWithFallback();
  }

  /**
   * Gets data.
   * 
   * @return Data
   */
  public final D getData()
  {
    return data;
  }

  /**
   * Sets data, and saves it.
   * 
   * @param data
   *        Data
   */
  public final void save(D data)
  {
    if (data != null)
    {
      this.data = data;
      save();
    }
  }

  protected final Writer getFileWriter(final File file)
  {
    try
    {
      final BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file),
                                                                              "UTF-8"));
      return writer;
    }
    catch (final UnsupportedEncodingException e)
    {
      LOGGER.log(Level.WARNING, "Cannot write file " + file, e);
      return null;
    }
    catch (final FileNotFoundException e)
    {
      LOGGER.log(Level.WARNING, "Cannot write file " + file, e);
      return null;
    }
  }

  /**
   * Loads data from the file.
   */
  protected abstract void load();

  protected abstract void load(final InputStream... inputs);

  /**
   * Loads data from the file, falling back to a default that is usually
   * loaded from an internal resource.
   */
  protected abstract void loadWithFallback();

  /**
   * Saves data to a file.
   */
  protected abstract void save();

}
