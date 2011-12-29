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
 * Denotes a middle term for a trigger sum.
 */
public class TriggerMidTerm implements ITriggerVisitable
{
  // VARIABLES

  private final TriggerInputTerm termA;
  private final TriggerInputTerm termB;
  private final TriggerInputTerm termC;
  private final TriggerInputTerm termD;
  private TriggerOperation operation;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerMidTerm instance.
   */
  public TriggerMidTerm( final TriggerInputTerm aTermA, final TriggerInputTerm aTermB, final TriggerInputTerm aTermC,
      final TriggerInputTerm aTermD )
  {
    if ( ( aTermA == null ) || ( aTermB == null ) || ( aTermC == null ) || ( aTermD == null ) )
    {
      throw new IllegalArgumentException( "None of the input terms can be null!" );
    }
    this.termA = aTermA;
    this.termB = aTermB;
    this.termC = aTermC;
    this.termD = aTermD;
    reset();
  }

  /**
   * Creates a new TriggerMidTerm instance as an exact copy of the given mid
   * term.
   * 
   * @param aMidTerm
   *          the mid term to copy.
   */
  public TriggerMidTerm( final TriggerMidTerm aMidTerm )
  {
    this.termA = aMidTerm.termA;
    this.termB = aMidTerm.termB;
    this.termC = aMidTerm.termC;
    this.termD = aMidTerm.termD;
    this.operation = aMidTerm.operation;
  }

  // METHODS

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept( final ITriggerVisitor aVisitor ) throws IOException
  {
    aVisitor.visitMidTerm( this );
  }

  /**
   * Returns the current value of operation.
   * 
   * @return the operation
   */
  public TriggerOperation getOperation()
  {
    return this.operation;
  }

  /**
   * Returns the current value of termA.
   * 
   * @return the termA
   */
  public TriggerInputTerm getTermA()
  {
    return this.termA;
  }

  /**
   * Returns the current value of termB.
   * 
   * @return the termB
   */
  public TriggerInputTerm getTermB()
  {
    return this.termB;
  }

  /**
   * Returns the current value of termC.
   * 
   * @return the termC
   */
  public TriggerInputTerm getTermC()
  {
    return this.termC;
  }

  /**
   * Returns the current value of termD.
   * 
   * @return the termD
   */
  public TriggerInputTerm getTermD()
  {
    return this.termD;
  }

  /**
   * Sets operation to the given value.
   * 
   * @param aOperation
   *          the operation to set.
   */
  public void setOperation( final TriggerOperation aOperation )
  {
    if ( aOperation == null )
    {
      throw new IllegalArgumentException( "Operation cannot be null!" );
    }
    this.operation = aOperation;
  }

  /**
   * @return the offset of the current operation.
   */
  final int getOffset()
  {
    return this.operation.getOffset();
  }

  /**
   * Resets this trigger mid term to its initial state.
   */
  final void reset()
  {
    this.operation = TriggerOperation.OR;
  }
}
