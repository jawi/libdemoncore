/*
 * OpenBench LogicSniffer / SUMP project 
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or (at
 * your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin St, Fifth Floor, Boston, MA 02110, USA
 *
 * Copyright (C) 2006-2010 Michael Poppitz, www.sump.org
 * Copyright (C) 2010 J.W. Janssen, www.lxtreme.nl
 */
package nl.lxtreme.ols.lib.demoncore;


import java.lang.reflect.*;
import java.util.*;


/**
 * Provides some utility methods.
 */
final class Utils
{
  // CONSTRUCTORS

  /**
   * Creates a new Utils instance, not used.
   */
  private Utils()
  {
    // NO-op
  }

  // METHODS

  /**
   * @param aInput
   * @return
   */
  @SuppressWarnings( "unchecked" )
  public static final <T> T[] deepCopy( final T[] aInput )
  {
    final Class<?> compType = ( ( Class<? extends T[]> )aInput.getClass() ).getComponentType();
    if ( compType.isPrimitive() )
    {
      // Faster: simply create new array with the original primitives...
      return Arrays.copyOf( aInput, aInput.length );
    }
    // Deep copy the individual terms as well...
    final T[] result = ( T[] )Array.newInstance( compType, aInput.length );

    try
    {
      Constructor<?> constructor = null;
      for ( int i = 0; i < aInput.length; i++ )
      {
        final T origElement = aInput[i];
        final Class<? extends Object> elemType = origElement.getClass();

        try
        {
          constructor = elemType.getDeclaredConstructor( elemType );
          constructor.setAccessible( true );

          result[i] = ( T )constructor.newInstance( origElement );
        }
        catch ( Exception exception )
        {
          // No copy constructor; use original element instead...
          result[i] = origElement;
        }
      }
    }
    catch ( Exception exception )
    {
      throw new RuntimeException( "Failed to deep copy array of " + compType, exception );
    }

    return result;
  }
}
