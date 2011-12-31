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
 * Denotes a range detector.
 */
public class TriggerRangeDetector extends AbstractTriggerTerm
{
  // VARIABLES

  private int mask;
  private int lowerTarget;
  private int upperTarget;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerRangeDetector instance.
   * 
   * @param aTerm
   */
  public TriggerRangeDetector( final TriggerRangeDetector aTerm )
  {
    super( aTerm );
    this.mask = aTerm.mask;
    this.lowerTarget = aTerm.lowerTarget;
    this.upperTarget = aTerm.upperTarget;
  }

  /**
   * Creates a new TriggerRangeDetector instance.
   * 
   * @param aType
   */
  public TriggerRangeDetector( final TriggerTermType aType )
  {
    super( aType );
  }

  // METHODS

  /**
   * Returns the current value of lowerTarget.
   * 
   * @return the lowerTarget
   */
  public int getLowerTarget()
  {
    return this.lowerTarget;
  }

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
   * Returns the current value of upperTarget.
   * 
   * @return the upperTarget
   */
  public int getUpperTarget()
  {
    return this.upperTarget;
  }

  /**
   * Sets lowerTarget to the given value.
   * 
   * @param aLowerTarget
   *          the lowerTarget to set.
   */
  public void setLowerTarget( final int aLowerTarget )
  {
    this.lowerTarget = aLowerTarget;
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
   * Sets upperTarget to the given value.
   * 
   * @param aUpperTarget
   *          the upperTarget to set.
   */
  public void setUpperTarget( final int aUpperTarget )
  {
    this.upperTarget = aUpperTarget;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void reset()
  {
    super.reset();
    this.lowerTarget = 0;
    this.upperTarget = 0;
    this.mask = 0;
  }
}
