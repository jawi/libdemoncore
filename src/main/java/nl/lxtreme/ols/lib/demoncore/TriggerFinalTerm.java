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
 * Denotes the final trigger term for a trigger sum.
 */
public class TriggerFinalTerm extends AbstractTriggerOperationTerm
{
  // VARIABLES

  private final TriggerMidTerm termA;
  private final TriggerMidTerm termB;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerFinalTerm instance as an exact copy of the given
   * trigger input term.
   */
  public TriggerFinalTerm( final TriggerFinalTerm aFinalTerm )
  {
    super( aFinalTerm );
    this.termA = new TriggerMidTerm( aFinalTerm.termA );
    this.termB = new TriggerMidTerm( aFinalTerm.termB );
  }

  /**
   * Creates a new TriggerFinalTerm instance.
   */
  public TriggerFinalTerm( final TriggerMidTerm aTermA, final TriggerMidTerm aTermB )
  {
    if ( ( aTermA == null ) || ( aTermB == null ) )
    {
      throw new IllegalArgumentException( "None of the mid terms can be null!" );
    }
    this.termA = aTermA;
    this.termB = aTermB;
    reset();
  }

  // METHODS

  /**
   * {@inheritDoc}
   */
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
  public TriggerMidTerm getTermA()
  {
    return this.termA;
  }

  /**
   * Returns the current value of termB.
   * 
   * @return the termB
   */
  public TriggerMidTerm getTermB()
  {
    return this.termB;
  }

  /**
   * @return
   */
  final TriggerMidTerm[] getMidTerms()
  {
    return new TriggerMidTerm[] { this.termA, this.termB };
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
