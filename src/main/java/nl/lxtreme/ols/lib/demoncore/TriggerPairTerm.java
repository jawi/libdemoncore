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
public class TriggerPairTerm extends AbstractTriggerOperationTerm
{
  // VARIABLES

  private final AbstractTriggerTerm termA;
  private final AbstractTriggerTerm termB;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerInputTerm instance.
   */
  public TriggerPairTerm( final AbstractTriggerTerm aTermA, final AbstractTriggerTerm aTermB )
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
  public TriggerPairTerm( final TriggerPairTerm aInput )
  {
    super( aInput );
    this.termA = AbstractTriggerTerm.create( aInput.termA );
    this.termB = AbstractTriggerTerm.create( aInput.termB );
  }

  // METHODS

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept( final ITriggerVisitor aVisitor ) throws IOException
  {
    this.termB.accept( aVisitor );
    this.termA.accept( aVisitor );

    aVisitor.visit( this );
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
   * {@inheritDoc}
   */
  @Override
  protected void reset()
  {
    super.reset();

    this.termA.reset();
    this.termB.reset();
  }
}
