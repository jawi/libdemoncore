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
import java.util.*;


/**
 * Denotes a trigger sum that combines several inputs to a logical trigger sum.
 */
public final class TriggerSum implements ITriggerVisitable
{
  // CONSTANTS

  /** The order in which the terms are inputted. */
  private static final TriggerTermType[] TERMS = { TriggerTermType.TERM_A, TriggerTermType.TERM_B,
      TriggerTermType.TERM_C, TriggerTermType.TERM_RANGE1, TriggerTermType.TERM_D, TriggerTermType.TERM_EDGE1,
      TriggerTermType.TERM_E, TriggerTermType.TERM_TIMER1, TriggerTermType.TERM_F, TriggerTermType.TERM_G,
      TriggerTermType.TERM_H, TriggerTermType.TERM_RANGE2, TriggerTermType.TERM_I, TriggerTermType.TERM_EDGE2,
      TriggerTermType.TERM_J, TriggerTermType.TERM_TIMER2 };

  // VARIABLES

  private int stateNumber;
  private final TriggerStateTerm stateTerm;
  private final TriggerInputTerm[] inputTerms;
  private final TriggerMidTerm[] midTerms;
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

    // Create the terms structure...
    this.inputTerms = new TriggerInputTerm[TERMS.length / 2];
    for ( int i = 0, j = 0; i < TERMS.length; i += 2, j++ )
    {
      final AbstractTriggerTerm termA = AbstractTriggerTerm.create( TERMS[i] );
      final AbstractTriggerTerm termB = AbstractTriggerTerm.create( TERMS[i + 1] );
      this.inputTerms[j] = new TriggerInputTerm( termA, termB );
    }

    this.midTerms = new TriggerMidTerm[2];
    for ( int i = 0, j = 0; i < ( TERMS.length / 2 ); i += 4, j++ )
    {
      this.midTerms[j] = new TriggerMidTerm( this.inputTerms[i], this.inputTerms[i + 1], this.inputTerms[i + 2],
          this.inputTerms[i + 3] );
    }

    this.finalTerm = new TriggerFinalTerm( this.midTerms[0], this.midTerms[1] );

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
    this.inputTerms = Arrays.copyOf( aSum.inputTerms, aSum.inputTerms.length ); // XXX
                                                                                // copy
                                                                                // individual
                                                                                // terms!
    this.midTerms = Arrays.copyOf( aSum.midTerms, aSum.midTerms.length ); // XXX
                                                                          // copy
                                                                          // individual
                                                                          // terms!
    this.finalTerm = new TriggerFinalTerm( aSum.finalTerm );
  }

  // METHODS

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept( final ITriggerVisitor aVisitor ) throws IOException
  {
    aVisitor.visitTriggerSum( this );
    this.finalTerm.accept( aVisitor );
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
   * Returns the current value of inputTerms.
   * 
   * @return the inputTerms
   */
  public TriggerInputTerm[] getInputTerms()
  {
    return this.inputTerms;
  }

  /**
   * Returns the current value of midTerms.
   * 
   * @return the midTerms
   */
  public TriggerMidTerm[] getMidTerms()
  {
    return this.midTerms;
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
   * @return
   */
  int getOffset()
  {
    return ( 0x40 + ( this.stateNumber * 4 ) + this.stateTerm.getOffset() );
  }

  /**
   * Resets this trigger sum to its initial state.
   */
  void reset()
  {
    this.stateNumber = 0;
    for ( TriggerInputTerm inputTerm : this.inputTerms )
    {
      inputTerm.reset();
    }
    for ( TriggerMidTerm midTerm : this.midTerms )
    {
      midTerm.reset();
    }
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
