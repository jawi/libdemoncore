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
 * Denotes a trigger term, which is either a bit-value, a range detector, an
 * edge detector or timer.
 */
public abstract class AbstractTriggerTerm implements ITriggerVisitable
{
  // VARIABLES

  private final TriggerTermType type;
  private TriggerInputState state;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerTerm instance as an exact copy of the given trigger
   * term.
   */
  protected AbstractTriggerTerm( final AbstractTriggerTerm aTerm )
  {
    this.type = aTerm.type;
    this.state = aTerm.state;
  }

  /**
   * Creates a new TriggerTerm instance.
   */
  protected AbstractTriggerTerm( final TriggerTermType aType )
  {
    this.type = aType;
    reset();
  }

  // METHODS

  /**
   * Factory method for creating a copy {@link AbstractTriggerTerm} instance,
   * based on the given {@link AbstractTriggerTerm} instance.
   * 
   * @param aTerm
   *          the term type to copy, cannot be <code>null</code>.
   * @return a new {@link AbstractTriggerTerm} instance, as exact copy of the
   *         given term, never <code>null</code>.
   */
  public static final AbstractTriggerTerm create( final AbstractTriggerTerm aTerm )
  {
    switch ( aTerm.getType() )
    {
      case TERM_A:
      case TERM_B:
      case TERM_C:
      case TERM_D:
      case TERM_E:
      case TERM_F:
      case TERM_G:
      case TERM_H:
      case TERM_I:
      case TERM_J:
        return new TriggerTerm( ( TriggerTerm )aTerm );

      case TERM_EDGE1:
      case TERM_EDGE2:
        return new TriggerEdgeDetector( ( TriggerEdgeDetector )aTerm );

      case TERM_RANGE1:
      case TERM_RANGE2:
        return new TriggerRangeDetector( ( TriggerRangeDetector )aTerm );

      case TERM_TIMER1:
      case TERM_TIMER2:
        return new TriggerTimer( ( TriggerTimer )aTerm );

      default:
        throw new IllegalArgumentException();
    }
  }

  /**
   * Factory method for creating a {@link AbstractTriggerTerm} instance, based
   * on the given {@link TriggerTermType}.
   * 
   * @param aType
   *          the term type to create a trigger term instance for, cannot be
   *          <code>null</code>.
   * @return a new (empty) {@link AbstractTriggerTerm} instance, never
   *         <code>null</code>.
   */
  public static final AbstractTriggerTerm create( final TriggerTermType aType )
  {
    switch ( aType )
    {
      case TERM_A:
      case TERM_B:
      case TERM_C:
      case TERM_D:
      case TERM_E:
      case TERM_F:
      case TERM_G:
      case TERM_H:
      case TERM_I:
      case TERM_J:
        return new TriggerTerm( aType );

      case TERM_EDGE1:
      case TERM_EDGE2:
        return new TriggerEdgeDetector( aType );

      case TERM_RANGE1:
      case TERM_RANGE2:
        return new TriggerRangeDetector( aType );

      case TERM_TIMER1:
      case TERM_TIMER2:
        return new TriggerTimer( aType );

      default:
        throw new IllegalArgumentException();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void accept( final ITriggerVisitor aVisitor ) throws IOException
  {
    aVisitor.visit( this );
  }

  /**
   * Returns the current value of state.
   * 
   * @return the state
   */
  public final TriggerInputState getState()
  {
    return this.state;
  }

  /**
   * Returns the current value of type.
   * 
   * @return the type
   */
  public final TriggerTermType getType()
  {
    return this.type;
  }

  /**
   * @return <code>true</code> if this trigger input is disabled,
   *         <code>false</code> otherwise.
   */
  public final boolean isDisabled()
  {
    return this.state.isDisabled();
  }

  /**
   * @return <code>true</code> if this trigger input is either enabled (may be
   *         inverted as well), <code>false</code> otherwise.
   */
  public final boolean isEnabled()
  {
    return this.state.isEnabled();
  }

  /**
   * @return <code>true</code> if this trigger input is enabled, but inverted,
   *         <code>false</code> otherwise.
   */
  public final boolean isInverted()
  {
    return this.state.isInverted();
  }

  /**
   * Marks this trigger term as disabled.
   */
  public final void setDisabled()
  {
    this.state = TriggerInputState.DISABLED;
  }

  /**
   * Marks this trigger term as enabled.
   */
  public final void setEnabled()
  {
    this.state = TriggerInputState.ENABLED;
  }

  /**
   * Marks this trigger term as enabled and inverted.
   */
  public final void setInverted()
  {
    this.state = TriggerInputState.ENABLED_INVERTED;
  }

  /**
   * Sets the current value of state.
   * 
   * @return the state
   */
  public final void setState( final TriggerInputState aState )
  {
    this.state = aState;
  }

  /**
   * Resets this trigger term to its initial state.
   */
  protected void reset()
  {
    this.state = TriggerInputState.DISABLED;
  }
}
