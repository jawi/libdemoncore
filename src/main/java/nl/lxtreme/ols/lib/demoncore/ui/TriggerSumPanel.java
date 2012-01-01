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
 * Provides a graphical representation of a single trigger sums. A trigger sum
 * defines how trigger terms, range detectors and/or timers play together to
 * form a logic sum.
 */
public class TriggerSumPanel extends JPanel
{
  // INNER TYPES

  /**
   * Provides a component panel that can be enabled or disabled.
   */
  static class InputTermPanel extends JPanel implements ItemListener
  {
    // CONSTANTS

    private static final String PROPERTY_OLD_STATE = "oldState";
    private static final String PREFIX = "\u00AC";

    private static final long serialVersionUID = 1L;

    // VARIABLES

    private final AbstractTriggerTerm term;
    private final JCheckBox state;
    private final JToggleButton inversionButton;

    // CONSTRUCTORS

    /**
     * Creates a new InputTermPanel instance.
     */
    public InputTermPanel( final AbstractTriggerTerm aTerm )
    {
      super( new GridBagLayout() );

      this.term = aTerm;
      this.state = new JCheckBox();
      this.inversionButton = new JToggleButton();

      // Initial state...
      boolean isEnabled = !aTerm.isDisabled();

      this.inversionButton.setSelected( aTerm.isInverted() );
      this.inversionButton.setEnabled( isEnabled );
      this.state.setSelected( isEnabled );

      this.inversionButton.setText( getInversionButtonText() );

      // Listeners...
      this.inversionButton.addItemListener( this );
      this.state.addItemListener( this );

      GridBagConstraints gbc = new GridBagConstraints( 0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.BASELINE,
          GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 0, 0 );

      add( this.state, gbc );

      gbc.gridx = 1;
      gbc.weightx = 1.0;
      gbc.fill = GridBagConstraints.HORIZONTAL;

      add( this.inversionButton, gbc );
    }

    // METHODS

    /**
     * {@inheritDoc}
     */
    @Override
    public void itemStateChanged( final ItemEvent aEvent )
    {
      final Object source = aEvent.getSource();
      if ( source == this.inversionButton )
      {
        // Toggle between inversion and not...
        this.term.setState( this.inversionButton.isSelected() ? TriggerInputState.ENABLED_INVERTED
            : TriggerInputState.ENABLED );
      }
      else if ( source == this.state )
      {
        this.inversionButton.setEnabled( this.state.isSelected() );
        if ( this.state.isSelected() )
        {
          // Restore previous state...
          TriggerInputState termState = ( TriggerInputState )getClientProperty( PROPERTY_OLD_STATE );
          if ( termState == null )
          {
            termState = TriggerInputState.ENABLED;
          }

          this.term.setState( termState );
        }
        else
        {
          // Keep track of the previous state...
          putClientProperty( PROPERTY_OLD_STATE, this.term.getState() );

          this.term.setDisabled();
        }
      }

      this.inversionButton.setText( getInversionButtonText() );
    }

    /**
     * @return
     */
    private String getInversionButtonText()
    {
      final String text = TriggerStringifier.asString( this.term );
      if ( this.term.isInverted() )
      {
        return PREFIX.concat( text );
      }
      return text;
    }
  }

  /**
   * A small panel with the trigger operation combobox.
   */
  static class OperationPanel extends JPanel implements ItemListener
  {
    // CONSTANTS

    private static final long serialVersionUID = 1L;

    // VARIABLES

    private final AbstractTriggerOperationTerm operationTerm;
    private final JComboBox comboBox;

    // CONSTRUCTORS

    /**
     * Creates a new TriggerSumPanel.OperationPanel instance.
     */
    public OperationPanel( final AbstractTriggerOperationTerm aOperationTerm )
    {
      this.operationTerm = aOperationTerm;

      this.comboBox = new JComboBox( EnumSet.range( TriggerOperation.AND, TriggerOperation.NXOR ).toArray() );
      this.comboBox.setSelectedItem( aOperationTerm.getOperation() );
      this.comboBox.addItemListener( this );

      add( this.comboBox, BorderLayout.CENTER );
    }

    // METHODS

    /**
     * {@inheritDoc}
     */
    @Override
    public void itemStateChanged( final ItemEvent aEvent )
    {
      final JComboBox cb = ( JComboBox )aEvent.getSource();
      this.operationTerm.setOperation( ( TriggerOperation )cb.getSelectedItem() );
    }
  }

  /**
   * Denotes a single UI element which (optionally) connects back to other UI
   * elements.
   */
  static final class UIElement
  {
    final JComponent component;
    final UIElement[] backLinks;

    /**
     * Creates a new TriggerSumPanel.UIElement instance.
     */
    public UIElement( final JComponent aComponent, final UIElement... aBackLinks )
    {
      this.component = aComponent;
      this.backLinks = Arrays.copyOf( aBackLinks, aBackLinks.length );
    }

    /**
     * @return <code>true</code> if there are backlinks to other UI elements,
     *         <code>false</code> otherwise.
     */
    public final boolean hasBackLinks()
    {
      return ( this.backLinks != null ) && ( this.backLinks.length > 0 );
    }
  }

  // CONSTANTS

  private static final long serialVersionUID = 1L;

  // VARIABLES

  private final TriggerSum model;

  private UIElement[] inputTerms;
  private UIElement[] pairTerms;
  private UIElement[] midTerms;
  private UIElement finalTerm;

  // CONSTRUCTORS

  /**
   * Creates a new {@link TriggerSumPanel} instance.
   * 
   * @param aMode
   *          the trigger mode, whether we're displaying states or timing
   *          values, cannot be <code>null</code>;
   * @param aTriggerSum
   *          the trigger sum to edit in this panel. Will be changed by this
   *          panel, cannot be <code>null</code>.
   */
  public TriggerSumPanel( final TriggerMode aMode, final TriggerSum aTriggerSum )
  {
    super( new GridBagLayout() );

    this.model = aTriggerSum;

    initPanel();
    buildPanel();
  }

  // METHODS

  /**
   * {@inheritDoc}
   */
  @Override
  public Dimension getPreferredSize()
  {
    final Dimension preferredSize = super.getPreferredSize();
    // Enlarge the width of this component by some pixels to ensure there's
    // space left for the tree-lines in between the various components...
    preferredSize.width += 160;
    return preferredSize;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void paintComponent( final Graphics aCanvas )
  {
    super.paintComponent( aCanvas );

    final Graphics2D canvas = ( Graphics2D )aCanvas.create();
    try
    {
      // Draw all the intermediary lines between the various stages (tail
      // recursive)...
      drawTreeConnections( canvas, this.finalTerm );
    }
    finally
    {
      canvas.dispose();
    }
  }

  /**
   * Builds the panel by placing all components to their correct location.
   */
  private void buildPanel()
  {
    GridBagConstraints gbc = new GridBagConstraints( 0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER,
        GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 0, 0 );

    // First column...
    gbc.gridx = 0;

    for ( UIElement inputStage : this.inputTerms )
    {
      gbc.gridy++;
      add( inputStage.component, gbc );
    }

    // Second column...
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridheight = 2;

    for ( UIElement pairValueStage : this.pairTerms )
    {
      add( pairValueStage.component, gbc );
      gbc.gridy += 2;
    }

    // Third column...
    gbc.gridx = 2;
    gbc.gridy = 4;

    for ( UIElement quadValueStage : this.midTerms )
    {
      add( quadValueStage.component, gbc );
      gbc.gridy += 8;
    }

    // Fourth column...
    gbc.gridx = 3;
    gbc.gridy = 8;

    add( this.finalTerm.component, gbc );
  }

  /**
   * Draws orthogonal lines between the given component and its "back linked"
   * components.
   * <p>
   * This method will (tail) recursively call itself to walk back through each
   * back linked component.
   * </p>
   * 
   * @param aCanvas
   *          the canvas to paint on, cannot be <code>null</code>;
   * @param aUIElement
   *          the component to draw the lines from, cannot be <code>null</code>.
   */
  private void drawTreeConnections( final Graphics2D aCanvas, final UIElement aUIElement )
  {
    final UIElement[] backlinks = aUIElement.backLinks;
    final int iV = backlinks.length;
    if ( iV < 2 )
    {
      return;
    }

    final int padX = 2;

    // Determine the minimum distance between from components and the to
    // component...
    int distX = Integer.MAX_VALUE;
    int distY = aUIElement.component.getHeight();
    for ( UIElement fromComp : backlinks )
    {
      distX = Math.min( distX, getDistanceBetween( fromComp, aUIElement ) );
    }

    final double dX = Math.floor( ( iV / 2.0 ) + 1 );
    final double oX = ( iV - 1 ) / 2.0;
    final double dY = iV + 1;

    final int x3 = aUIElement.component.getX() - padX;
    final int y3 = aUIElement.component.getY() - 1; // XXX

    for ( int i = 0; i < backlinks.length; i++ )
    {
      final UIElement backlinkUIElement = backlinks[i];
      final Rectangle fromBounds = backlinkUIElement.component.getBounds();

      final int offsetX = ( int )( distX * ( Math.floor( Math.abs( i - oX ) + 0.5 ) / dX ) );
      final int offsetY = ( int )( distY * ( ( i + 1 ) / dY ) );

      final int x1 = fromBounds.x + fromBounds.width + padX;
      final int x2 = ( x1 + offsetX );
      final int y1 = ( int )fromBounds.getCenterY();
      final int y2 = y3 + offsetY;

      aCanvas.drawLine( x1, y1, x2, y1 );
      aCanvas.drawLine( x2, y1, x2, y2 );
      aCanvas.drawLine( x2, y2, x3, y2 );

      // Tail recursion: draw the connections to the backlinks...
      drawTreeConnections( aCanvas, backlinkUIElement );
    }
  }

  /**
   * @param aComponent1
   * @param aComponent2
   * @return
   */
  private int getDistanceBetween( final UIElement aComponent1, final UIElement aComponent2 )
  {
    Rectangle bounds1 = aComponent1.component.getBounds();
    Rectangle bounds2 = aComponent2.component.getBounds();
    assert !bounds1.intersects( bounds2 );

    if ( bounds2.x > bounds1.x )
    {
      return bounds2.x - ( bounds1.x + bounds1.width );
    }

    return bounds1.x - ( bounds2.x + bounds2.width );
  }

  /**
   * Initializes this panel by creating all necessary component instances.
   */
  private void initPanel()
  {
    TriggerFinalTerm ft = this.model.getFinalTerm();

    TriggerMidTerm[] mts = { ft.getTermA(), ft.getTermB() };
    TriggerPairTerm[] pts = { mts[0].getTermA(), mts[0].getTermB(), mts[0].getTermC(), mts[0].getTermD(), //
        mts[1].getTermA(), mts[1].getTermB(), mts[1].getTermC(), mts[1].getTermD() };
    AbstractTriggerTerm[] tts = { pts[0].getTermA(), pts[0].getTermB(), pts[1].getTermA(), pts[1].getTermB(),
        pts[2].getTermA(), pts[2].getTermB(), pts[3].getTermA(), pts[3].getTermB(), pts[4].getTermA(),
        pts[4].getTermB(), pts[5].getTermA(), pts[5].getTermB(), pts[6].getTermA(), pts[6].getTermB(),
        pts[7].getTermA(), pts[7].getTermB() };

    Dimension maxSize = new Dimension( 0, 0 );

    this.inputTerms = new UIElement[tts.length];
    for ( int i = 0; i < this.inputTerms.length; i++ )
    {
      this.inputTerms[i] = new UIElement( new InputTermPanel( tts[i] ) );
    }

    this.pairTerms = new UIElement[pts.length];
    for ( int i = 0, j = 0; j < this.pairTerms.length; i += 2, j++ )
    {
      this.pairTerms[j] = new UIElement( new OperationPanel( pts[j] ), this.inputTerms[i], this.inputTerms[i + 1] );
    }

    this.midTerms = new UIElement[mts.length];
    for ( int i = 0, j = 0; j < this.midTerms.length; i += 4, j++ )
    {
      this.midTerms[j] = new UIElement( new OperationPanel( mts[j] ), this.pairTerms[i], this.pairTerms[i + 1],
          this.pairTerms[i + 2], this.pairTerms[i + 3] );
    }

    this.finalTerm = new UIElement( new OperationPanel( ft ), this.midTerms );

    // Make all input stage buttons equally wide...
    for ( UIElement inputTerm : this.inputTerms )
    {
      Dimension size = inputTerm.component.getMinimumSize();
      if ( size.width > maxSize.width )
      {
        maxSize = size;
      }
    }
    for ( UIElement inputStage : this.inputTerms )
    {
      inputStage.component.setMinimumSize( maxSize );
      inputStage.component.setPreferredSize( maxSize );
    }
  }
}
