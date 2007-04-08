/*
 * Copyright (c) 2004-2007 Sualeh Fatehi. All Rights Reserved.
 */
package daylightchart.location;


import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import daylightchart.location.parser.LocationLexer;
import daylightchart.location.parser.LocationParser;

/**
 * Parses objects from strings.
 * 
 * @author Sualeh Fatehi
 */
public class $Parser
{

  /**
   * Parses a string representation of the location.
   * 
   * @param representation
   *        String representation of the location
   * @return Location
   * @throws ParserException
   */
  public final static Location parseLocation(final String representation)
    throws ParserException
  {
    final LocationParser parser = constructLocationParser(representation);
    Location location;
    try
    {
      location = parser.location();
    }
    catch (final RecognitionException e)
    {
      throw new ParserException(e);
    }
    catch (final TokenStreamException e)
    {
      throw new ParserException(e);
    }
    return location;
  }

  /**
   * Reads locations from a reader.
   * 
   * @param reader
   *        Reader
   * @return List of locations
   * @throws ParserException
   */
  public static List<Location> parseLocations(final Reader reader)
    throws ParserException
  {
    final LocationParser parser = constructLocationParser(reader);
    List<Location> locations;
    try
    {
      locations = parser.locations();
    }
    catch (final RecognitionException e)
    {
      throw new ParserException(e);
    }
    catch (final TokenStreamException e)
    {
      throw new ParserException(e);
    }
    return locations;
  }

  private static LocationParser constructLocationParser(final Reader reader)
  {
    final LocationLexer lexer = new LocationLexer(reader);
    final LocationParser parser = new LocationParser(lexer);
    return parser;
  }

  private static LocationParser constructLocationParser(final String text)
  {
    final StringReader reader = new StringReader(text + "\n");
    return constructLocationParser(reader);
  }

  private $Parser()
  {
  }

}
