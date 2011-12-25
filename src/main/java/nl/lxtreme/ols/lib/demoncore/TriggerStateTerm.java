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
 * Denotes a trigger state term, which is either a hit-term, else-term or a
 * capture-term.
 */
public enum TriggerStateTerm
{
  // CONSTANTS

  /** Denotes the if-clause. */
  HIT( 0 ), //
  /** Denotes the else-clause. */
  ELSE( 1 ), //
  /** Denotes the captured term. */
  CAPTURE( 2 ); //

  // VARIABLES

  private final int offset;

  // CONSTRUCTORS

  /**
   * Creates a new {@link TriggerStateTerm} instance.
   * 
   * @param aOffset
   *          the logical offset of this term.
   */
  private TriggerStateTerm( final int aOffset )
  {
    this.offset = aOffset;
  }

  // METHODS

  /**
   * Returns the offset of this state term.
   * 
   * @return the offset, >= 0.
   */
  public int getOffset()
  {
    return this.offset;
  }
}
