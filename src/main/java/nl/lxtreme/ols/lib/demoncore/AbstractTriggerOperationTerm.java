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


/**
 * 
 */
public abstract class AbstractTriggerOperationTerm implements ITriggerVisitable
{
  // VARIABLES

  private TriggerOperation operation;

  // CONSTRUCTORS

  /**
   * Creates a new AbstractTriggerOperationTerm instance.
   */
  protected AbstractTriggerOperationTerm()
  {
    // No-op
  }

  /**
   * Creates a new AbstractTriggerOperationTerm instance.
   */
  protected AbstractTriggerOperationTerm( final AbstractTriggerOperationTerm aOperationTerm )
  {
    this.operation = aOperationTerm.operation;
  }

  // METHODS

  /**
   * Returns the current value of operation.
   * 
   * @return the operation
   */
  public final TriggerOperation getOperation()
  {
    return this.operation;
  }

  /**
   * Sets operation to the given value.
   * 
   * @param aOperation
   *          the operation to set.
   */
  public final void setOperation( final TriggerOperation aOperation )
  {
    if ( aOperation == null )
    {
      throw new IllegalArgumentException( "Operation cannot be null!" );
    }
    this.operation = aOperation;
  }

  /**
   * @return the offset of the current operation.
   */
  protected final int getOffset()
  {
    return this.operation.getOffset();
  }

  /**
   * Resets this trigger input term to its initial state.
   */
  protected void reset()
  {
    this.operation = TriggerOperation.OR;
  }
}
