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
 * Denotes a concrete trigger term, with a bitmask and trigger value.
 */
public class TriggerTerm extends AbstractTriggerTerm
{
  // VARIABLES

  private int value;
  private int mask;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerTerm instance as exact copy of the given term.
   * 
   * @param aTerm
   *          the trigger term to copy, cannot be <code>null</code>.
   */
  public TriggerTerm( final TriggerTerm aTerm )
  {
    super( aTerm );
    this.value = aTerm.value;
    this.mask = aTerm.mask;
  }

  /**
   * Creates a new TriggerTerm instance.
   * 
   * @param aType
   *          the trigger term type.
   */
  public TriggerTerm( final TriggerTermType aType )
  {
    super( aType );
    reset();
  }

  // METHODS

  /**
   * Returns the current value of mask.
   * 
   * @return the mask
   */
  public int getMask()
  {
    return this.mask;
  }

  /**
   * Returns the current value of value.
   * 
   * @return the value
   */
  public int getValue()
  {
    return this.value;
  }

  /**
   * Sets mask to the given value.
   * 
   * @param aMask
   *          the mask to set.
   */
  public void setMask( final int aMask )
  {
    this.mask = aMask;
  }

  /**
   * Sets value to the given value.
   * 
   * @param aValue
   *          the value to set.
   */
  public void setValue( final int aValue )
  {
    this.value = aValue;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void reset()
  {
    super.reset();
    this.mask = 0;
    this.value = 0;
  }
}
