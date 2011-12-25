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


import java.io.*;


/**
 * Denotes a trigger sum that combines several inputs to a logical trigger sum.
 */
public final class TriggerSum implements ITriggerVisitable
{
  // VARIABLES

  private final TriggerStateTerm stateTerm;
  private int stateNumber;
  private TriggerTerms triggerTerms;

  // CONSTRUCTORS

  /**
   * Creates a new {@link TriggerSum} instance.
   * 
   * @param aStateTerm
   *          the trigger state term this sum represents.
   */
  TriggerSum( final TriggerStateTerm aStateTerm )
  {
    this.stateTerm = aStateTerm;

    reset();
  }

  /**
   * Creates a new {@link TriggerSum} instance that is an exact copy of the
   * given trigger sum.
   * 
   * @param aSum
   *          the trigger sum to copy, cannot be <code>null</code>.
   */
  TriggerSum( final TriggerSum aSum )
  {
    this.stateTerm = aSum.stateTerm;
    this.stateNumber = aSum.stateNumber;
    this.triggerTerms = aSum.triggerTerms;
  }

  // METHODS

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept( final ITriggerVisitor aVisitor ) throws IOException
  {
    aVisitor.selectChain( 0x40 + ( this.stateNumber * 4 ) + this.stateTerm.getOffset() );

    this.triggerTerms.accept( aVisitor );
  }

  /**
   * Returns the current value of stateNumber.
   * 
   * @return the stateNumber
   */
  public int getStateNumber()
  {
    return this.stateNumber;
  }

  /**
   * Returns the current value of stateTerm.
   * 
   * @return the stateTerm
   */
  public TriggerStateTerm getStateTerm()
  {
    return this.stateTerm;
  }

  /**
   * Resets this trigger sum to its initial state.
   */
  void reset()
  {
    this.stateNumber = 0;
    this.triggerTerms.reset();
  }

  /**
   * Sets stateNumber to the given value.
   * 
   * @param aStateNumber
   *          the stateNumber to set.
   */
  void setStateNumber( final int aStateNumber )
  {
    if ( ( aStateNumber < 0 ) || ( aStateNumber > 15 ) )
    {
      throw new IllegalArgumentException( "Invalid state number!" );
    }

    this.stateNumber = aStateNumber;
  }

  /**
   * Sets triggerTerms to the given value.
   * 
   * @param aTriggerTerms
   *          the triggerTerms to set.
   */
  void setTriggerTerms( final TriggerTerms aTriggerTerms )
  {
    if ( aTriggerTerms == null )
    {
      throw new IllegalArgumentException( "Invalid trigger terms" );
    }

    this.triggerTerms = aTriggerTerms;
  }
}
