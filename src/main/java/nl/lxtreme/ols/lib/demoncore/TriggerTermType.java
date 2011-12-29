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


import nl.lxtreme.ols.lib.demoncore.ui.*;


/**
 * Denotes one of the trigger terms.
 */
public enum TriggerTermType
{
  // CONSTANTS

  /** The 1st term. */
  TERM_A( 0x20, DemonCore.rTERM_A ), //
  /** The 2nd term. */
  TERM_B( 0x21, DemonCore.rTERM_B ), //
  /** The 3rd term. */
  TERM_C( 0x22, DemonCore.rTERM_C ), //
  /** The 4th term. */
  TERM_D( 0x23, DemonCore.rTERM_D ), //
  /** The 5th term. */
  TERM_E( 0x24, DemonCore.rTERM_E ), //
  /** The 6th term. */
  TERM_F( 0x25, DemonCore.rTERM_F ), //
  /** The 7th term. */
  TERM_G( 0x26, DemonCore.rTERM_G ), //
  /** The 8th term. */
  TERM_H( 0x27, DemonCore.rTERM_H ), //
  /** The 9th term. */
  TERM_I( 0x28, DemonCore.rTERM_I ), //
  /** The 10th term. */
  TERM_J( 0x29, DemonCore.rTERM_J ),
  /** The range 1 term. */
  TERM_RANGE1( 0x30, DemonCore.rIN_RANGE1 ),
  /** The range 2 term. */
  TERM_RANGE2( 0x32, DemonCore.rIN_RANGE2 ),
  /** The edge 1 term. */
  TERM_EDGE1( 0x34, DemonCore.rEDGE1 ),
  /** The edge 2 term. */
  TERM_EDGE2( 0x35, DemonCore.rEDGE2 ),
  /** The timer 1 term. */
  TERM_TIMER1( 0x38, DemonCore.rTIMER1 ),
  /** The timer 2 term. */
  TERM_TIMER2( 0x3A, DemonCore.rTIMER2 );

  // VARIABLES

  private final int lutChainAddress;
  private final String resourceKey;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerTerm instance.
   * 
   * @param aLutChainAddress
   *          the LUT chain address to use.
   */
  private TriggerTermType( final int aLutChainAddress, final String aResourceKey )
  {
    this.lutChainAddress = aLutChainAddress;
    this.resourceKey = aResourceKey;
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

  /**
   * Returns the current value of resourceKey.
   * 
   * @return the resourceKey
   */
  public String getResourceKey()
  {
    return this.resourceKey;
  }
}
