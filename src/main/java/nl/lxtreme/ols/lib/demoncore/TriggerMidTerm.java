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
public class TriggerMidTerm extends AbstractTriggerOperationTerm
{
  // VARIABLES

  private final TriggerPairTerm termA;
  private final TriggerPairTerm termB;
  private final TriggerPairTerm termC;
  private final TriggerPairTerm termD;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerMidTerm instance as an exact copy of the given mid
   * term.
   * 
   * @param aMidTerm
   *          the mid term to copy.
   */
  public TriggerMidTerm( final TriggerMidTerm aMidTerm )
  {
    super( aMidTerm );
    this.termA = new TriggerPairTerm( aMidTerm.termA );
    this.termB = new TriggerPairTerm( aMidTerm.termB );
    this.termC = new TriggerPairTerm( aMidTerm.termC );
    this.termD = new TriggerPairTerm( aMidTerm.termD );
  }

  /**
   * Creates a new TriggerMidTerm instance.
   */
  public TriggerMidTerm( final TriggerPairTerm aTermA, final TriggerPairTerm aTermB, final TriggerPairTerm aTermC,
      final TriggerPairTerm aTermD )
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

  // METHODS

  /**
   * {@inheritDoc}
   */
  public void accept( final ITriggerVisitor aVisitor ) throws IOException
  {
    this.termD.accept( aVisitor );
    this.termC.accept( aVisitor );
    this.termB.accept( aVisitor );
    this.termA.accept( aVisitor );

    aVisitor.visit( this );
  }

  /**
   * Returns the current value of termA.
   * 
   * @return the termA
   */
  public TriggerPairTerm getTermA()
  {
    return this.termA;
  }

  /**
   * Returns the current value of termB.
   * 
   * @return the termB
   */
  public TriggerPairTerm getTermB()
  {
    return this.termB;
  }

  /**
   * Returns the current value of termC.
   * 
   * @return the termC
   */
  public TriggerPairTerm getTermC()
  {
    return this.termC;
  }

  /**
   * Returns the current value of termD.
   * 
   * @return the termD
   */
  public TriggerPairTerm getTermD()
  {
    return this.termD;
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
    this.termC.reset();
    this.termD.reset();
  }
}
