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
 * Provides an edge detector term.
 */
public class TriggerEdgeDetector extends AbstractTriggerTerm
{
  // VARIABLES

  private int risingEdgeMask;
  private int fallingEdgeMask;
  private int noEdgeMask;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerEdgeDetector instance.
   * 
   * @param aTerm
   */
  public TriggerEdgeDetector( final TriggerEdgeDetector aTerm )
  {
    super( aTerm );
    this.risingEdgeMask = aTerm.risingEdgeMask;
    this.fallingEdgeMask = aTerm.fallingEdgeMask;
    this.noEdgeMask = aTerm.noEdgeMask;
  }

  /**
   * Creates a new TriggerEdgeDetector instance.
   * 
   * @param aType
   */
  public TriggerEdgeDetector( final TriggerTermType aType )
  {
    super( aType );
  }

  // METHODS

  /**
   * Returns the current value of fallingEdgeMask.
   * 
   * @return the fallingEdgeMask
   */
  public int getFallingEdgeMask()
  {
    return this.fallingEdgeMask;
  }

  /**
   * Returns the current value of noEdgeMask.
   * 
   * @return the noEdgeMask
   */
  public int getNoEdgeMask()
  {
    return this.noEdgeMask;
  }

  /**
   * Returns the current value of risingEdgeMask.
   * 
   * @return the risingEdgeMask
   */
  public int getRisingEdgeMask()
  {
    return this.risingEdgeMask;
  }

  /**
   * Sets fallingEdgeMask to the given value.
   * 
   * @param aFallingEdgeMask
   *          the fallingEdgeMask to set.
   */
  public void setFallingEdgeMask( final int aFallingEdgeMask )
  {
    this.fallingEdgeMask = aFallingEdgeMask;
  }

  /**
   * Sets noEdgeMask to the given value.
   * 
   * @param aNoEdgeMask
   *          the noEdgeMask to set.
   */
  public void setNoEdgeMask( final int aNoEdgeMask )
  {
    this.noEdgeMask = aNoEdgeMask;
  }

  /**
   * Sets risingEdgeMask to the given value.
   * 
   * @param aRisingEdgeMask
   *          the risingEdgeMask to set.
   */
  public void setRisingEdgeMask( final int aRisingEdgeMask )
  {
    this.risingEdgeMask = aRisingEdgeMask;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void reset()
  {
    super.reset();
    this.fallingEdgeMask = 0;
    this.noEdgeMask = 0;
    this.risingEdgeMask = 0;
  }
}
