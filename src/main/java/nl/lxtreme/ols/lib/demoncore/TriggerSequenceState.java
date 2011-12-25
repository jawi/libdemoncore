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
  // CONSTANTS

  private static final int TRIGSTATE_STATENUM_MASK = 0xF;
  private static final int TRIGSTATE_OBTAIN_MASK = 0x000FFFFF;
  private static final int TRIGSTATE_ELSE_BITOFS = 20;
  private static final long TRIGSTATE_STOP_TIMER0 = 0x01000000L;
  private static final long TRIGSTATE_STOP_TIMER1 = 0x02000000L;
  private static final long TRIGSTATE_CLEAR_TIMER0 = 0x04000000L;
  private static final long TRIGSTATE_CLEAR_TIMER1 = 0x08000000L;
  private static final long TRIGSTATE_START_TIMER0 = 0x10000000L;
  private static final long TRIGSTATE_START_TIMER1 = 0x20000000L;
  private static final long TRIGSTATE_TRIGGER_FLAG = 0x40000000L;
  private static final long TRIGSTATE_LASTSTATE = 0x80000000L;

  // VARIABLES

  private int stateNumber; // 0 to 15
  private boolean lastState;
  private boolean raiseTrigger; //
  private int startTimer; // bit0=timer1, bit1=timer2
  private int stopTimer; // bit0=timer1, bit1=timer2
  private int clearTimer; // bit0=timer1, bit1=timer2
  private int elseState; // 0 to 15
  private int occurrenceCount;

  /**
   * Creates a new {@link TriggerSequenceState} instance.
   */
  TriggerSequenceState()
  {
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
  TriggerSequenceState( final TriggerSequenceState aInitial )
  {
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
    // Select the correct LUT chain...
    aVisitor.selectChain( this.stateNumber & TRIGSTATE_STATENUM_MASK );

    // Build the actual chain data...
    int value = ( ( this.elseState & TRIGSTATE_STATENUM_MASK ) << TRIGSTATE_ELSE_BITOFS )
        | ( this.occurrenceCount & TRIGSTATE_OBTAIN_MASK );
    if ( this.lastState )
    {
      value |= TRIGSTATE_LASTSTATE;
    }
    if ( this.raiseTrigger )
    {
      value |= TRIGSTATE_TRIGGER_FLAG;
    }
    if ( ( this.startTimer & 1 ) != 0 )
    {
      value |= TRIGSTATE_START_TIMER0;
    }
    if ( ( this.startTimer & 2 ) != 0 )
    {
      value |= TRIGSTATE_START_TIMER1;
    }
    if ( ( this.stopTimer & 1 ) != 0 )
    {
      value |= TRIGSTATE_STOP_TIMER0;
    }
    if ( ( this.stopTimer & 2 ) != 0 )
    {
      value |= TRIGSTATE_STOP_TIMER1;
    }
    if ( ( this.clearTimer & 1 ) != 0 )
    {
      value |= TRIGSTATE_CLEAR_TIMER0;
    }
    if ( ( this.clearTimer & 2 ) != 0 )
    {
      value |= TRIGSTATE_CLEAR_TIMER1;
    }

    aVisitor.writeChain( value );
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
  }

  /**
   * Sets clearTimer to the given value.
   * 
   * @param aClearTimer
   *          the clearTimer to set.
   */
  void setClearTimer( final int aClearTimer )
  {
    this.clearTimer = aClearTimer;
  }

  /**
   * Sets elseState to the given value.
   * 
   * @param aElseState
   *          the elseState to set.
   */
  void setElseState( final int aElseState )
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
  void setLastState( final boolean aLastState )
  {
    this.lastState = aLastState;
  }

  /**
   * Sets occurrenceCount to the given value.
   * 
   * @param aOccurrenceCount
   *          the occurrenceCount to set.
   */
  void setOccurrenceCount( final int aOccurrenceCount )
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
  void setRaiseTrigger( final boolean aRaiseTrigger )
  {
    this.raiseTrigger = aRaiseTrigger;
  }

  /**
   * Sets startTimer to the given value.
   * 
   * @param aStartTimer
   *          the startTimer to set.
   */
  void setStartTimer( final int aStartTimer )
  {
    this.startTimer = aStartTimer;
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

  /**
   * Sets stopTimer to the given value.
   * 
   * @param aStopTimer
   *          the stopTimer to set.
   */
  void setStopTimer( final int aStopTimer )
  {
    this.stopTimer = aStopTimer;
  }
}
