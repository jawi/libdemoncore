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


import javax.swing.*;

import nl.lxtreme.ols.lib.demoncore.*;


/**
 * 
 */
public class TestUI extends JFrame
{
  // CONSTANTS

  private static final long serialVersionUID = 1L;

  /**
   * Creates a new TestUI instance.
   */
  public TestUI()
  {
    super( "TEST UI" );
  }

  // MAIN ENTRY POINT

  /**
   * @param args
   */
  public static void main( final String[] args )
  {
    final TestUI testUI = new TestUI();
    SwingUtilities.invokeLater( new Runnable()
    {
      @Override
      public void run()
      {
        testUI.init();
        testUI.run();
      }
    } );
  }

  /**
   * Initializes this frame.
   */
  protected void init()
  {
    setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
    setSize( 640, 480 );

    final TriggerSequenceState seqState = new TriggerSequenceState();
    seqState.setStateNumber( 1 );

    final TriggerSum captureTerms = seqState.getTriggerSum( TriggerStateTerm.CAPTURE );

    TriggerFinalTerm finalTerm = captureTerms.getFinalTerm();
    TriggerMidTerm midTermA = finalTerm.getTermA();
    midTermA.setOperation( TriggerOperation.OR );

    TriggerPairTerm pairTermA = midTermA.getTermA();
    pairTermA.setOperation( TriggerOperation.AND );
    pairTermA.getTermA().setEnabled();
    pairTermA.getTermB().setInverted();

    TriggerPairTerm pairTermB = midTermA.getTermB();
    pairTermB.setOperation( TriggerOperation.OR );
    pairTermB.getTermB().setEnabled();

    final TriggerSum hitTerms = seqState.getTriggerSum( TriggerStateTerm.HIT );

    finalTerm = hitTerms.getFinalTerm();
    midTermA = finalTerm.getTermA();
    midTermA.setOperation( TriggerOperation.OR );

    pairTermA = midTermA.getTermA();
    pairTermA.setOperation( TriggerOperation.ANY );
    pairTermA.getTermA().setEnabled();

    // getContentPane().add( new TriggerSumPanel( TriggerMode.STATE ) );
    getContentPane().add( new TriggerSequencePanel( TriggerMode.STATE, seqState ) );
  }

  /**
   * Makes this frame visible on screen.
   */
  protected void run()
  {
    setVisible( true );
  }
}
