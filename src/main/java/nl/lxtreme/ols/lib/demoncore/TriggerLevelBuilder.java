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


/**
 * Provides a builder for creating {@link TriggerLevel} instances.
 */
public final class TriggerLevelBuilder
{
  // VARIABLES

  private final TriggerSum captureSum;
  private final TriggerSum hitSum;
  private final TriggerSum elseSum;
  private final TriggerSequenceState state;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerSumBuilder instance.
   */
  public TriggerLevelBuilder()
  {
    this.captureSum = new TriggerSum( TriggerStateTerm.CAPTURE );
    this.hitSum = new TriggerSum( TriggerStateTerm.HIT );
    this.elseSum = new TriggerSum( TriggerStateTerm.ELSE );
    this.state = new TriggerSequenceState();
  }

  // METHODS

  /**
   * Builds the actual trigger level.
   * 
   * @return a {@link TriggerLevel} instance, never <code>null</code>.
   */
  public TriggerLevel build()
  {
    // Create a new copies!
    TriggerLevel result = new TriggerLevel( this.captureSum, this.hitSum, this.elseSum, this.state );

    // Reset the current sums & sequence state to their initial states...
    this.captureSum.reset();
    this.hitSum.reset();
    this.elseSum.reset();
    this.state.reset();

    return result;
  }

  /**
   * Marks that upon a full hit on the sequence state the FSM should proceed to
   * the next state.
   * 
   * @return this builder.
   * @see #lastState()
   */
  public TriggerLevelBuilder continueToNextState()
  {
    this.state.setLastState( false );
    return this;
  }

  /**
   * Denotes how the else-term is defined.
   * 
   * @param aTriggerTerms
   *          the trigger terms to set;
   * @param aStateNumber
   *          the else-state number to branch to, >= 0 && < 16.
   * @return this builder.
   */
  public TriggerLevelBuilder elseTarget( final TriggerTerms aTriggerTerms, final int aStateNumber )
  {
    if ( aTriggerTerms == null )
    {
      throw new IllegalArgumentException( "TriggerTerms cannot be null!" );
    }
    if ( ( aStateNumber < 0 ) || ( aStateNumber > 15 ) )
    {
      throw new IllegalArgumentException( "Invalid state number!" );
    }
    this.elseSum.setTriggerTerms( aTriggerTerms );
    this.state.setElseState( aStateNumber );
    return this;
  }

  /**
   * Sets the state number of this trigger.
   * 
   * @param aStateNumber
   *          the state number of this trigger, >= 0 && < 16.
   * @return this builder.
   */
  public TriggerLevelBuilder forState( final int aStateNumber )
  {
    if ( ( aStateNumber < 0 ) || ( aStateNumber > 15 ) )
    {
      throw new IllegalArgumentException( "Invalid state number!" );
    }

    this.hitSum.setStateNumber( aStateNumber );
    this.captureSum.setStateNumber( aStateNumber );
    this.elseSum.setStateNumber( aStateNumber );

    this.state.setStateNumber( aStateNumber );

    return this;
  }

  /**
   * Denotes how the hit-term is defined.
   * 
   * @param aTriggerSum
   *          the trigger sum.
   * @return this builder.
   */
  public TriggerLevelBuilder ifTarget( final TriggerTerms aTriggerTerms )
  {
    if ( aTriggerTerms == null )
    {
      throw new IllegalArgumentException( "TriggerTerms cannot be null!" );
    }
    this.hitSum.setTriggerTerms( aTriggerTerms );
    return this;
  }

  /**
   * Marks this sequence state as the last state.
   * 
   * @return this builder.
   * @see #continueToNextState()
   */
  public TriggerLevelBuilder lastState()
  {
    this.state.setLastState( true );
    return this;
  }

  /**
   * Sets the number of hits the if-state should obtain in order to proceed to
   * the next state.
   * 
   * @param aOccurrenceCount
   *          the 20-bit number of occurrences, >= 0.
   * @return this builder.
   */
  public TriggerLevelBuilder occurs( final int aOccurrenceCount )
  {
    this.state.setOccurrenceCount( aOccurrenceCount );
    return this;
  }

  /**
   * Clears the first timer upon a full hit of the if-condition.
   * 
   * @return this builder.
   */
  public TriggerLevelBuilder onHitClearTimer1()
  {
    this.state.setClearTimer( 1 );
    return this;
  }

  /**
   * Clears the second timer upon a full hit of the if-condition.
   * 
   * @return this builder.
   */
  public TriggerLevelBuilder onHitClearTimer2()
  {
    this.state.setClearTimer( 2 );
    return this;
  }

  /**
   * Arms the trigger for this state sequence.
   * 
   * @return this builder.
   */
  public TriggerLevelBuilder onHitRaiseTrigger()
  {
    this.state.setRaiseTrigger( true );
    return this;
  }

  /**
   * Starts the first timer upon a full hit of the if-condition.
   * 
   * @return this builder.
   */
  public TriggerLevelBuilder onHitStartTimer1()
  {
    this.state.setStartTimer( 1 );
    return this;
  }

  /**
   * Starts the second timer upon a full hit of the if-condition.
   * 
   * @return this builder.
   */
  public TriggerLevelBuilder onHitStartTimer2()
  {
    this.state.setStartTimer( 2 );
    return this;
  }

  /**
   * Stops the first timer upon a full hit of the if-condition.
   * 
   * @return this builder.
   */
  public TriggerLevelBuilder onHitStopTimer1()
  {
    this.state.setStopTimer( 1 );
    return this;
  }

  /**
   * Stops the second timer upon a full hit of the if-condition.
   * 
   * @return this builder.
   */
  public TriggerLevelBuilder onHitStopTimer2()
  {
    this.state.setStopTimer( 2 );
    return this;
  }

  /**
   * Denotes the capture-term is defined.
   * 
   * @param aTriggerSum
   *          the trigger sum.
   * @return this builder.
   */
  public TriggerLevelBuilder whileStoring( final TriggerTerms aTriggerTerms )
  {
    if ( aTriggerTerms == null )
    {
      throw new IllegalArgumentException( "TriggerTerms cannot be null!" );
    }
    this.captureSum.setTriggerTerms( aTriggerTerms );
    return this;
  }
}
