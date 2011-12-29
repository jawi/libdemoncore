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


import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;

import javax.swing.*;

import nl.lxtreme.ols.lib.demoncore.*;


/**
 * Denotes a single trigger sequence, which defined what to capture when, and on
 * what trigger conditions.
 */
public class TriggerSequencePanel extends JPanel
{
  // CONSTANTS

  /**
   * Converts a {@link TriggerSum} to a single, human readable, string
   * representation.
   */
  static final class TriggerSumStringifier implements ITriggerVisitor
  {
    // CONSTANTS

    private static final boolean USE_NEGATION_LINE = true;

    private static final String NEGATE = "\u0305";
    private static final String NOT = "\u00AC";

    // VARIABLES

    private final Stack<String> symStack = new Stack<String>();

    // METHODS

    /**
     * Converts a given {@link TriggerSum} to a human readable string
     * representation.
     * 
     * @param aTriggerSum
     *          the trigger sum to convert to a string representation, cannot be
     *          <code>null</code>.
     * @return a human readable string representation of the given trigger sum,
     *         or an empty string if the given trigger sum did not provide any
     *         terms.
     */
    public String toString( final TriggerSum aTriggerSum )
    {
      this.symStack.clear();

      try
      {
        aTriggerSum.accept( this );
      }
      catch ( IOException exception )
      {
        exception.printStackTrace();
      }

      final StringBuilder sb = new StringBuilder();
      while ( !this.symStack.isEmpty() )
      {
        sb.insert( 0, this.symStack.pop() );
      }

      return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitFinalTerm( final TriggerFinalTerm aTerm ) throws IOException
    {
      int depth = this.symStack.size();

      aTerm.getTermB().accept( this );
      aTerm.getTermA().accept( this );

      // Walk across the stack and simplify its entries...
      walkSymbolStack( aTerm.getOperation(), depth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitInputTerm( final TriggerInputTerm aTerm ) throws IOException
    {
      int depth = this.symStack.size();

      // Visit the terms individually first...
      aTerm.getTermB().accept( this );
      aTerm.getTermA().accept( this );

      // Walk across the stack and simplify its entries...
      walkSymbolStack( aTerm.getOperation(), depth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitMidTerm( final TriggerMidTerm aTerm ) throws IOException
    {
      int depth = this.symStack.size();

      // Visit the terms individually first...
      aTerm.getTermD().accept( this );
      aTerm.getTermC().accept( this );
      aTerm.getTermB().accept( this );
      aTerm.getTermA().accept( this );

      // Walk across the stack and simplify its entries...
      walkSymbolStack( aTerm.getOperation(), depth );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitTerm( final AbstractTriggerTerm aTerm ) throws IOException
    {
      if ( !aTerm.isDisabled() )
      {
        this.symStack.push( asString( aTerm ) );
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitTriggerSequence( final TriggerSequenceState aTriggerSequenceState ) throws IOException
    {
      // NO-op
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visitTriggerSum( final TriggerSum aSum ) throws IOException
    {
      // NO-op
    }

    /**
     * Converts the given term to a string representation.
     * 
     * @param aTerm
     *          the trigger term to convert to string, cannot be
     *          <code>null</code>.
     * @return a string representation of the given term, never
     *         <code>null</code>.
     */
    private String asString( final AbstractTriggerTerm aTerm )
    {
      StringBuilder sb = new StringBuilder( RB.getString( aTerm.getType().getResourceKey() ) );
      if ( aTerm.isInverted() )
      {
        if ( !USE_NEGATION_LINE || ( sb.length() > 1 ) )
        {
          sb.insert( 0, NOT );
        }
        else
        {
          sb.append( NEGATE );
        }
      }
      return sb.toString();
    }

    /**
     * Combines a given list of terms with the given trigger operation into a
     * single string representation.
     * 
     * @param aTermList
     *          the list of terms, can be empty, but never <code>null</code>;
     * @param aOperation
     *          the trigger operation to apply to each term, cannot be
     *          <code>null</code>.
     * @return a string representation, can be <code>null</code> if the given
     *         term list is empty.
     */
    private String asString( final List<String> aTermList, final TriggerOperation aOperation )
    {
      if ( aTermList.isEmpty() )
      {
        return null;
      }

      final String opName = asString( aOperation );

      StringBuilder sb = new StringBuilder();
      for ( String term : aTermList )
      {
        if ( sb.length() > 0 )
        {
          sb.append( ' ' ).append( opName ).append( ' ' );
        }
        sb.append( term );
      }

      String result = sb.toString().trim();
      if ( ( aTermList.size() > 1 ) || aOperation.isInverted() )
      {
        result = "(".concat( result.concat( ")" ) );
      }
      if ( aOperation.isInverted() )
      {
        result = NOT.concat( result );
      }

      return result;
    }

    /**
     * Converts the given trigger operation to a string representation.
     * 
     * @param aOperation
     *          the operation to convert, cannot be <code>null</code>.
     * @return the string representation of the given operation, never
     *         <code>null</code>.
     */
    private String asString( final TriggerOperation aOperation )
    {
      switch ( aOperation )
      {
        case A_ONLY:
        case B_ONLY:
          return "";

        case ANY:
          return "any state";

        case NOP:
          return "no state";

        case NAND:
        case AND:
          return "\u22C5"; // middle dot

        case NOR:
        case OR:
          return "+"; // plus symbol

        case NXOR:
        case XOR:
          return "\u2295"; // circled plus

        default:
          throw new IllegalArgumentException();
      }
    }

    /**
     * @param symStack
     * @param aTerm
     */
    private void walkSymbolStack( final TriggerOperation aOperation, final int aDepth )
    {
      List<String> terms = new ArrayList<String>();
      while ( !this.symStack.isEmpty() && ( this.symStack.size() > aDepth ) )
      {
        terms.add( this.symStack.pop() );
      }

      String value = asString( terms, aOperation );
      if ( ( value != null ) && ( value.length() > 0 ) )
      {
        this.symStack.push( value );
      }
    }
  }

  // CONSTANTS

  private static final long serialVersionUID = 1L;

  private static final ResourceBundle RB = ResourceBundle.getBundle( "nl.lxtreme.ols.lib.demoncore.ui.DemonCore" );

  // VARIABLES

  private final TriggerSumStringifier triggerSumStringifier;
  private final TriggerSequenceState model;
  private final TriggerMode mode;

  private JLabel title;
  private JButton captureTerm;
  private JButton hitTerm;
  private JButton elseTerm;
  private JCheckBox setTrigger;
  private JButton timerControl;
  private JTextField occurenceCount;
  private JTextField gotoLevel;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerSequencePanel instance.
   * 
   * @param aMode
   *          the trigger mode, whether we're displaying states or timing
   *          values;
   * @param aModel
   *          the trigger sequence state model to use in this panel, cannot be
   *          <code>null</code>.
   */
  public TriggerSequencePanel( final TriggerMode aMode, final TriggerSequenceState aModel )
  {
    super( new GridBagLayout() );

    this.mode = aMode;
    // Create a copy of the given model as to not directly modify it!!!
    this.model = new TriggerSequenceState( aModel );
    this.triggerSumStringifier = new TriggerSumStringifier();

    initPanel();
    buildPanel();
  }

  // METHODS

  /**
   * Returns the current (possibly modified!) trigger sequence state.
   * 
   * @return the trigger sequence state, never <code>null</code>.
   */
  public TriggerSequenceState getTriggerSequence()
  {
    return this.model;
  }

  /**
   * Builds this panel by placing all components on it.
   */
  private void buildPanel()
  {
    GridBagConstraints gbc = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
        GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 0, 0 );

    gbc.weighty = 0.0;
    gbc.insets = new Insets( 8, 8, 0, 8 );

    add( this.title, gbc );

    gbc.gridy = 1;
    gbc.weighty = 1.0;

    add( buildSequencePane(), gbc );
  }

  /**
   * Builds the actual sequence pane, where you can define the capture/if/else
   * terms and so on.
   */
  private JPanel buildSequencePane()
  {
    JPanel pane = new JPanel( new GridBagLayout() );

    Insets rowInsets = new Insets( 0, 0, 10, 0 );
    Insets emptyInsets = new Insets( 0, 0, 0, 0 );

    GridBagConstraints gbc = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.BASELINE,
        GridBagConstraints.NONE, new Insets( 0, 0, 20, 0 ), 0, 0 );

    // 1st row...

    // First column...
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_TRAILING;

    pane.add( new JLabel( RB.getString( "rWhileStoring" ) ), gbc );

    // Second, third and fourth column...
    gbc.gridx = 1;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    pane.add( this.captureTerm, gbc );

    // Fifth & sixth column...
    gbc.gridx = 4;
    gbc.gridwidth = 2;
    gbc.fill = GridBagConstraints.NONE;

    pane.add( new JLabel( "" ), gbc );

    // 2nd row...

    // First column...
    gbc.gridx = 0;
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
    gbc.insets = emptyInsets;

    pane.add( new JLabel( RB.getString( "rIfTarget" ) ), gbc );

    // Second, third and fourth column...
    gbc.gridx = 1;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.BASELINE_TRAILING;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    pane.add( this.hitTerm, gbc );

    // Fifth column...
    gbc.gridx = 4;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;

    pane.add( new JLabel( RB.getString( "rOccurs" ) ), gbc );

    // Sixth column...
    gbc.gridx = 5;

    pane.add( this.occurenceCount, gbc );

    // Seventh column...
    gbc.gridx = 6;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;

    pane.add( new JLabel( RB.getString( "rSamples" ) ), gbc );

    // 3rd row...

    // First column...
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;
    gbc.insets = rowInsets;

    pane.add( new JLabel( "" ), gbc );

    // Second column...
    gbc.gridx = 1;
    gbc.anchor = GridBagConstraints.BASELINE_TRAILING;

    pane.add( new JLabel( RB.getString( "rOnHit" ) ), gbc );

    // Third column...
    gbc.gridx = 2;

    pane.add( this.setTrigger, gbc );

    // Fourth column...
    gbc.gridx = 3;

    pane.add( this.timerControl, gbc );

    // Fifth & sixth column...
    gbc.gridx = 4;
    gbc.gridwidth = 2;

    pane.add( new JLabel( "" ), gbc );

    // 4th row...

    // First column...
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.BASELINE_TRAILING;

    pane.add( new JLabel( RB.getString( "rElseOn" ) ), gbc );

    // Second, third and fourth column...
    gbc.gridx = 1;
    gbc.gridwidth = 3;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.BASELINE_LEADING;

    pane.add( this.elseTerm, gbc );

    // Fifth column...
    gbc.gridx = 4;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;

    pane.add( new JLabel( RB.getString( "rGotoLevel" ) ), gbc );

    // Sixth column...
    gbc.gridx = 5;

    pane.add( this.gotoLevel, gbc );

    return pane;
  }

  /**
   * @param aTriggerSum
   * @return
   */
  private String getTriggerSumString( final TriggerSum aTriggerSum )
  {
    return this.triggerSumStringifier.toString( aTriggerSum );
  }

  /**
   * Initializes all (sub)components used in this component.
   */
  private void initPanel()
  {
    final String modeStr = RB.getString( "r".concat( this.mode.name() ) );
    final Integer stateNr = Integer.valueOf( this.model.getStateNumber() );
    this.title = new JLabel( String.format( RB.getString( "rSequenceLevel" ), modeStr, stateNr ) );

    this.captureTerm = new JButton( getTriggerSumString( this.model.getCaptureTerms() ) );
    this.hitTerm = new JButton( getTriggerSumString( this.model.getHitTerms() ) );
    this.elseTerm = new JButton( getTriggerSumString( this.model.getElseTerms() ) );

    this.setTrigger = new JCheckBox( RB.getString( "rSetTrigger" ) );
    this.timerControl = new JButton( RB.getString( "rTimerControl" ) );

    this.occurenceCount = new JTextField( "1", 3 );
    this.gotoLevel = new JTextField( "" + ( stateNr.intValue() + 1 ), 3 );

    // Initialize listeners...
    this.setTrigger.addItemListener( new ItemListener()
    {
      @Override
      public void itemStateChanged( final ItemEvent aEvent )
      {
        JCheckBox source = ( JCheckBox )aEvent.getSource();
        TriggerSequencePanel.this.timerControl.setEnabled( !source.isSelected() );
      }
    } );
  }
}
