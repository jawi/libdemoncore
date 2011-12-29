package nl.lxtreme.ols.lib.demoncore.ui;


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

import java.util.*;


/**
 * Resources for the libdemoncore library.
 */
public class DemonCore extends ResourceBundle
{
  // CONSTANTS

  public static final String rTERM_A = "rA";
  public static final String rTERM_B = "rB";
  public static final String rTERM_C = "rC";
  public static final String rTERM_D = "rD";
  public static final String rTERM_E = "rE";
  public static final String rTERM_F = "rF";
  public static final String rTERM_G = "rG";
  public static final String rTERM_H = "rH";
  public static final String rTERM_I = "rI";
  public static final String rTERM_J = "rJ";
  public static final String rIN_RANGE1 = "rInRange1";
  public static final String rIN_RANGE2 = "rInRange2";
  public static final String rEDGE1 = "rEdge1";
  public static final String rEDGE2 = "rEdge2";
  public static final String rTIMER1 = "rTimer1";
  public static final String rTIMER2 = "rTimer2";
  public static final String rAND = "rAnd";
  public static final String rNAND = "rNand";
  public static final String rOR = "rOr";
  public static final String rNOR = "rNor";
  public static final String rXOR = "rXor";
  public static final String rXNOR = "rXNor";
  public static final String rA_ONLY = "rAOnly";
  public static final String rB_ONLY = "rBOnly";
  public static final String rANY = "rAny";
  public static final String rNOP = "rNop";

  private static final Object[][] CONTENTS = {
      // Trigger sequence resources...
      { "rTIME", "Timing" }, //
      { "rSTATE", "State" }, //
      { "rSequenceLevel", "%s sequence level %d" }, //
      { "rWhileStoring", "While storing" }, //
      { "rIfTarget", "If target" }, //
      { "rOccurs", "occurs for" }, //
      { "rSamples", "samples" }, //
      { "rOnHit", "On hit" }, //
      { "rElseOn", "else" }, //
      { "rGotoLevel", "go to level" }, //
      { "rSetTrigger", "Set trigger" }, //
      { "rTimerControl", "Timer control..." }, //

      // Trigger sum resources...
      { rTERM_A, "A" }, //
      { rTERM_B, "B" }, //
      { rTERM_C, "C" }, //
      { rTERM_D, "D" }, //
      { rTERM_E, "E" }, //
      { rTERM_F, "F" }, //
      { rTERM_G, "G" }, //
      { rTERM_H, "H" }, //
      { rTERM_I, "I" }, //
      { rTERM_J, "J" }, //
      { rIN_RANGE1, "In range 1" }, //
      { rIN_RANGE2, "In range 2" }, //
      { rEDGE1, "Edge 1" }, //
      { rEDGE2, "Edge 2" }, //
      { rTIMER1, "Timer 1" }, //
      { rTIMER2, "Timer 2" }, //
      { rAND, "AND" }, //
      { rNAND, "NAND" }, //
      { rOR, "OR" }, //
      { rNOR, "NOR" }, //
      { rXOR, "XOR" }, //
      { rXNOR, "NXOR" }, //
      { rA_ONLY, "A only" }, //
      { rB_ONLY, "B only" }, //
      { rANY, "Any (= 1)" }, //
      { rNOP, "Nop (= 0)" }, //
      // Combines resources...
      { "rInputStageNames", new String[] { rTERM_A, rTERM_B, rTERM_C, //
          rIN_RANGE1, rTERM_D, rEDGE1, rTERM_E, rTIMER1, rTERM_F, //
          rTERM_G, rTERM_H, rIN_RANGE2, rTERM_I, rEDGE2, rTERM_J, rTIMER2 } }, //
      { "rInputLogicalOperations", new String[] { rAND, rNAND, //
          rOR, rNOR, rXOR, rXNOR, rA_ONLY, rB_ONLY, rANY, rNOP } }, //
      { "rOtherLogicalOperations", new String[] { rAND, rNAND, //
          rOR, rNOR, rXOR, rXNOR, rANY, rNOP } }, //
  };

  // VARIABLES

  private final Map<String, Object> entries;

  // CONSTRUCTORS

  /**
   * Creates a new DemonCore instance.
   */
  public DemonCore()
  {
    this.entries = new HashMap<String, Object>( DemonCore.CONTENTS.length );
    for ( Object[] contentLine : DemonCore.CONTENTS )
    {
      this.entries.put( String.valueOf( contentLine[0] ), contentLine[1] );
    }
  }

  // METHODS

  /**
   * {@inheritDoc}
   */
  @Override
  public Enumeration<String> getKeys()
  {
    return new Enumeration<String>()
    {
      final Iterator<String> iter = DemonCore.this.entries.keySet().iterator();

      @Override
      public boolean hasMoreElements()
      {
        return this.iter.hasNext();
      }

      @Override
      public String nextElement()
      {
        return this.iter.next();
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected Object handleGetObject( final String aKey )
  {
    Object value = this.entries.get( aKey );
    if ( this.entries.containsKey( value ) )
    {
      value = this.entries.get( value );
    }
    else if ( value instanceof Object[] )
    {
      // Do an in-place replacement of any contained resource keys...
      Object[] tmp = ( String[] )value;
      for ( int i = 0; i < tmp.length; i++ )
      {
        if ( this.entries.containsKey( tmp[i] ) )
        {
          tmp[i] = this.entries.get( tmp[i] );
        }
      }
    }
    return value;
  }
}
