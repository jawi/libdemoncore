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

import javax.swing.*;

import nl.lxtreme.ols.lib.demoncore.*;


/**
 * 
 */
public class TriggerSumEditor extends JDialog
{
  // CONSTANTS

  private static final long serialVersionUID = 1L;

  // VARIABLES

  private final TriggerMode mode;
  private final TriggerSum model;

  private boolean dialogResult;
  private TriggerSumPanel triggerSumPane;
  private JPanel buttonPane;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerSumEditor instance.
   */
  public TriggerSumEditor( final TriggerMode aMode, final TriggerSum aTriggerSum )
  {
    super( null /* XXX */, DEFAULT_MODALITY_TYPE );

    this.mode = aMode;
    this.model = new TriggerSum( aTriggerSum );

    initDialog();
    buildDialog();
  }

  // METHODS

  /**
   * Returns the current value of model.
   * 
   * @return the model
   */
  public final TriggerSum getTriggerSum()
  {
    return this.model;
  }

  /**
   * @return <code>true</code> if this dialog is acknowledged,
   *         <code>false</code> if this dialog is cancelled.
   */
  public boolean showDialog()
  {
    this.dialogResult = false;
    setVisible( true );
    return this.dialogResult;
  }

  /**
   * Builds this dialog.
   */
  private void buildDialog()
  {
    final JPanel contentPane = new JPanel( new BorderLayout() );
    setContentPane( contentPane );

    contentPane.setBorder( BorderFactory.createEmptyBorder( 8, 8, 8, 8 ) );
    contentPane.add( this.triggerSumPane, BorderLayout.CENTER );
    contentPane.add( this.buttonPane, BorderLayout.SOUTH );

    pack();
  }

  /**
   * Initializes this dialog.
   */
  private void initDialog()
  {
    this.triggerSumPane = new TriggerSumPanel( this.mode, this.model );

    JButton cancelButton = new JButton( "Cancel" );
    cancelButton.addActionListener( new ActionListener()
    {
      @Override
      public void actionPerformed( final ActionEvent aEvent )
      {
        TriggerSumEditor.this.dialogResult = false;
        setVisible( false );
      }
    } );

    JButton okButton = new JButton( "Ok" );
    okButton.addActionListener( new ActionListener()
    {
      @Override
      public void actionPerformed( final ActionEvent aEvent )
      {
        TriggerSumEditor.this.dialogResult = true;
        setVisible( false );
      }
    } );

    this.buttonPane = new JPanel();
    this.buttonPane.add( cancelButton, BorderLayout.LINE_END );
    this.buttonPane.add( okButton, BorderLayout.LINE_END );
  }
}
