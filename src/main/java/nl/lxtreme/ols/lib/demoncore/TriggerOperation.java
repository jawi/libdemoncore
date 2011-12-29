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
 * Denotes a trigger sum operation.
 */
public enum TriggerOperation
{
  // CONSTANTS

  /** No-operation (= always 0). */
  NOP( 0, DemonCore.rNOP, false /* inverted */),
  /** Any operation (= always 1). */
  ANY( 1, DemonCore.rANY, false /* inverted */),
  /** Logical AND. */
  AND( 2, DemonCore.rAND, false /* inverted */),
  /** Logical NAND. */
  NAND( 3, DemonCore.rNAND, true /* inverted */),
  /** Logical OR. */
  OR( 4, DemonCore.rOR, false /* inverted */),
  /** Logical NOR. */
  NOR( 5, DemonCore.rNOR, true /* inverted */),
  /** Logical XOR. */
  XOR( 6, DemonCore.rXOR, false /* inverted */),
  /** Logical NXOR. */
  NXOR( 7, DemonCore.rXNOR, true /* inverted */),
  /** A only */
  A_ONLY( 8, DemonCore.rA_ONLY, false /* inverted */),
  /** B only */
  B_ONLY( 9, DemonCore.rB_ONLY, false /* inverted */);

  // VARIABLES

  private final int offset;
  private final String resourceKey;
  private final boolean inverted;

  // CONSTRUCTORS

  /**
   * Creates a new {@link TriggerOperation} instance.
   * 
   * @param aOffset
   *          the offset of this {@link TriggerOperation}.
   */
  private TriggerOperation( final int aOffset, final String aResourceKey, final boolean aInverted )
  {
    this.offset = aOffset;
    this.resourceKey = aResourceKey;
    this.inverted = aInverted;
  }

  // METHODS

  /**
   * Returns the logical offset of this operation.
   * 
   * @return the offset, >= 0.
   */
  public int getOffset()
  {
    return this.offset;
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

  /**
   * Returns the current value of inverted.
   * 
   * @return the inverted
   */
  public boolean isInverted()
  {
    return this.inverted;
  }
}
