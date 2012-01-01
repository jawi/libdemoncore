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


import static nl.lxtreme.ols.lib.demoncore.TriggerTermType.*;

import java.io.*;


/**
 * Denotes a trigger sum that combines several inputs to a logical trigger sum.
 */
public final class TriggerSum implements ITriggerVisitable
{
  // CONSTANTS

  /** The order in which the terms are inputted. */
  private static final TriggerTermType[] TERMS = { TERM_A, TERM_B, TERM_C, TERM_RANGE1, TERM_D, TERM_EDGE1, TERM_E,
      TERM_TIMER1, TERM_F, TERM_G, TERM_H, TERM_RANGE2, TERM_I, TERM_EDGE2, TERM_J, TERM_TIMER2 };

  // VARIABLES

  private int stateNumber;
  private final TriggerStateTerm stateTerm;
  private final TriggerFinalTerm finalTerm;

  // CONSTRUCTORS

  /**
   * Creates a new {@link TriggerSum} instance.
   * 
   * @param aStateTerm
   *          the trigger state term this sum represents.
   */
  public TriggerSum( final TriggerStateTerm aStateTerm )
  {
    this.stateTerm = aStateTerm;

    // The input terms...
    AbstractTriggerTerm[] inputTerms = new AbstractTriggerTerm[TERMS.length];
    for ( int i = 0; i < inputTerms.length; i++ )
    {
      inputTerms[i] = AbstractTriggerTerm.create( TERMS[i] );
    }

    // Create the terms structure...
    TriggerPairTerm[] pairTerms = new TriggerPairTerm[TERMS.length / 2];
    for ( int i = 0, j = 0; i < TERMS.length; i += 2, j++ )
    {
      final AbstractTriggerTerm termA = inputTerms[i];
      final AbstractTriggerTerm termB = inputTerms[i + 1];
      pairTerms[j] = new TriggerPairTerm( termA, termB );
    }

    TriggerMidTerm[] midTerms = new TriggerMidTerm[2];
    for ( int i = 0, j = 0; i < ( TERMS.length / 2 ); i += 4, j++ )
    {
      midTerms[j] = new TriggerMidTerm( pairTerms[i], pairTerms[i + 1], pairTerms[i + 2], pairTerms[i + 3] );
    }

    this.finalTerm = new TriggerFinalTerm( midTerms[0], midTerms[1] );

    reset();
  }

  /**
   * Creates a new {@link TriggerSum} instance that is an exact copy of the
   * given trigger sum.
   * 
   * @param aSum
   *          the trigger sum to copy, cannot be <code>null</code>.
   */
  public TriggerSum( final TriggerSum aSum )
  {
    this.stateTerm = aSum.stateTerm;
    this.stateNumber = aSum.stateNumber;

    // Recursively copies all other (mid/pair/input) terms as well!
    this.finalTerm = new TriggerFinalTerm( aSum.finalTerm );
  }

  // METHODS

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept( final ITriggerVisitor aVisitor ) throws IOException
  {
    this.finalTerm.accept( aVisitor );

    aVisitor.visit( this );
  }

  /**
   * Returns the current value of finalTerm.
   * 
   * @return the finalTerm
   */
  public TriggerFinalTerm getFinalTerm()
  {
    return this.finalTerm;
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
   * @return
   */
  final int getOffset()
  {
    return ( 0x40 + ( this.stateNumber * 4 ) + this.stateTerm.getOffset() );
  }

  /**
   * Resets this trigger sum to its initial state.
   */
  void reset()
  {
    this.stateNumber = 0;
    this.finalTerm.reset();
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
}
