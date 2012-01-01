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
package nl.lxtreme.ols.lib.demoncore.ui;


import static org.junit.Assert.*;
import static nl.lxtreme.ols.lib.demoncore.ui.TriggerStringifier.*;

import nl.lxtreme.ols.lib.demoncore.*;

import org.junit.*;


/**
 * Test cases for {@link TriggerStringifier}.
 */
public class TriggerStringifierTest
{
  private static final String COMPLEX_TRIGGER_SUM = "((A + B".concat( NEGATE_STR ).concat( ") " ).concat( XOR_STR )
      .concat( " (F " ).concat( AND_STR ).concat( " G))" );
  // VARIABLES

  private TriggerStringifier stringifier;

  // METHODS

  /**
   * @throws java.lang.Exception
   */
  @Before
  public void setUp() throws Exception
  {
    this.stringifier = new TriggerStringifier();
  }

  /**
   * Test method for {@link TriggerStringifier#toString(ITriggerVisitable)}.
   */
  @Test
  public void testToStringDisabledTriggerTermOk()
  {
    TriggerTerm term = ( TriggerTerm )AbstractTriggerTerm.create( TriggerTermType.TERM_A );

    assertEquals( "", this.stringifier.toString( term ) );
  }

  /**
   * Test method for {@link TriggerStringifier#toString(ITriggerVisitable)}.
   */
  @Test
  public void testToStringEmptyTriggerPairOk()
  {
    TriggerTerm termA = ( TriggerTerm )AbstractTriggerTerm.create( TriggerTermType.TERM_A );
    TriggerTerm termB = ( TriggerTerm )AbstractTriggerTerm.create( TriggerTermType.TERM_B );

    TriggerPairTerm pair = new TriggerPairTerm( termA, termB );
    pair.setOperation( TriggerOperation.NOR );

    assertEquals( "", this.stringifier.toString( pair ) );
  }

  /**
   * Test method for {@link TriggerStringifier#toString(ITriggerVisitable)}.
   */
  @Test
  public void testToStringEmptyTriggerSequenceStateOk()
  {
    TriggerSequenceState seqState = new TriggerSequenceState();

    assertEquals( "", this.stringifier.toString( seqState ) );
  }

  /**
   * Test method for {@link TriggerStringifier#toString(ITriggerVisitable)}.
   */
  @Test
  public void testToStringEmptyTriggerSumOk()
  {
    TriggerSum sum = new TriggerSum( TriggerStateTerm.CAPTURE );
    assertEquals( "", this.stringifier.toString( sum ) );
  }

  /**
   * Test method for {@link TriggerStringifier#toString(ITriggerVisitable)}.
   */
  @Test
  public void testToStringEnabledTriggerTermOk()
  {
    TriggerTerm term = ( TriggerTerm )AbstractTriggerTerm.create( TriggerTermType.TERM_A );
    term.setEnabled();

    assertEquals( "A", this.stringifier.toString( term ) );
  }

  /**
   * Test method for {@link TriggerStringifier#toString(ITriggerVisitable)}.
   */
  @Test
  public void testToStringInvertedTriggerTermOk()
  {
    TriggerTerm term = ( TriggerTerm )AbstractTriggerTerm.create( TriggerTermType.TERM_B );
    term.setInverted();

    assertEquals( "B".concat( NEGATE_STR ), this.stringifier.toString( term ) );
  }

  /**
   * Test method for {@link TriggerStringifier#toString(ITriggerVisitable)}.
   */
  @Test( expected = IllegalArgumentException.class )
  public void testToStringNullValueFail()
  {
    this.stringifier.toString( null );
  }

  /**
   * Test method for {@link TriggerStringifier#toString(ITriggerVisitable)}.
   */
  @Test
  public void testToStringTriggerPairOk()
  {
    TriggerTerm termA = ( TriggerTerm )AbstractTriggerTerm.create( TriggerTermType.TERM_A );
    TriggerTerm termB = ( TriggerTerm )AbstractTriggerTerm.create( TriggerTermType.TERM_B );
    TriggerPairTerm pair = new TriggerPairTerm( termA, termB );

    termA.setEnabled();
    termB.setEnabled();
    pair.setOperation( TriggerOperation.NOR );

    assertEquals( NOT_STR.concat( "(A + B)" ), this.stringifier.toString( pair ) );
  }

  /**
   * Test method for {@link TriggerStringifier#toString(ITriggerVisitable)}.
   */
  @Test
  public void testToStringTriggerSequenceStateOk()
  {
    Integer occurrenceCount = Integer.valueOf( 2 );

    TriggerSequenceState seqState = new TriggerSequenceState();
    seqState.setOccurrenceCount( occurrenceCount.intValue() );
    makeComplexTriggerSum( seqState.getTriggerSum( TriggerStateTerm.CAPTURE ) );
    makeSimpleTriggerSum( seqState.getTriggerSum( TriggerStateTerm.HIT ) );

    assertEquals(
        String.format( "While storing %s\nIf %s occurs for %d samples", COMPLEX_TRIGGER_SUM, "A", occurrenceCount ),
        this.stringifier.toString( seqState ) );
  }

  /**
   * Test method for {@link TriggerStringifier#toString(ITriggerVisitable)}.
   */
  @Test
  public void testToStringTriggerSequenceWithElseStateOk()
  {
    Integer occurrenceCount = Integer.valueOf( 2 );

    TriggerSequenceState seqState = new TriggerSequenceState();
    seqState.setOccurrenceCount( occurrenceCount.intValue() );
    seqState.setElseState( 3 );
    makeComplexTriggerSum( seqState.getTriggerSum( TriggerStateTerm.CAPTURE ) );
    makeSimpleTriggerSum( seqState.getTriggerSum( TriggerStateTerm.HIT ) );
    makeSimpleTriggerSum( seqState.getTriggerSum( TriggerStateTerm.ELSE ) );

    assertEquals( String.format( "While storing %s\nIf %s occurs for %d samples\nelse %s go to level %d",
        COMPLEX_TRIGGER_SUM, "A", occurrenceCount, "A", Integer.valueOf( 3 ) ), this.stringifier.toString( seqState ) );
  }

  /**
   * Test method for {@link TriggerStringifier#toString(ITriggerVisitable)}.
   */
  @Test
  public void testToStringTriggerSumOk()
  {
    TriggerSum sum = new TriggerSum( TriggerStateTerm.CAPTURE );
    makeComplexTriggerSum( sum );

    assertEquals( COMPLEX_TRIGGER_SUM, this.stringifier.toString( sum ) );
  }

  /**
   * Makes a trigger sum in the form of "((A + ~B) (X) (F & G))".
   * 
   * @param aSum
   *          the trigger sum to define/make.
   */
  private void makeComplexTriggerSum( final TriggerSum aSum )
  {
    TriggerFinalTerm finalTerm = aSum.getFinalTerm();

    TriggerMidTerm midTermA = finalTerm.getTermA();
    TriggerPairTerm pairTermA = midTermA.getTermA();
    TriggerTerm termA = ( TriggerTerm )pairTermA.getTermA();
    TriggerTerm termB = ( TriggerTerm )pairTermA.getTermB();

    TriggerMidTerm midTermB = finalTerm.getTermB();
    TriggerPairTerm pairTermB = midTermB.getTermA();
    TriggerTerm termF = ( TriggerTerm )pairTermB.getTermA();
    TriggerTerm termG = ( TriggerTerm )pairTermB.getTermB();

    termA.setEnabled();
    termB.setInverted();
    pairTermA.setOperation( TriggerOperation.OR );

    termF.setEnabled();
    termG.setEnabled();
    pairTermB.setOperation( TriggerOperation.AND );

    finalTerm.setOperation( TriggerOperation.XOR );
  }

  /**
   * Makes a simple trigger sum in the form of "A".
   * 
   * @param aSum
   *          the trigger sum to define/make.
   */
  private void makeSimpleTriggerSum( final TriggerSum aSum )
  {
    TriggerFinalTerm finalTerm = aSum.getFinalTerm();

    TriggerMidTerm midTermA = finalTerm.getTermA();
    TriggerPairTerm pairTermA = midTermA.getTermA();
    TriggerTerm termA = ( TriggerTerm )pairTermA.getTermA();

    termA.setEnabled();
    pairTermA.setOperation( TriggerOperation.OR );

    midTermA.setOperation( TriggerOperation.AND );

    finalTerm.setOperation( TriggerOperation.XOR );
  }
}
