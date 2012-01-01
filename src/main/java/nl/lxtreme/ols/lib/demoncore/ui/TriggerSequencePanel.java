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
import java.util.*;

import javax.swing.*;

import nl.lxtreme.ols.lib.demoncore.*;


/**
 * Denotes a single trigger sequence, which defined what to capture when, and on
 * what trigger conditions.
 */
public class TriggerSequencePanel extends JPanel
{
  // INNER TYPES

  /**
   * Provides a trigger sum edit action.
   */
  static final class TriggerSumEditAction extends AbstractAction
  {
    // CONSTANTS

    private static final long serialVersionUID = 1L;

    // VARIABLES

    private final TriggerStringifier triggerStringifier;
    private final TriggerStateTerm stateTermType;
    private final TriggerSequenceState triggerSequence;
    private final TriggerMode mode;

    // CONSTRUCTORS

    /**
     * Creates a new {@link TriggerSumEditAction} instance.
     */
    public TriggerSumEditAction( final TriggerStateTerm aStateTermType, final TriggerMode aMode,
        final TriggerSequenceState aTriggerSequenceState )
    {
      this.stateTermType = aStateTermType;
      this.mode = aMode;
      this.triggerSequence = aTriggerSequenceState;

      this.triggerStringifier = new TriggerStringifier();

      updateName();
    }

    // METHODS

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed( final ActionEvent aEvent )
    {
      final TriggerSumEditor editor = new TriggerSumEditor( this.mode, getTriggerSum() );
      if ( editor.showDialog() )
      {
        setTriggerSum( editor.getTriggerSum() );
      }

      updateName();
    }

    /**
     * Returns the current trigger sum.
     * 
     * @return a trigger sum, never <code>null</code>.
     */
    private TriggerSum getTriggerSum()
    {
      return this.triggerSequence.getTriggerSum( this.stateTermType );
    }

    /**
     * Sets the trigger sum to the one given.
     * 
     * @param aSum
     *          the trigger sum to set, cannot be <code>null</code>.
     */
    private void setTriggerSum( final TriggerSum aSum )
    {
      this.triggerSequence.setTriggerSum( this.stateTermType, aSum );
    }

    /**
     * Updates the name of this action according to the human-readable
     * representation of the contained trigger sum.
     */
    private void updateName()
    {
      putValue( NAME, this.triggerStringifier.toString( getTriggerSum() ) );
    }
  }

  // CONSTANTS

  private static final long serialVersionUID = 1L;

  private static final ResourceBundle RB = ResourceBundle.getBundle( DemonCore.class.getName() );

  // VARIABLES

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
   * Initializes all (sub)components used in this component.
   */
  private void initPanel()
  {
    final String modeStr = RB.getString( "r".concat( this.mode.name() ) );
    final Integer stateNr = Integer.valueOf( this.model.getStateNumber() );
    this.title = new JLabel( String.format( RB.getString( "rSequenceLevel" ), modeStr, stateNr ) );

    this.captureTerm = new JButton( new TriggerSumEditAction( TriggerStateTerm.CAPTURE, this.mode, this.model ) );
    this.hitTerm = new JButton( new TriggerSumEditAction( TriggerStateTerm.HIT, this.mode, this.model ) );
    this.elseTerm = new JButton( new TriggerSumEditAction( TriggerStateTerm.ELSE, this.mode, this.model ) );

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
