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

/**
 * Denotes one of the two range-triggers.
 */
public enum TriggerRange
{
  /** The first range-trigger, lower boundary. */
  RANGE_1_LOWER( 0x30 ), //
  /** The first edge-trigger, upper boundary. */
  RANGE_1_UPPER( 0x31 ), //
  /** The second range-trigger, lower boundary. */
  RANGE_2_LOWER( 0x32 ), //
  /** The second edge-trigger, upper boundary. */
  RANGE_2_UPPER( 0x33 );

  private final int lutChainAddress;

  /**
   * Creates a new TriggerRange instance.
   * 
   * @param aLutChainAddress
   *          the LUT chain address to use.
   */
  private TriggerRange( final int aLutChainAddress )
  {
    this.lutChainAddress = aLutChainAddress;
  }

  /**
   * Returns the current value of lutChainAddress.
   * 
   * @return the LUT chain address, never <code>null</code>.
   */
  public int getLutChainAddress()
  {
    return this.lutChainAddress;
  }

  /**
   * Returns whether or not this range represents the lower boundary.
   * 
   * @return <code>true</code> if this range is the lower boundary,
   *         <code>false</code> otherwise.
   */
  public boolean isLowerRange()
  {
    return ( this == RANGE_1_LOWER ) || ( this == RANGE_2_LOWER );
  }
}