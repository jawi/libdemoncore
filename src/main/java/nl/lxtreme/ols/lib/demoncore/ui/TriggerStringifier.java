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


import static nl.lxtreme.ols.lib.demoncore.ui.DemonCore.*;
import java.io.*;
import java.util.*;

import nl.lxtreme.ols.lib.demoncore.*;


/**
 * Converts a trigger(part) to a single, human readable, string representation.
 */
public class TriggerStringifier implements ITriggerVisitor
{
  // CONSTANTS

  private static final boolean USE_NEGATION_LINE = true;

  protected static final String NEGATE_STR = "\u0305";
  protected static final String NOT_STR = "\u00AC";
  protected static final String XOR_STR = "\u2295";
  protected static final String OR_STR = "+";
  protected static final String AND_STR = "\u22C5";

  private static final int FINAL_TERM_SIZE = 2;
  private static final int MID_TERM_SIZE = 4;
  private static final int PAIR_TERM_SIZE = 2;

  private static final ResourceBundle RB = ResourceBundle.getBundle( DemonCore.class.getName() );

  // VARIABLES

  private final Stack<String> symStack = new Stack<String>();

  // METHODS

  /**
   * Converts the given term to a string representation.
   * 
   * @param aTerm
   *          the trigger term to convert to string, cannot be <code>null</code>
   *          .
   * @return a string representation of the given term, never <code>null</code>.
   */
  public static String asString( final AbstractTriggerTerm aTerm )
  {
    final String resourceKey;
    switch ( aTerm.getType() )
    {
      case TERM_A:
        resourceKey = rTERM_A;
        break;
      case TERM_B:
        resourceKey = rTERM_B;
        break;
      case TERM_C:
        resourceKey = rTERM_C;
        break;
      case TERM_D:
        resourceKey = rTERM_D;
        break;
      case TERM_E:
        resourceKey = rTERM_E;
        break;
      case TERM_F:
        resourceKey = rTERM_F;
        break;
      case TERM_G:
        resourceKey = rTERM_G;
        break;
      case TERM_H:
        resourceKey = rTERM_H;
        break;
      case TERM_I:
        resourceKey = rTERM_I;
        break;
      case TERM_J:
        resourceKey = rTERM_J;
        break;
      case TERM_EDGE1:
        resourceKey = rEDGE1;
        break;
      case TERM_EDGE2:
        resourceKey = rEDGE2;
        break;
      case TERM_RANGE1:
        resourceKey = rIN_RANGE1;
        break;
      case TERM_RANGE2:
        resourceKey = rIN_RANGE2;
        break;
      case TERM_TIMER1:
        resourceKey = rTIMER1;
        break;
      case TERM_TIMER2:
        resourceKey = rTIMER2;
        break;
      default:
        throw new IllegalArgumentException( "Unknown term type: " + aTerm.getType() );
    }
    return RB.getString( resourceKey );
  }

  /**
   * Converts the given trigger operation to a string representation.
   * 
   * @param aOperation
   *          the operation to convert, cannot be <code>null</code>.
   * @return the string representation of the given operation, never
   *         <code>null</code>.
   */
  public static String asString( final TriggerOperation aOperation )
  {
    switch ( aOperation )
    {
      case A_ONLY:
      case B_ONLY:
        return "";

      case ANY:
        return RB.getString( rANY );

      case NOP:
        return RB.getString( rNOP );

      case NAND:
      case AND:
        return AND_STR; // middle dot

      case NOR:
      case OR:
        return OR_STR; // plus symbol

      case NXOR:
      case XOR:
        return XOR_STR; // circled plus

      default:
        throw new IllegalArgumentException( "Unknown operation: " + aOperation );
    }
  }

  /**
   * Converts a given {@link TriggerSum} to a human readable string
   * representation.
   * 
   * @param aTriggerSum
   *          the trigger sum to convert to a string representation, cannot be
   *          <code>null</code>.
   * @return a human readable string representation of the given trigger sum, or
   *         an empty string if the given trigger sum did not provide any terms.
   * @throws IllegalArgumentException
   *           in case the given argument was <code>null</code>.
   */
  public final String toString( final ITriggerVisitable aTriggerVisitable )
  {
    if ( aTriggerVisitable == null )
    {
      throw new IllegalArgumentException( "TriggerVisitable cannot be null!" );
    }

    this.symStack.clear();

    try
    {
      aTriggerVisitable.accept( this );
    }
    catch ( IOException exception )
    {
      exception.printStackTrace();
    }

    final StringBuilder sb = new StringBuilder();
    while ( !this.symStack.isEmpty() )
    {
      final String term = this.symStack.pop();
      if ( term != null )
      {
        sb.insert( 0, term );
      }
    }

    return sb.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void visit( final AbstractTriggerTerm aTerm ) throws IOException
  {
    String termStr = null;
    if ( !aTerm.isDisabled() )
    {
      termStr = asDecoratedString( aTerm );
    }
    this.symStack.push( termStr );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void visit( final TriggerFinalTerm aTerm ) throws IOException
  {
    // Walk across the stack and simplify its entries...
    walkSymbolStack( aTerm.getOperation(), FINAL_TERM_SIZE );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void visit( final TriggerMidTerm aTerm ) throws IOException
  {
    // Walk across the stack and simplify its entries...
    walkSymbolStack( aTerm.getOperation(), MID_TERM_SIZE );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void visit( final TriggerPairTerm aTerm ) throws IOException
  {
    // Walk across the stack and simplify its entries...
    walkSymbolStack( aTerm.getOperation(), PAIR_TERM_SIZE );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void visit( final TriggerSequenceState aTriggerSequenceState ) throws IOException
  {
    StringBuilder result = new StringBuilder();
    if ( !this.symStack.isEmpty() )
    {
      String elseTerm = this.symStack.pop();
      if ( elseTerm != null )
      {
        Integer elseState = Integer.valueOf( aTriggerSequenceState.getElseState() );
        result.insert( 0, String.format( "%s %s %s %d\n", RB.getString( "rElseOn" ), elseTerm,
            RB.getString( "rGotoLevel" ), elseState ) );
      }
    }
    if ( !this.symStack.isEmpty() )
    {
      String hitTerm = this.symStack.pop();
      if ( hitTerm != null )
      {
        Integer occurrenceCount = Integer.valueOf( aTriggerSequenceState.getOccurrenceCount() );
        result.insert( 0, String.format( "%s %s %s %d %s\n", RB.getString( "rIfTarget" ), hitTerm,
            RB.getString( "rOccurs" ), occurrenceCount, RB.getString( "rSamples" ) ) );
      }
    }
    if ( !this.symStack.isEmpty() )
    {
      String captureTerm = this.symStack.pop();
      if ( captureTerm != null )
      {
        result.insert( 0, String.format( "%s %s\n", RB.getString( "rWhileStoring" ), captureTerm ) );
      }
    }

    this.symStack.push( result.toString().trim() );
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
   * Converts the given term to a string representation.
   * 
   * @param aTerm
   *          the trigger term to convert to a string representation, cannot be
   *          <code>null</code>.
   * @return a string representation of the given term, never <code>null</code>.
   */
  private String asDecoratedString( final AbstractTriggerTerm aTerm )
  {
    StringBuilder sb = new StringBuilder( asString( aTerm ) );
    if ( aTerm.isInverted() )
    {
      if ( !USE_NEGATION_LINE || ( sb.length() > 1 ) )
      {
        sb.insert( 0, NOT_STR );
      }
      else
      {
        sb.append( NEGATE_STR );
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
   * @return a string representation, can be <code>null</code> if the given term
   *         list is empty.
   */
  private String combineTerms( final List<String> aTermList, final TriggerOperation aOperation )
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
      result = "(".concat( result ).concat( ")" );
    }
    if ( aOperation.isInverted() )
    {
      result = NOT_STR.concat( result );
    }

    return result;
  }

  /**
   * Walks across the top of the symbol stack and concatenates up to a given
   * number of items (= arity) of the stack. The result of this operation is
   * pushed onto the stack as replacement.
   * 
   * @param aOperation
   *          the operation to apply to the stack items;
   * @param aArity
   *          the number of operators the operator takes, >= 0.
   */
  private void walkSymbolStack( final TriggerOperation aOperation, final int aArity )
  {
    final int depth = Math.max( 0, this.symStack.size() - aArity );

    final List<String> terms = new ArrayList<String>();
    while ( !this.symStack.isEmpty() && ( this.symStack.size() > depth ) )
    {
      final String term = this.symStack.pop();
      if ( term != null )
      {
        terms.add( term );
      }
    }

    this.symStack.push( combineTerms( terms, aOperation ) );
  }
}
