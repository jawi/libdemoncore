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
 * Denotes a trigger sequence state.
 */
public final class TriggerSequenceState implements ITriggerVisitable
{
  // VARIABLES

  private final TriggerSum captureSum;
  private final TriggerSum hitSum;
  private final TriggerSum elseSum;

  private int stateNumber; // 0 to 15
  private boolean lastState;
  private boolean raiseTrigger; //
  private int startTimer; // bit0=timer1, bit1=timer2
  private int stopTimer; // bit0=timer1, bit1=timer2
  private int clearTimer; // bit0=timer1, bit1=timer2
  private int elseState; // 0 to 15
  private int occurrenceCount;

  /**
   * Creates a new, empty, {@link TriggerSequenceState} instance.
   */
  public TriggerSequenceState()
  {
    this.captureSum = new TriggerSum( TriggerStateTerm.CAPTURE );
    this.hitSum = new TriggerSum( TriggerStateTerm.HIT );
    this.elseSum = new TriggerSum( TriggerStateTerm.ELSE );

    this.stateNumber = 0;
    this.lastState = false;
    this.raiseTrigger = false;
    this.startTimer = 0;
    this.stopTimer = 0;
    this.clearTimer = 0;
    this.elseState = 0;
    this.occurrenceCount = 0;
  }

  /**
   * Creates a new {@link TriggerSequenceState} instance as an exact copy of the
   * given {@link TriggerSequenceState}.
   * 
   * @param aInitial
   *          the {@link TriggerSequenceState} to copy.
   */
  public TriggerSequenceState( final TriggerSequenceState aInitial )
  {
    this.captureSum = new TriggerSum( aInitial.captureSum );
    this.hitSum = new TriggerSum( aInitial.hitSum );
    this.elseSum = new TriggerSum( aInitial.elseSum );

    this.stateNumber = aInitial.stateNumber;
    this.lastState = aInitial.lastState;
    this.raiseTrigger = aInitial.raiseTrigger;
    this.startTimer = aInitial.startTimer;
    this.stopTimer = aInitial.stopTimer;
    this.clearTimer = aInitial.clearTimer;
    this.elseState = aInitial.elseState;
    this.occurrenceCount = aInitial.occurrenceCount;
  }

  // METHODS

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept( final ITriggerVisitor aVisitor ) throws IOException
  {
    aVisitor.visitTriggerSequence( this );

    this.captureSum.accept( aVisitor );
    this.hitSum.accept( aVisitor );
    this.elseSum.accept( aVisitor );
  }

  /**
   * Returns the capture terms.
   * 
   * @return the capture trigger terms, never <code>null</code>.
   */
  public TriggerSum getCaptureTerms()
  {
    return this.captureSum;
  }

  /**
   * Returns the current value of clearTimer.
   * 
   * @return the clearTimer
   */
  public int getClearTimer()
  {
    return this.clearTimer;
  }

  /**
   * Returns the current value of elseState.
   * 
   * @return the elseState
   */
  public int getElseState()
  {
    return this.elseState;
  }

  /**
   * Returns the else terms.
   * 
   * @return the else trigger terms, never <code>null</code>.
   */
  public TriggerSum getElseTerms()
  {
    return this.elseSum;
  }

  /**
   * Returns the hit (or "if") terms.
   * 
   * @return the hit trigger terms, never <code>null</code>.
   */
  public TriggerSum getHitTerms()
  {
    return this.hitSum;
  }

  /**
   * Returns the current value of occurrenceCount.
   * 
   * @return the occurrenceCount
   */
  public int getOccurrenceCount()
  {
    return this.occurrenceCount;
  }

  /**
   * Returns the current value of startTimer.
   * 
   * @return the startTimer
   */
  public int getStartTimer()
  {
    return this.startTimer;
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
   * Returns the current value of stopTimer.
   * 
   * @return the stopTimer
   */
  public int getStopTimer()
  {
    return this.stopTimer;
  }

  /**
   * Returns the current value of lastState.
   * 
   * @return the lastState
   */
  public boolean isLastState()
  {
    return this.lastState;
  }

  /**
   * Returns the current value of setTrigger.
   * 
   * @return the setTrigger
   */
  public boolean isRaiseTrigger()
  {
    return this.raiseTrigger;
  }

  /**
   * Sets clearTimer to the given value.
   * 
   * @param aClearTimer
   *          the clearTimer to set.
   */
  public void setClearTimer( final int aClearTimer )
  {
    this.clearTimer = aClearTimer;
  }

  /**
   * Sets elseState to the given value.
   * 
   * @param aElseState
   *          the elseState to set.
   */
  public void setElseState( final int aElseState )
  {
    if ( ( aElseState < 0 ) || ( aElseState > 15 ) )
    {
      throw new IllegalArgumentException( "Invalid state number!" );
    }

    this.elseState = aElseState;
  }

  /**
   * Sets lastState to the given value.
   * 
   * @param aLastState
   *          the lastState to set.
   */
  public void setLastState( final boolean aLastState )
  {
    this.lastState = aLastState;
  }

  /**
   * Sets occurrenceCount to the given value.
   * 
   * @param aOccurrenceCount
   *          the occurrenceCount to set.
   */
  public void setOccurrenceCount( final int aOccurrenceCount )
  {
    this.occurrenceCount = aOccurrenceCount;
  }

  /**
   * Sets setTrigger to the given value.
   * 
   * @param aRaiseTrigger
   *          <code>true</code> to raise the trigger in this state,
   *          <code>false</code> to continue in the FSM.
   */
  public void setRaiseTrigger( final boolean aRaiseTrigger )
  {
    this.raiseTrigger = aRaiseTrigger;
  }

  /**
   * Sets startTimer to the given value.
   * 
   * @param aStartTimer
   *          the startTimer to set.
   */
  public void setStartTimer( final int aStartTimer )
  {
    this.startTimer = aStartTimer;
  }

  /**
   * Sets stateNumber to the given value.
   * 
   * @param aStateNumber
   *          the stateNumber to set.
   */
  public void setStateNumber( final int aStateNumber )
  {
    if ( ( aStateNumber < 0 ) || ( aStateNumber > 15 ) )
    {
      throw new IllegalArgumentException( "Invalid state number!" );
    }

    this.stateNumber = aStateNumber;
    this.captureSum.setStateNumber( aStateNumber );
    this.hitSum.setStateNumber( aStateNumber );
    this.elseSum.setStateNumber( aStateNumber );
  }

  /**
   * Sets stopTimer to the given value.
   * 
   * @param aStopTimer
   *          the stopTimer to set.
   */
  public void setStopTimer( final int aStopTimer )
  {
    this.stopTimer = aStopTimer;
  }

  /**
   * Resets the internal state of this sequence state to its initial state.
   */
  void reset()
  {
    this.stateNumber = 0;
    this.lastState = false;
    this.raiseTrigger = false;
    this.startTimer = 0;
    this.stopTimer = 0;
    this.clearTimer = 0;
    this.elseState = 0;
    this.occurrenceCount = 0;

    this.captureSum.reset();
    this.hitSum.reset();
    this.elseSum.reset();
  }
}
