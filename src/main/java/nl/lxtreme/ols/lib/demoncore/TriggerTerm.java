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
 * Denotes one of the ten trigger terms.
 */
public enum TriggerTerm
{
  // CONSTANTS

  /** The 1st term. */
  TERM_A( 0x20 ), //
  /** The 2nd term. */
  TERM_B( 0x21 ), //
  /** The 3rd term. */
  TERM_C( 0x22 ), //
  /** The 4th term. */
  TERM_D( 0x23 ), //
  /** The 5th term. */
  TERM_E( 0x24 ), //
  /** The 6th term. */
  TERM_F( 0x25 ), //
  /** The 7th term. */
  TERM_G( 0x26 ), //
  /** The 8th term. */
  TERM_H( 0x27 ), //
  /** The 9th term. */
  TERM_I( 0x28 ), //
  /** The 10th term. */
  TERM_J( 0x29 );

  // VARIABLES

  private final int lutChainAddress;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerTerm instance.
   * 
   * @param aLutChainAddress
   *          the LUT chain address to use.
   */
  private TriggerTerm( final int aLutChainAddress )
  {
    this.lutChainAddress = aLutChainAddress;
  }

  // METHODS

  /**
   * Returns the current value of lutChainAddress.
   * 
   * @return the LUT chain address, never <code>null</code>.
   */
  public int getLutChainAddress()
  {
    return this.lutChainAddress;
  }
}
