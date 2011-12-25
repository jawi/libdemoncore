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
   * Setup LUT's for Edge Detectors.
   * 
   * @param aEdge
   *          selects which edge we want to select & write trigger data for;
   * @param aRisingEdge
   *          the bitmask denoting all channels on which a rising edge should be
   *          detected;
   * @param aFallingEdge
   *          the bitmask denoting all channels on which a falling edge should
   *          be detected;
   * @param aNeitherEdge
   *          ??? XXX
   * @throws IOException
   *           in case of I/O problems.
   */
  public void writeEdge( final TriggerEdge aEdge, final int aRisingEdge, final int aFallingEdge, final int aNeitherEdge )
      throws IOException
  {
    writeSelect( aEdge.getLutChainAddress() );

    int lutvalue = 0;

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
   * @param aRange
   *          the trigger range selector;
   * @param aTarget
   *          the target upper/lower limit value to set;
   * @param aMask
   *          the target mask to set (indicates which bits should participate in
   *          range compare).
   * @throws IOException
   *           in case of I/O problems.
   */
  public void writeRange( final TriggerRange aRange, final int aTarget, int aMask ) throws IOException
  {
    long value;
    int lutValue = 0;
    int i;

    writeSelect( aRange.getLutChainAddress() );

    // Count # of bits in mask...
    int bitcount = Integer.bitCount( aMask );

    // Prepare target value...
    if ( aRange.isLowerRange() )
    {
      // lower range value
      value = ~( aTarget - 1 ) & 0xFFFFFFFF;
    }
    else
    {
      // upper range target
      value = ~aTarget & 0xFFFFFFFF;
    }

    // Push MSB of target into bit 31...
    value <<= ( 32 - bitcount ) & 0xFFFFFFFF;

    // Generate & program LUT values. Total of 512 bits.
    for ( i = 0; i < 16; i++ )
    {
      if ( ( ( aMask >>> 31 ) & 1 ) == 0 )
      {
        lutValue = RANGE_NOP;
      }
      else
      {
        lutValue = ( ( ( value >>> 31 ) & 1 ) != 0 ) ? RANGE_XOR1 : RANGE_XOR0;
        value <<= 1;
      }
      aMask <<= 1;
      lutValue <<= 16;

      if ( ( ( aMask >>> 31 ) & 1 ) == 0 )
      {
        lutValue |= RANGE_NOP;
      }
      else
      {
        lutValue |= ( ( ( value >>> 31 ) & 1 ) != 0 ) ? RANGE_XOR1 : RANGE_XOR0;
        value <<= 1;
      }
      aMask <<= 1;

      writeChain( lutValue );
    }
  }

  /**
   * Setup LUT's for Trigger Term Inputs are 32-bit target & mask for comparing
   * against captured analyzer data. If a mask bit is set, the corresponding
   * target bit participates in the trigger.
   * 
   * @param aTerm
   *          the trigger term to write;
   * @param aTarget
   *          the trigger target value;
   * @param aMask
   *          the trigger mask value.
   * @throws IOException
   *           in case of I/O problems.
   */
  public void writeTerm( final TriggerTerm aTerm, final int aTarget, final int aMask ) throws IOException
  {
    int bitmask = 1;
    int lutvalue0 = 0;
    int lutvalue1 = 0;
    int lutvalue2 = 0;
    int lutvalue3 = 0;

    for ( int i = 0; i < 16; i++ )
    {
      if ( ( ( i ^ ( aTarget & 0xF ) ) & ( aMask & 0xF ) ) == 0 )
      {
        lutvalue0 |= bitmask;
      }

      if ( ( ( i ^ ( ( aTarget >>> 4 ) & 0xF ) ) & ( ( aMask >>> 4 ) & 0xF ) ) == 0 )
      {
        lutvalue0 |= ( bitmask << 16 );
      }

      if ( ( ( i ^ ( ( aTarget >>> 8 ) & 0xF ) ) & ( ( aMask >>> 8 ) & 0xF ) ) == 0 )
      {
        lutvalue1 |= bitmask;
      }

      if ( ( ( i ^ ( ( aTarget >>> 12 ) & 0xF ) ) & ( ( aMask >>> 12 ) & 0xF ) ) == 0 )
      {
        lutvalue1 |= ( bitmask << 16 );
      }

      if ( ( ( i ^ ( ( aTarget >>> 16 ) & 0xF ) ) & ( ( aMask >>> 16 ) & 0xF ) ) == 0 )
      {
        lutvalue2 |= bitmask;
      }

      if ( ( ( i ^ ( ( aTarget >>> 20 ) & 0xF ) ) & ( ( aMask >>> 20 ) & 0xF ) ) == 0 )
      {
        lutvalue2 |= ( bitmask << 16 );
      }

      if ( ( ( i ^ ( ( aTarget >>> 24 ) & 0xF ) ) & ( ( aMask >>> 24 ) & 0xF ) ) == 0 )
      {
        lutvalue3 |= bitmask;
      }

      if ( ( ( i ^ ( ( aTarget >>> 28 ) & 0xF ) ) & ( ( aMask >>> 28 ) & 0xF ) ) == 0 )
      {
        lutvalue3 |= ( bitmask << 16 );
      }

      bitmask <<= 1;
    }

    // Write data into LUT serial chain. MSB must goes in first.
    // Total of 128 bits.
    writeSelect( aTerm.getLutChainAddress() );
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
  public void writeTriggerLimit( final TriggerTimer aTimer, final long aValue ) throws IOException
  {
    writeSelect( aTimer.getLutChainAddress() );
    writeChain( ( int )( aValue & 0xFFFFFFFF ) );
    writeSelect( aTimer.getLutChainAddress() + 1 );
    writeChain( ( int )( ( aValue >>> 32 ) & 0x0F ) );
  }

  /**
   * @param aValue
   * @throws IOException
   */
  private void writeChain( final int aValue ) throws IOException
  {
    // sendCommand( CMD_CHAIN, aValue );
  }

  /**
   * @param aValue
   * @throws IOException
   */
  private void writeSelect( final int aValue ) throws IOException
  {
    // sendCommand( CMD_SELECT, aValue );
  }
}
