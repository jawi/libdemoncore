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
 * 
 */
public class TriggerLevel implements ITriggerVisitable
{
  // VARIABLES

  private final TriggerSum captureSum;
  private final TriggerSum hitSum;
  private final TriggerSum elseSum;
  private final TriggerSequenceState state;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerLevel instance.
   */
  TriggerLevel( final TriggerSum aCaptureSum, final TriggerSum aHitSum, final TriggerSum aElseSum,
      final TriggerSequenceState aState )
  {
    this.captureSum = new TriggerSum( aCaptureSum );
    this.hitSum = new TriggerSum( aHitSum );
    this.elseSum = new TriggerSum( aElseSum );
    this.state = new TriggerSequenceState( aState );
  }

  // METHODS

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept( final ITriggerVisitor aVisitor ) throws IOException
  {
    this.state.accept( aVisitor );
    this.captureSum.accept( aVisitor );
    this.hitSum.accept( aVisitor );
    this.elseSum.accept( aVisitor );
  }

  /**
   * Returns the current value of captureSum.
   * 
   * @return the captureSum
   */
  public TriggerSum getCaptureSum()
  {
    return this.captureSum;
  }

  /**
   * Returns the current value of elseSum.
   * 
   * @return the elseSum
   */
  public TriggerSum getElseSum()
  {
    return this.elseSum;
  }

  /**
   * Returns the current value of hitSum.
   * 
   * @return the hitSum
   */
  public TriggerSum getHitSum()
  {
    return this.hitSum;
  }

  /**
   * Returns the current value of state.
   * 
   * @return the state
   */
  public TriggerSequenceState getState()
  {
    return this.state;
  }

  /**
   * Resets this trigger level to its initial state.
   */
  void reset()
  {
    this.captureSum.reset();
    this.elseSum.reset();
    this.hitSum.reset();
    this.state.reset();
  }

}
