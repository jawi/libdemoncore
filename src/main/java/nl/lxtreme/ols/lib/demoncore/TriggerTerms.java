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
 * Denotes the trigger terms for a single trigger sequence.
 */
public class TriggerTerms implements ITriggerVisitable
{
  // CONSTANTS

  // { NOP ANY AND NAND OR NOR XOR NXOR A B }
  private static final int[] PAIR_VALUE = { 0x0000, 0xFFFF, 0x8000, 0x7FFF, 0xF888, 0x0777, 0x7888, 0x8777, 0x8888,
      0xF000 };
  private static final int[] MID_VALUE = { 0x0000, 0xFFFF, 0x8000, 0x7FFF, 0xFFFE, 0x0001, 0x0116, 0xFEE9, 0xEEEE,
      0xFFF0 };
  private static final int[] FINAL_VALUE = { 0x0000, 0xFFFF, 0x0008, 0x0007, 0x000E, 0x0001, 0x0006, 0x0009, 0x0002,
      0x0004 };

  // VARIABLES

  private TriggerOperation a_b;
  private TriggerOperation c_range1;
  private TriggerOperation d_edge1;
  private TriggerOperation e_timer1;
  private TriggerOperation f_g;
  private TriggerOperation h_range2;
  private TriggerOperation i_edge2;
  private TriggerOperation j_timer2;
  private TriggerOperation midOp1;
  private TriggerOperation midOp2;
  private TriggerOperation finalOp;

  // CONSTRUCTORS

  /**
   * Creates a new TriggerTerms instance.
   */
  public TriggerTerms()
  {
    reset();
  }

  /**
   * Creates a new TriggerTerms instance.
   * 
   * @param aTerms
   *          the trigger terms.
   */
  public TriggerTerms( final TriggerTerms aTerms )
  {
    this.a_b = aTerms.a_b;
    this.c_range1 = aTerms.c_range1;
    this.d_edge1 = aTerms.d_edge1;
    this.e_timer1 = aTerms.e_timer1;
    this.f_g = aTerms.f_g;
    this.h_range2 = aTerms.h_range2;
    this.i_edge2 = aTerms.i_edge2;
    this.j_timer2 = aTerms.j_timer2;
    this.midOp1 = aTerms.midOp1;
    this.midOp2 = aTerms.midOp2;
    this.finalOp = aTerms.finalOp;
  }

  // METHODS

  /**
   * {@inheritDoc}
   */
  @Override
  public void accept( final ITriggerVisitor aVisitor ) throws IOException
  {
    aVisitor.writeChain( FINAL_VALUE[this.finalOp.getOffset()] );
    aVisitor.writeChain( ( MID_VALUE[this.midOp2.getOffset()] << 16 ) | MID_VALUE[this.midOp1.getOffset()] );
    aVisitor.writeChain( ( PAIR_VALUE[this.j_timer2.getOffset()] << 16 ) | PAIR_VALUE[this.i_edge2.getOffset()] );
    aVisitor.writeChain( ( PAIR_VALUE[this.h_range2.getOffset()] << 16 ) | PAIR_VALUE[this.f_g.getOffset()] );
    aVisitor.writeChain( ( PAIR_VALUE[this.e_timer1.getOffset()] << 16 ) | PAIR_VALUE[this.d_edge1.getOffset()] );
    aVisitor.writeChain( ( PAIR_VALUE[this.c_range1.getOffset()] << 16 ) | PAIR_VALUE[this.a_b.getOffset()] );
  }

  /**
   * Returns the final operation.
   * 
   * @return the logical operation, never <code>null</code>.
   */
  public TriggerOperation getFinalOperation()
  {
    return this.finalOp;
  }

  /**
   * Returns the first/upper middle operation.
   * 
   * @return the logical operation, never <code>null</code>.
   */
  public TriggerOperation getMiddleOperation1()
  {
    return this.midOp1;
  }

  /**
   * Returns the second/lower middle operation.
   * 
   * @return the logical operation, never <code>null</code>.
   */
  public TriggerOperation getMiddleOperation2()
  {
    return this.midOp2;
  }

  /**
   * Returns the operation of terms A & B.
   * 
   * @return the logical operation, never <code>null</code>.
   */
  public TriggerOperation getOperationAB()
  {
    return this.a_b;
  }

  /**
   * Returns the operation of term C & range 1.
   * 
   * @return the logical operation, never <code>null</code>.
   */
  public TriggerOperation getOperationCRange1()
  {
    return this.c_range1;
  }

  /**
   * Returns the operation of term D & edge 1.
   * 
   * @return the logical operation, never <code>null</code>.
   */
  public TriggerOperation getOperationDEdge1()
  {
    return this.d_edge1;
  }

  /**
   * Returns the operation of term E & timer 1.
   * 
   * @return the logical operation, never <code>null</code>.
   */
  public TriggerOperation getOperationETimer1()
  {
    return this.e_timer1;
  }

  /**
   * Returns the operation of terms F & G.
   * 
   * @return the logical operation, never <code>null</code>.
   */
  public TriggerOperation getOperationFG()
  {
    return this.f_g;
  }

  /**
   * Returns the operation of term H & range 2.
   * 
   * @return the logical operation, never <code>null</code>.
   */
  public TriggerOperation getOperationHRange2()
  {
    return this.h_range2;
  }

  /**
   * Returns the operation of term I & edge 2.
   * 
   * @return the logical operation, never <code>null</code>.
   */
  public TriggerOperation getOperationIEdge2()
  {
    return this.i_edge2;
  }

  /**
   * Returns the operation of term J & timer 2.
   * 
   * @return the logical operation, never <code>null</code>.
   */
  public TriggerOperation getOperationJTimer2()
  {
    return this.j_timer2;
  }

  /**
   * Sets final operation.
   * 
   * @param aOperation
   *          the operation to set, cannot be <code>null</code>.
   */
  public void setFinalOperation( final TriggerOperation aOperation )
  {
    if ( aOperation == null )
    {
      throw new IllegalArgumentException( "Operation cannot be null!" );
    }
    this.finalOp = aOperation;
  }

  /**
   * Sets first/upper middle operation.
   * 
   * @param aOperation
   *          the operation to set, cannot be <code>null</code>.
   */
  public void setMiddleOperation1( final TriggerOperation aOperation )
  {
    if ( aOperation == null )
    {
      throw new IllegalArgumentException( "Operation cannot be null!" );
    }
    this.midOp1 = aOperation;
  }

  /**
   * Sets second/lower middle operation.
   * 
   * @param aOperation
   *          the operation to set, cannot be <code>null</code>.
   */
  public void setMiddleOperation2( final TriggerOperation aOperation )
  {
    if ( aOperation == null )
    {
      throw new IllegalArgumentException( "Operation cannot be null!" );
    }
    this.midOp2 = aOperation;
  }

  /**
   * Sets the operation for terms A & B.
   * 
   * @param aOperation
   *          the operation to set, cannot be <code>null</code>.
   */
  public void setOperationAB( final TriggerOperation aOperation )
  {
    if ( aOperation == null )
    {
      throw new IllegalArgumentException( "Operation cannot be null!" );
    }
    this.a_b = aOperation;
  }

  /**
   * Sets the operation for term C & range 1.
   * 
   * @param aOperation
   *          the operation to set, cannot be <code>null</code>.
   */
  public void setOperationCRange1( final TriggerOperation aOperation )
  {
    if ( aOperation == null )
    {
      throw new IllegalArgumentException( "Operation cannot be null!" );
    }
    this.c_range1 = aOperation;
  }

  /**
   * Sets the operation for term D & edge 1.
   * 
   * @param aOperation
   *          the operation to set, cannot be <code>null</code>.
   */
  public void setOperationDEdge1( final TriggerOperation aOperation )
  {
    if ( aOperation == null )
    {
      throw new IllegalArgumentException( "Operation cannot be null!" );
    }
    this.d_edge1 = aOperation;
  }

  /**
   * Sets the operation for term E & timer 1.
   * 
   * @param aOperation
   *          the operation to set, cannot be <code>null</code>.
   */
  public void setOperationETimer1( final TriggerOperation aOperation )
  {
    if ( aOperation == null )
    {
      throw new IllegalArgumentException( "Operation cannot be null!" );
    }
    this.e_timer1 = aOperation;
  }

  /**
   * Sets the operation for terms F & G.
   * 
   * @param aOperation
   *          the operation to set, cannot be <code>null</code>.
   */
  public void setOperationFG( final TriggerOperation aOperation )
  {
    if ( aOperation == null )
    {
      throw new IllegalArgumentException( "Operation cannot be null!" );
    }
    this.f_g = aOperation;
  }

  /**
   * Sets the operation for term H & range 2.
   * 
   * @param aOperation
   *          the operation to set, cannot be <code>null</code>.
   */
  public void setOperationHRange2( final TriggerOperation aOperation )
  {
    if ( aOperation == null )
    {
      throw new IllegalArgumentException( "Operation cannot be null!" );
    }
    this.h_range2 = aOperation;
  }

  /**
   * Sets the operation for term I & edge 2.
   * 
   * @param aOperation
   *          the operation to set, cannot be <code>null</code>.
   */
  public void setOperationIEdge2( final TriggerOperation aOperation )
  {
    if ( aOperation == null )
    {
      throw new IllegalArgumentException( "Operation cannot be null!" );
    }
    this.i_edge2 = aOperation;
  }

  /**
   * Sets the operation for term J & timer 2.
   * 
   * @param aOperation
   *          the operation to set, cannot be <code>null</code>.
   */
  public void setOperationJTimer2( final TriggerOperation aOperation )
  {
    if ( aOperation == null )
    {
      throw new IllegalArgumentException( "Operation cannot be null!" );
    }
    this.j_timer2 = aOperation;
  }

  /**
   * Resets this trigger sum to its initial state.
   */
  void reset()
  {
    this.a_b = TriggerOperation.NOP;
    this.c_range1 = TriggerOperation.NOP;
    this.d_edge1 = TriggerOperation.NOP;
    this.e_timer1 = TriggerOperation.NOP;
    this.f_g = TriggerOperation.NOP;
    this.h_range2 = TriggerOperation.NOP;
    this.i_edge2 = TriggerOperation.NOP;
    this.j_timer2 = TriggerOperation.NOP;
    this.midOp1 = TriggerOperation.NOP;
    this.midOp2 = TriggerOperation.NOP;
    this.finalOp = TriggerOperation.NOP;
  }
}
