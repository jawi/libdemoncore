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
 * Denotes an input term for a trigger sum.
 */
public class TriggerInputTerm implements ITriggerVisitable
{
  // VARIABLES

  private final AbstractTriggerTerm termA;
  private final AbstractTriggerTerm termB;
  private TriggerOperation operation;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerInputTerm instance.
   */
  public TriggerInputTerm( final AbstractTriggerTerm aTermA, final AbstractTriggerTerm aTermB )
  {
    if ( ( aTermA == null ) || ( aTermB == null ) )
    {
      throw new IllegalArgumentException( "None of the trigger terms can be null!" );
    }
    this.termA = aTermA;
    this.termB = aTermB;
    reset();
  }

  /**
   * Creates a new TriggerInputTerm instance as an exact copy of the given
   * trigger input term.
   */
  public TriggerInputTerm( final TriggerInputTerm aInput )
  {
    this.termA = aInput.termA;
    this.termB = aInput.termB;
    this.operation = aInput.operation;
  }

  // METHODS

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept( final ITriggerVisitor aVisitor ) throws IOException
  {
    aVisitor.visitInputTerm( this );
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
  public AbstractTriggerTerm getTermA()
  {
    return this.termA;
  }

  /**
   * Returns the current value of termB.
   * 
   * @return the termB
   */
  public AbstractTriggerTerm getTermB()
  {
    return this.termB;
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
   * Resets this trigger input term to its initial state.
   */
  final void reset()
  {
    this.termA.reset();
    this.termB.reset();
    this.operation = TriggerOperation.OR;
  }
}
