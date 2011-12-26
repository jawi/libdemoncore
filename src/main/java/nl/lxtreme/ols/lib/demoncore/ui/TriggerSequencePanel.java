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

import javax.swing.*;


/**
 * Denotes a single trigger sequence, which defined what to capture when, and on
 * what trigger conditions.
 */
public class TriggerSequencePanel extends JPanel
{
  // CONSTANTS

  private static final long serialVersionUID = 1L;

  // VARIABLES

  private final int level;

  private JButton captureTerm;
  private JButton hitTerm;
  private JButton elseTerm;
  private JButton setTrigger;
  private JButton timerControl;
  private JTextField occurenceCount;
  private JTextField gotoLevel;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerSequencePanel instance.
   * 
   * @param aLevel
   *          the trigger sequence level, >= 0 && < 16.
   */
  public TriggerSequencePanel( final int aLevel )
  {
    super( new GridBagLayout() );

    this.level = aLevel;

    initPanel();
    buildPanel();
  }

  // METHODS

  /**
   * Builds this panel by placing all components on it.
   */
  private void buildPanel()
  {
    JPanel pane = new JPanel( new GridBagLayout() );

    GridBagConstraints gbc = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
        GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 0, 0 );

    // 1st row...

    // First column...
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.EAST;

    pane.add( new JLabel( "While storing" ), gbc );

    // Second, third and fourth column...
    gbc.gridx = 1;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
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
    gbc.anchor = GridBagConstraints.EAST;

    pane.add( new JLabel( "If target" ), gbc );

    // Second, third and fourth column...
    gbc.gridx = 1;
    gbc.gridwidth = 3;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;

    pane.add( this.hitTerm, gbc );

    // Fifth column...
    gbc.gridx = 4;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;

    pane.add( new JLabel( "Occurs" ), gbc );

    // Sixth column...
    gbc.gridx = 5;

    pane.add( this.occurenceCount, gbc );

    // 3rd row...

    // First column...
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.EAST;

    pane.add( new JLabel( "" ), gbc );

    // Second column...
    gbc.gridx = 1;
    gbc.anchor = GridBagConstraints.WEST;

    pane.add( new JLabel( "On hit" ), gbc );

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
    gbc.anchor = GridBagConstraints.EAST;

    pane.add( new JLabel( "Else on" ), gbc );

    // Second, third and fourth column...
    gbc.gridx = 1;
    gbc.gridwidth = 3;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    gbc.anchor = GridBagConstraints.WEST;

    pane.add( this.elseTerm, gbc );

    // Fifth column...
    gbc.gridx = 4;
    gbc.gridwidth = 1;
    gbc.fill = GridBagConstraints.NONE;

    pane.add( new JLabel( "Goto level" ), gbc );

    // Sixth column...
    gbc.gridx = 5;

    pane.add( this.gotoLevel, gbc );

    gbc.gridx = 0;
    gbc.gridy = 0;
    add( pane );
  }

  /**
   * Initializes all (sub)components used in this component.
   */
  private void initPanel()
  {
    this.captureTerm = new JButton( "..." );
    this.hitTerm = new JButton( "..." );
    this.elseTerm = new JButton( "..." );

    this.setTrigger = new JButton( "Set trigger" );
    this.timerControl = new JButton( "Timer control" );

    this.occurenceCount = new JTextField( "1" );
    this.gotoLevel = new JTextField( "" + ( this.level + 1 ) );
  }
}
