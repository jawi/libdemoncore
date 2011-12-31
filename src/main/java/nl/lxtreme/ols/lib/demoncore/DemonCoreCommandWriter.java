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
 * Provides an extension to the {@link SumpCommandWriter} that allows the
 * "Demon Core" to be configured as well.
 */
public class DemonCoreCommandWriter
{
  // INNER TYPES

  /**
   * Writer for a trigger sum or sequence.
   */
  final class TriggerSumSequenceWriter implements ITriggerVisitor
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public void visit( final AbstractTriggerTerm aTerm ) throws IOException
    {
      // NO-op
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit( final TriggerFinalTerm aTerm ) throws IOException
    {
      writeChain( FINAL_VALUE[aTerm.getOffset()] );
      writeChain( ( MID_VALUE[aTerm.getTermB().getOffset()] << 16 ) | MID_VALUE[aTerm.getTermA().getOffset()] );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit( final TriggerMidTerm aTerm ) throws IOException
    {
      writeChain( ( PAIR_VALUE[aTerm.getTermD().getOffset()] << 16 ) | PAIR_VALUE[aTerm.getTermC().getOffset()] );
      writeChain( ( PAIR_VALUE[aTerm.getTermB().getOffset()] << 16 ) | PAIR_VALUE[aTerm.getTermA().getOffset()] );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit( final TriggerPairTerm aTerm ) throws IOException
    {
      // NO-op
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit( final TriggerSequenceState aTriggerSequenceState ) throws IOException
    {
      // Select the correct LUT chain...
      writeSelect( aTriggerSequenceState.getStateNumber() & TRIGSTATE_STATENUM_MASK );

      // Build the actual chain data...
      int value = ( ( aTriggerSequenceState.getElseState() & TRIGSTATE_STATENUM_MASK ) << TRIGSTATE_ELSE_BITOFS )
          | ( aTriggerSequenceState.getOccurrenceCount() & TRIGSTATE_OBTAIN_MASK );
      if ( aTriggerSequenceState.isLastState() )
      {
        value |= TRIGSTATE_LASTSTATE;
      }
      if ( aTriggerSequenceState.isRaiseTrigger() )
      {
        value |= TRIGSTATE_TRIGGER_FLAG;
      }
      if ( ( aTriggerSequenceState.getStartTimer() & 1 ) != 0 )
      {
        value |= TRIGSTATE_START_TIMER0;
      }
      if ( ( aTriggerSequenceState.getStartTimer() & 2 ) != 0 )
      {
        value |= TRIGSTATE_START_TIMER1;
      }
      if ( ( aTriggerSequenceState.getStopTimer() & 1 ) != 0 )
      {
        value |= TRIGSTATE_STOP_TIMER0;
      }
      if ( ( aTriggerSequenceState.getStopTimer() & 2 ) != 0 )
      {
        value |= TRIGSTATE_STOP_TIMER1;
      }
      if ( ( aTriggerSequenceState.getClearTimer() & 1 ) != 0 )
      {
        value |= TRIGSTATE_CLEAR_TIMER0;
      }
      if ( ( aTriggerSequenceState.getClearTimer() & 2 ) != 0 )
      {
        value |= TRIGSTATE_CLEAR_TIMER1;
      }

      writeChain( value );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit( final TriggerSum aSum ) throws IOException
    {
      writeSelect( aSum.getOffset() );
    }
  }

  /**
   * Writer for trigger terms.
   */
  final class TriggerTermWriter implements ITriggerVisitor
  {
    /**
     * {@inheritDoc}
     */
    @Override
    public void visit( final AbstractTriggerTerm aTerm ) throws IOException
    {
      final TriggerTermType termType = aTerm.getType();
      switch ( termType )
      {
        case TERM_A:
        case TERM_B:
        case TERM_C:
        case TERM_D:
        case TERM_E:
        case TERM_F:
        case TERM_G:
        case TERM_H:
        case TERM_I:
        case TERM_J:
          writeTerm( ( TriggerTerm )aTerm );
          break;

        case TERM_EDGE1:
        case TERM_EDGE2:
          writeEdge( ( TriggerEdgeDetector )aTerm );
          break;

        case TERM_RANGE1:
        case TERM_RANGE2:
          writeRange( ( TriggerRangeDetector )aTerm );
          break;

        case TERM_TIMER1:
        case TERM_TIMER2:
          writeTriggerTimer( ( TriggerTimer )aTerm );
          break;
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit( final TriggerFinalTerm aTerm ) throws IOException
    {
      // NO-op
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit( final TriggerMidTerm aTerm ) throws IOException
    {
      // NO-op
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit( final TriggerPairTerm aTerm ) throws IOException
    {
      // NO-op
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit( final TriggerSequenceState aTriggerSequenceState ) throws IOException
    {
      // NO-op
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit( final TriggerSum aSum ) throws IOException
    {
      // NO-op
    }

    /**
     * Setup LUT's for Edge Detectors.
     * 
     * @param aEdgeDetector
     *          selects which edge we want to select & write trigger data for.
     * @throws IOException
     *           in case of I/O problems.
     */
    private void writeEdge( final TriggerEdgeDetector aEdgeDetector ) throws IOException
    {
      writeSelect( aEdgeDetector.getType().getLutChainAddress() );

      int lutvalue = 0;

      int aNeitherEdge = aEdgeDetector.getNoEdgeMask();
      int aFallingEdge = aEdgeDetector.getFallingEdgeMask();
      int aRisingEdge = aEdgeDetector.getRisingEdgeMask();

      long bitmask = 0x80000000L;
      for ( int i = 0; i < 16; i++ )
      {
        // Evaluate indata bit1...
        if ( ( aNeitherEdge != 0 ) && ( bitmask != 0 ) )
        {
          lutvalue |= EDGE_NEITHER1;
        }
        else
        {
          if ( ( aRisingEdge & bitmask ) != 0 )
          {
            lutvalue |= EDGE_RISE1;
          }
          if ( ( aFallingEdge & bitmask ) != 0 )
          {
            lutvalue |= EDGE_FALL1;
          }
        }
        bitmask >>>= 1;

        // Evaluate indata bit0...
        if ( ( aNeitherEdge != 0 ) && ( bitmask != 0 ) )
        {
          lutvalue |= EDGE_NEITHER0;
        }
        else
        {
          if ( ( aRisingEdge & bitmask ) != 0 )
          {
            lutvalue |= EDGE_RISE0;
          }
          if ( ( aFallingEdge & bitmask ) != 0 )
          {
            lutvalue |= EDGE_FALL0;
          }
        }
        bitmask >>>= 1;

        if ( ( i & 1 ) == 0 )
        {
          lutvalue <<= 16;
        }
        else
        {
          writeChain( lutvalue ); // write total of 256 bits
          lutvalue = 0;
        }
      }
    }

    /**
     * Setup LUT's for Range Detectors.
     * 
     * @param aRangeDetector
     *          the trigger range selector.
     * @throws IOException
     *           in case of I/O problems.
     */
    private void writeRange( final TriggerRangeDetector aRangeDetector ) throws IOException
    {
      int mask = aRangeDetector.getMask();
      int lowerTarget = ~( aRangeDetector.getLowerTarget() - 1 ) & 0xFFFFFFFF;
      int upperTarget = ~aRangeDetector.getUpperTarget() & 0xFFFFFFFF;

      int lutChainAddress = aRangeDetector.getType().getLutChainAddress();
      writeRangeValue( lutChainAddress, lowerTarget, mask );
      writeRangeValue( lutChainAddress + 1, upperTarget, mask );
    }

    /**
     * @param aLutChainAddress
     * @param aRangeValue
     * @param aMask
     */
    private void writeRangeValue( final int aLutChainAddress, long aRangeValue, int aMask ) throws IOException
    {
      writeSelect( aLutChainAddress );

      // Count # of bits in mask...
      int bitcount = Integer.bitCount( aMask );

      // Push MSB of target into bit 31...
      aRangeValue <<= ( 32 - bitcount ) & 0xFFFFFFFF;

      int lutValue = 0;

      // Generate & program LUT values. Total of 512 bits.
      for ( int i = 0; i < 16; i++ )
      {
        if ( ( ( aMask >>> 31 ) & 1 ) == 0 )
        {
          lutValue = RANGE_NOP;
        }
        else
        {
          lutValue = ( ( ( aRangeValue >>> 31 ) & 1 ) != 0 ) ? RANGE_XOR1 : RANGE_XOR0;
          aRangeValue <<= 1;
        }
        aMask <<= 1;
        lutValue <<= 16;

        if ( ( ( aMask >>> 31 ) & 1 ) == 0 )
        {
          lutValue |= RANGE_NOP;
        }
        else
        {
          lutValue |= ( ( ( aRangeValue >>> 31 ) & 1 ) != 0 ) ? RANGE_XOR1 : RANGE_XOR0;
          aRangeValue <<= 1;
        }
        aMask <<= 1;

        writeChain( lutValue );
      }

    }

    /**
     * Setup LUT's for Trigger Term Inputs are 32-bit target & mask for
     * comparing against captured analyzer data. If a mask bit is set, the
     * corresponding target bit participates in the trigger.
     * 
     * @param aTerm
     *          the trigger term to write.
     * @throws IOException
     *           in case of I/O problems.
     */
    private void writeTerm( final TriggerTerm aTerm ) throws IOException
    {
      int bitmask = 1;
      int lutvalue0 = 0;
      int lutvalue1 = 0;
      int lutvalue2 = 0;
      int lutvalue3 = 0;

      final int target = aTerm.getValue();
      final int mask = aTerm.getMask();

      for ( int i = 0; i < 16; i++ )
      {
        if ( ( ( i ^ ( target & 0xF ) ) & ( mask & 0xF ) ) == 0 )
        {
          lutvalue0 |= bitmask;
        }

        if ( ( ( i ^ ( ( target >>> 4 ) & 0xF ) ) & ( ( mask >>> 4 ) & 0xF ) ) == 0 )
        {
          lutvalue0 |= ( bitmask << 16 );
        }

        if ( ( ( i ^ ( ( target >>> 8 ) & 0xF ) ) & ( ( mask >>> 8 ) & 0xF ) ) == 0 )
        {
          lutvalue1 |= bitmask;
        }

        if ( ( ( i ^ ( ( target >>> 12 ) & 0xF ) ) & ( ( mask >>> 12 ) & 0xF ) ) == 0 )
        {
          lutvalue1 |= ( bitmask << 16 );
        }

        if ( ( ( i ^ ( ( target >>> 16 ) & 0xF ) ) & ( ( mask >>> 16 ) & 0xF ) ) == 0 )
        {
          lutvalue2 |= bitmask;
        }

        if ( ( ( i ^ ( ( target >>> 20 ) & 0xF ) ) & ( ( mask >>> 20 ) & 0xF ) ) == 0 )
        {
          lutvalue2 |= ( bitmask << 16 );
        }

        if ( ( ( i ^ ( ( target >>> 24 ) & 0xF ) ) & ( ( mask >>> 24 ) & 0xF ) ) == 0 )
        {
          lutvalue3 |= bitmask;
        }

        if ( ( ( i ^ ( ( target >>> 28 ) & 0xF ) ) & ( ( mask >>> 28 ) & 0xF ) ) == 0 )
        {
          lutvalue3 |= ( bitmask << 16 );
        }

        bitmask <<= 1;
      }

      // Write data into LUT serial chain. MSB must goes in first.
      // Total of 128 bits.
      writeSelect( aTerm.getType().getLutChainAddress() );
      writeChain( lutvalue3 );
      writeChain( lutvalue2 );
      writeChain( lutvalue1 );
      writeChain( lutvalue0 );
    }

    /**
     * Setup trigger timers.
     * 
     * @param aTimer
     *          the trigger timer to set;
     * @param aValue
     *          the 36-bit timer value to set.
     */
    private void writeTriggerTimer( final TriggerTimer aTimer ) throws IOException
    {
      final int lutChainAddress = aTimer.getType().getLutChainAddress();

      writeSelect( lutChainAddress );
      writeChain( ( int )( aTimer.getValue() & 0xFFFFFFFF ) );

      writeSelect( lutChainAddress + 1 );
      writeChain( ( int )( ( aTimer.getValue() >>> 32 ) & 0x0F ) );
    }
  }

  // CONSTANTS

  public static final int RANGE_XOR0 = 0xAAAA;
  public static final int RANGE_XOR1 = 0x5555;
  public static final int RANGE_NOP = 0xFFFF;

  public static final int EDGE_RISE0 = 0x0A0A;
  public static final int EDGE_RISE1 = 0x00CC;
  public static final int EDGE_FALL0 = 0x5050;
  public static final int EDGE_FALL1 = 0x3300;
  public static final int EDGE_BOTH0 = ( EDGE_RISE0 | EDGE_FALL0 );
  public static final int EDGE_BOTH1 = ( EDGE_RISE1 | EDGE_FALL1 );
  public static final int EDGE_NEITHER0 = ( ~EDGE_BOTH0 & 0xFFFF );
  public static final int EDGE_NEITHER1 = ( ~EDGE_BOTH1 & 0xFFFF );

  public static final int OP_NOP = 0;
  public static final int OP_ANY = 1;
  public static final int OP_AND = 2;
  public static final int OP_NAND = 3;
  public static final int OP_OR = 4;
  public static final int OP_NOR = 5;
  public static final int OP_XOR = 6;
  public static final int OP_NXOR = 7;
  public static final int OP_A = 8;
  public static final int OP_B = 9;

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

  // { NOP ANY AND NAND OR NOR XOR NXOR A B }
  private static final int[] PAIR_VALUE = { 0x0000, 0xFFFF, 0x8000, 0x7FFF, 0xF888, 0x0777, 0x7888, 0x8777, 0x8888,
      0xF000 };
  private static final int[] MID_VALUE = { 0x0000, 0xFFFF, 0x8000, 0x7FFF, 0xFFFE, 0x0001, 0x0116, 0xFEE9, 0xEEEE,
      0xFFF0 };
  private static final int[] FINAL_VALUE = { 0x0000, 0xFFFF, 0x0008, 0x0007, 0x000E, 0x0001, 0x0006, 0x0009, 0x0002,
      0x0004 };

  private static final int CMD_SELECT = 0x9E;
  private static final int CMD_CHAIN = 0x9F;

  // VARIABLES

  private final DataOutputStream outputStream;

  // CONSTRUCTORS

  /**
   * Creates a new {@link DemonCoreCommandWriter} instance.
   * 
   * @param aOutputStream
   *          the {@link DataOutputStream} to use.
   */
  public DemonCoreCommandWriter( final DataOutputStream aOutputStream )
  {
    this.outputStream = aOutputStream;
  }

  // METHODS

  /**
   * @param aValue
   * @throws IOException
   */
  final void writeChain( final int aValue ) throws IOException
  {
    this.outputStream.write( CMD_CHAIN );
    this.outputStream.writeInt( aValue );
  }

  /**
   * @param aValue
   * @throws IOException
   */
  final void writeSelect( final int aValue ) throws IOException
  {
    this.outputStream.write( CMD_SELECT );
    this.outputStream.writeInt( aValue );
  }

}
