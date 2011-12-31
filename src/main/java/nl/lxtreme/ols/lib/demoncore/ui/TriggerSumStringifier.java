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


import java.io.*;
import java.util.*;

import nl.lxtreme.ols.lib.demoncore.*;


/**
 * Converts a {@link TriggerSum} to a single, human readable, string
 * representation.
 */
final class TriggerSumStringifier implements ITriggerVisitor
{
  // CONSTANTS

  private static final boolean USE_NEGATION_LINE = true;

  private static final String NEGATE = "\u0305";
  private static final String NOT = "\u00AC";

  private static final ResourceBundle RB = ResourceBundle.getBundle( "nl.lxtreme.ols.lib.demoncore.ui.DemonCore" );

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
    return RB.getString( aTerm.getType().getResourceKey() );
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
        return "any state";

      case NOP:
        return "no state";

      case NAND:
      case AND:
        return "\u22C5"; // middle dot

      case NOR:
      case OR:
        return "+"; // plus symbol

      case NXOR:
      case XOR:
        return "\u2295"; // circled plus

      default:
        throw new IllegalArgumentException();
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
   */
  public String toString( final TriggerSum aTriggerSum )
  {
    this.symStack.clear();

    try
    {
      aTriggerSum.accept( this );
    }
    catch ( IOException exception )
    {
      exception.printStackTrace();
    }

    final StringBuilder sb = new StringBuilder();
    while ( !this.symStack.isEmpty() )
    {
      sb.insert( 0, this.symStack.pop() );
    }

    return sb.toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void visit( final AbstractTriggerTerm aTerm ) throws IOException
  {
    if ( !aTerm.isDisabled() )
    {
      this.symStack.push( asDecoratedString( aTerm ) );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void visit( final TriggerFinalTerm aTerm ) throws IOException
  {
    int depth = this.symStack.size();

    aTerm.getTermB().accept( this );
    aTerm.getTermA().accept( this );

    // Walk across the stack and simplify its entries...
    walkSymbolStack( aTerm.getOperation(), depth );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void visit( final TriggerMidTerm aTerm ) throws IOException
  {
    int depth = this.symStack.size();

    // Visit the terms individually first...
    aTerm.getTermD().accept( this );
    aTerm.getTermC().accept( this );
    aTerm.getTermB().accept( this );
    aTerm.getTermA().accept( this );

    // Walk across the stack and simplify its entries...
    walkSymbolStack( aTerm.getOperation(), depth );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void visit( final TriggerPairTerm aTerm ) throws IOException
  {
    int depth = this.symStack.size();

    // Visit the terms individually first...
    aTerm.getTermB().accept( this );
    aTerm.getTermA().accept( this );

    // Walk across the stack and simplify its entries...
    walkSymbolStack( aTerm.getOperation(), depth );
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
   * Converts the given term to a string representation.
   * 
   * @param aTerm
   *          the trigger term to convert to string, cannot be <code>null</code>
   *          .
   * @return a string representation of the given term, never <code>null</code>.
   */
  private String asDecoratedString( final AbstractTriggerTerm aTerm )
  {
    StringBuilder sb = new StringBuilder( RB.getString( aTerm.getType().getResourceKey() ) );
    if ( aTerm.isInverted() )
    {
      if ( !USE_NEGATION_LINE || ( sb.length() > 1 ) )
      {
        sb.insert( 0, NOT );
      }
      else
      {
        sb.append( NEGATE );
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
  private String asString( final List<String> aTermList, final TriggerOperation aOperation )
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
      result = "(".concat( result.concat( ")" ) );
    }
    if ( aOperation.isInverted() )
    {
      result = NOT.concat( result );
    }

    return result;
  }

  /**
   * @param symStack
   * @param aTerm
   */
  private void walkSymbolStack( final TriggerOperation aOperation, final int aDepth )
  {
    List<String> terms = new ArrayList<String>();
    while ( !this.symStack.isEmpty() && ( this.symStack.size() > aDepth ) )
    {
      terms.add( this.symStack.pop() );
    }

    String value = asString( terms, aOperation );
    if ( ( value != null ) && ( value.length() > 0 ) )
    {
      this.symStack.push( value );
    }
  }
}
