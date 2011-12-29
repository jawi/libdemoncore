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
import java.beans.*;
import java.util.*;

import javax.swing.*;


/**
 * Provides a graphical representation of a single trigger sums. A trigger sum
 * defines how trigger terms, range detectors and/or timers play together to
 * form a logic sum.
 */
public class TriggerSumPanel extends JPanel
{
  // INNER TYPES

  static class ButtonUIElement extends UIElement<StatePanel>
  {
    /**
     * Creates a new ButtonUIElement instance.
     */
    public ButtonUIElement( final StatePanel aComponent, final UIElement<?>... aBackLinks )
    {
      super( aComponent, aBackLinks );
    }
  }

  /**
   * Denotes a single UI element which (optionally) connects back to other UI
   * elements.
   */
  static class ComboBoxUIElement extends UIElement<JComboBox>
  {
    /**
     * Creates a new TriggerSumPanel.ComboBoxUIElement instance.
     */
    public ComboBoxUIElement( final JComboBox aComponent, final UIElement<?>... aBackLinks )
    {
      super( aComponent, aBackLinks );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange( final PropertyChangeEvent aEvent )
    {
      if ( isInputStagePropertyChangeEvent( aEvent ) )
      {
        boolean aEnabled = isButtonEnabled( 0 );
        boolean bEnabled = isButtonEnabled( 1 );

        String selectedKey;
        if ( aEnabled )
        {
          if ( bEnabled )
          {
            selectedKey = DemonCore.rOR;
          }
          else
          {
            selectedKey = DemonCore.rA_ONLY;
          }
        }
        else
        {
          if ( bEnabled )
          {
            selectedKey = DemonCore.rB_ONLY;
          }
          else
          {
            selectedKey = DemonCore.rNOP;
          }
        }

        this.component.setSelectedItem( RB.getString( selectedKey ) );
      }
    }

    /**
     * @param aIndex
     * @return
     */
    private boolean isButtonEnabled( final int aIndex )
    {
      JComponent comp = this.backLinks[aIndex].component;
      assert comp instanceof StatePanel : "Not a state panel?!";
      return ( ( StatePanel )comp ).isStateEnabled();
    }

    /**
     * @param aEvent
     * @return
     */
    private boolean isInputStagePropertyChangeEvent( final PropertyChangeEvent aEvent )
    {
      boolean isRightProperty = aEvent.getPropertyName().startsWith( StatePanel.STATE_PROPERTY_NAME );
      boolean isInputPairStage = Boolean.TRUE.equals( this.component.getClientProperty( PROPERTY_IS_INPUT_STAGE ) );
      return isRightProperty && isInputPairStage;
    }
  }

  /**
   * Provides a component panel that can be enabled or disabled.
   */
  static class StatePanel extends JPanel
  {
    private static final long serialVersionUID = 1L;

    static final String STATE_PROPERTY_NAME = "stateChange";

    private final JCheckBox state;
    private final JComponent innerComponent;

    /**
     * Creates a new TriggerSumPanel.StatePanel instance.
     */
    public StatePanel( final JComponent aInnerComponent )
    {
      super( new GridBagLayout() );

      this.innerComponent = aInnerComponent;

      this.state = new JCheckBox();
      this.state.addActionListener( new ActionListener()
      {
        @Override
        public void actionPerformed( final ActionEvent aEvent )
        {
          final JCheckBox cb = ( JCheckBox )aEvent.getSource();
          StatePanel.this.innerComponent.setEnabled( cb.isSelected() );

          StatePanel.this.firePropertyChange( STATE_PROPERTY_NAME, !cb.isSelected(), cb.isSelected() );
        }
      } );
      // Initial state...
      this.innerComponent.setEnabled( false );
      this.state.setSelected( false );

      GridBagConstraints gbc = new GridBagConstraints( 0, 0, 1, 1, 0.0, 1.0, GridBagConstraints.BASELINE,
          GridBagConstraints.NONE, new Insets( 0, 0, 0, 0 ), 0, 0 );

      add( this.state, gbc );

      gbc.gridx = 1;
      gbc.weightx = 1.0;
      gbc.fill = GridBagConstraints.HORIZONTAL;

      add( this.innerComponent, gbc );
    }

    /**
     * @return
     */
    public boolean isStateEnabled()
    {
      return this.state.isSelected();
    }
  }

  /**
   * Denotes a single UI element which (optionally) connects back to other UI
   * elements.
   */
  static abstract class UIElement<TYPE extends JComponent> implements PropertyChangeListener
  {
    protected final TYPE component;
    protected final UIElement<?>[] backLinks;

    /**
     * Creates a new TriggerSumPanel.UIElement instance.
     */
    public UIElement( final TYPE aComponent, final UIElement<?>... aBackLinks )
    {
      this.component = aComponent;
      this.backLinks = Arrays.copyOf( aBackLinks, aBackLinks.length );
      for ( UIElement<?> backLink : this.backLinks )
      {
        backLink.component.addPropertyChangeListener( this );
      }
    }

    /**
     * @return <code>true</code> if there are backlinks to other UI elements,
     *         <code>false</code> otherwise.
     */
    public final boolean hasBackLinks()
    {
      return ( this.backLinks != null ) && ( this.backLinks.length > 0 );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange( final PropertyChangeEvent aEvent )
    {
      // NO-op
    }
  }

  // CONSTANTS

  private static final long serialVersionUID = 1L;

  private static final ResourceBundle RB = ResourceBundle.getBundle( "nl.lxtreme.ols.lib.demoncore.ui.DemonCore" );

  private static final String PROPERTY_IS_INPUT_STAGE = "IsInputStage";

  private static final String[] LOGICAL_OPERATIONS_INPUT = RB.getStringArray( "rInputLogicalOperations" );
  private static final String[] LOGICAL_OPERATIONS_OTHER = RB.getStringArray( "rOtherLogicalOperations" );
  private static final String[] INPUT_STAGE_NAMES = RB.getStringArray( "rInputStageNames" );

  // VARIABLES

  private final TriggerMode mode;
  private UIElement<?>[] inputStages;
  private UIElement<?>[] pairValueStages;
  private UIElement<?>[] quadValueStages;
  private UIElement<?> endStage;

  // CONSTRUCTORS

  /**
   * Creates a new {@link TriggerSumPanel} instance.
   * 
   * @param aMode
   *          the trigger mode, whether we're displaying states or timing
   *          values.
   */
  public TriggerSumPanel( final TriggerMode aMode )
  {
    super( new GridBagLayout() );

    this.mode = aMode;

    initPanel();
    buildPanel();
  }

  // METHODS

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
      drawTreeConnections( canvas, this.endStage );
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

    for ( UIElement<?> inputStage : this.inputStages )
    {
      gbc.gridy++;
      add( inputStage.component, gbc );
    }

    // Second column...
    gbc.gridx = 1;
    gbc.gridy = 1;
    gbc.gridheight = 2;

    for ( UIElement<?> pairValueStage : this.pairValueStages )
    {
      add( pairValueStage.component, gbc );
      gbc.gridy += 2;
    }

    // Third column...
    gbc.gridx = 2;
    gbc.gridy = 4;

    for ( UIElement<?> quadValueStage : this.quadValueStages )
    {
      add( quadValueStage.component, gbc );
      gbc.gridy += 8;
    }

    // Fourth column...
    gbc.gridx = 3;
    gbc.gridy = 8;

    add( this.endStage.component, gbc );
  }

  /**
   * @param aIndex
   * @return
   */
  private JComboBox createLogicalOperator( final boolean aInputStage )
  {
    JComboBox result = new JComboBox( aInputStage ? LOGICAL_OPERATIONS_INPUT : LOGICAL_OPERATIONS_OTHER );
    result.putClientProperty( PROPERTY_IS_INPUT_STAGE, Boolean.valueOf( aInputStage ) );
    result.setSelectedIndex( 2 );
    return result;
  }

  /**
   * @param aCanvas
   * @param aToComponent
   * @param aFromComponents
   */
  private void drawTreeConnections( final Graphics2D aCanvas, final UIElement<?> aToComponent )
  {
    final UIElement<?>[] fromComponents = aToComponent.backLinks;
    final int iV = fromComponents.length;
    if ( iV < 2 )
    {
      return;
    }

    final int padX = 2;

    // Determine the minimum distance between from components and the to
    // component...
    int distX = Integer.MAX_VALUE;
    int distY = aToComponent.component.getHeight();
    for ( UIElement<?> fromComp : fromComponents )
    {
      distX = Math.min( distX, getDistanceBetween( fromComp, aToComponent ) );
    }

    final double dX = Math.floor( ( iV / 2.0 ) + 1 );
    final double oX = ( iV - 1 ) / 2.0;
    final double dY = iV + 1;

    final int x3 = aToComponent.component.getX() - padX;
    final int y3 = aToComponent.component.getY() - 1; // XXX

    for ( int i = 0; i < fromComponents.length; i++ )
    {
      final UIElement<?> fromComponent = fromComponents[i];
      final Rectangle fromBounds = fromComponent.component.getBounds();

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
      drawTreeConnections( aCanvas, fromComponent );
    }
  }

  /**
   * @param aComponent1
   * @param aComponent2
   * @return
   */
  private int getDistanceBetween( final UIElement<?> aComponent1, final UIElement<?> aComponent2 )
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
    final int inputCount = INPUT_STAGE_NAMES.length;
    assert ( inputCount % 2 ) == 0 : "Internal error: input stage names not a multiple of two!";

    Dimension maxSize = new Dimension( 0, 0 );
    this.inputStages = new UIElement[inputCount];
    for ( int i = 0; i < inputCount; i++ )
    {
      JToggleButton toggleButton = new JToggleButton( INPUT_STAGE_NAMES[i] );
      toggleButton.addItemListener( new ItemListener()
      {
        private static final String PREFIX = "\u2260";

        /**
         * {@inheritDoc}
         */
        @Override
        public void itemStateChanged( final ItemEvent aEvent )
        {
          JToggleButton tb = ( JToggleButton )aEvent.getSource();

          String name = tb.getText();
          if ( !tb.isSelected() )
          {
            tb.setText( name.substring( 1 ) );
          }
          else
          {
            tb.setText( PREFIX.concat( name ) );
          }
        }
      } );

      StatePanel component = new StatePanel( toggleButton );

      Dimension size = component.getMinimumSize();
      if ( size.width > maxSize.width )
      {
        maxSize = size;
      }
      this.inputStages[i] = new ButtonUIElement( component );
    }

    final int pairCount = inputCount / 2;
    this.pairValueStages = new UIElement[pairCount];
    for ( int i = 0, j = 0; i < pairCount; i++ )
    {
      JComboBox logicalOp = createLogicalOperator( true );

      this.pairValueStages[i] = new ComboBoxUIElement( logicalOp, this.inputStages[j++], this.inputStages[j++] );
    }

    final int quadCount = pairCount / 4;
    this.quadValueStages = new UIElement[quadCount];
    for ( int i = 0, j = 0; i < quadCount; i++ )
    {
      this.quadValueStages[i] = new ComboBoxUIElement( createLogicalOperator( false ), this.pairValueStages[j++],
          this.pairValueStages[j++], this.pairValueStages[j++], this.pairValueStages[j++] );
    }

    this.endStage = new ComboBoxUIElement( createLogicalOperator( false ), this.quadValueStages[0],
        this.quadValueStages[1] );

    // Make all input stage buttons equally wide...
    for ( UIElement<?> inputStage : this.inputStages )
    {
      inputStage.component.setMinimumSize( maxSize );
      inputStage.component.setPreferredSize( maxSize );
    }
  }
}
