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
    // TODO Auto-generated method stub
  }

  /**
   * Initializes all (sub)components used in this component.
   */
  private void initPanel()
  {
    // TODO Auto-generated method stub
  }
}
